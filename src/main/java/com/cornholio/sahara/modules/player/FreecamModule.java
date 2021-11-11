package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import com.cornholio.sahara.utils.PlayerUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.*;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


public class FreecamModule extends Module
{
    private EntityOtherPlayerMP FakeCamera;
    ModuleSetting isCameraInteracting;
    ModuleSetting speed;
    ModuleSetting look;

    public FreecamModule()
    {
        super("Freecam", "Makes your camera go rogue, stay seif boiii", ModuleCategory.Player);
        this.setKey(Keyboard.KEY_C);

        this.registerSetting(isCameraInteracting = new ModuleSetting("Camera Interaction", false));
        this.registerSetting(speed = new ModuleSetting("Speed", 0, 20, 1));
        this.registerSetting(look = new ModuleSetting("Look", false));
    }

    @Override
    public void onEnable()
    {
        super.onEnable();
        //SaharaClient.getSahara().sendMessage("ON");
        resetKeys();

        mc.renderChunksMany = true;
        FakeCamera = new FakeCamera(mc.world).Init(mc.player);

        mc.world.addEntityToWorld(-1769, FakeCamera);
        mc.setRenderViewEntity(FakeCamera);
    }

    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event)
    {
        if(look.getBoolean() && mc.objectMouseOver != null && mc.player != null &&mc.objectMouseOver.getBlockPos() != null)
        {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            float[] lookingAt = PlayerUtils.getLookAt(new Vec3d(pos.getX(),pos.getY(),pos.getZ()), new Vec3d(0.5, 0, 0.5));
            mc.player.rotationYaw = lookingAt[0];
            mc.player.rotationPitch = lookingAt[1];
        }
    }


    @SubscribeEvent
    public void onKeyUpdate(InputUpdateEvent event)
    {
        resetKeys();
    }

    private void resetKeys()
    {
        MovementInput input =  mc.player.movementInput;

        input.moveForward = 0;
        input.moveStrafe = 0;
        input.forwardKeyDown = false;
        input.backKeyDown = false;
        input.rightKeyDown = false;
        input.leftKeyDown = false;
        input.jump = false;
        input.sneak = false;
    }

    @Override
    public boolean onTurn(Entity entity, float yaw, float pitch, CallbackInfo ci)
    {
        if(!isActive() || mc.player != entity) return false;
        if(FakeCamera != null)
            FakeCamera.turn(yaw, pitch);
        return true;
    }

    public void onDisable()
    {
        super.onDisable();
        //SaharaClient.getSahara().sendMessage("OFF");
        if(FakeCamera != null)
            mc.world.removeEntity(FakeCamera);
        mc.renderChunksMany = false;
        mc.setRenderViewEntity(mc.player);
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        if(event.getPacket() instanceof CPacketUseEntity)
        {
            CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if(packet.getEntityFromWorld(mc.world)==mc.player) //AUEGH DON'T PLAY WITH YOURSELF
                event.setCanceled(true);
        }
    }

    public Entity getRenderViewEntity()
    {
        if(isCameraInteracting.getBoolean() && isActive())
            return FakeCamera;

        return mc.player;
    }

    private class FakeCamera extends EntityOtherPlayerMP
    {
        FakeCamera(World world)
        {
            super(world, mc.getSession().getProfile());
        }

        public FakeCamera Init(EntityPlayerSP player)
        {
            this.copyLocationAndAnglesFrom(player);
            this.capabilities.isFlying = true;
            this.capabilities.allowFlying = true;

            return this;
        }

        @Override
        public void onLivingUpdate() {

            updateEntityActionState();

            //fuck java for not treating bools like the chad c
            int forward = mc.gameSettings.keyBindForward.isKeyDown() ? 1 : mc.gameSettings.keyBindBack.isKeyDown() ? -1 : 0;
            int right = mc.gameSettings.keyBindRight.isKeyDown() ? 1 : mc.gameSettings.keyBindLeft.isKeyDown() ? -1 : 0;
            int vertical = mc.gameSettings.keyBindJump.isKeyDown() ? 1 : mc.gameSettings.keyBindSneak.isKeyDown() ? -1 : 0;
            //resetKeys();

            if(mc.player.movementInput instanceof MovementInputFromOptions)
                resetKeys();

            motionX = 0;
            motionY = vertical * speed.getFloat();
            motionZ = 0;
            float yaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * mc.getRenderPartialTicks();
            float pitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * mc.getRenderPartialTicks();

            double radYaw = yaw * 0.0174533;

            if (forward != 0 || right != 0) {
                double cos = Math.cos(radYaw), sin = Math.sin(radYaw);

                motionX = (-sin*forward - cos*right )*speed.getFloat();
                motionZ = (cos*forward - sin*right) *speed.getFloat();
            }
            //cameraYaw += mc.gameSettings.keyBind
            move(MoverType.SELF, motionX, motionY, motionZ);
            inventory.copyInventory(mc.player.inventory);

        }
        public boolean isInvisible() {return true; }
        public boolean isSpectator() {return true; }
    }
}
