package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class NoRotation extends Module
{
    private EntityOtherPlayerMP FakePlayer;

    ModuleSetting classic;
    public NoRotation()
    {
        super("NoRotation", "Cancels rotation packets", ModuleCategory.Player);
        registerSetting(classic = new ModuleSetting("Packet", false));
    }

    @Override
    public boolean onTurn(Entity entity, float yaw, float pitch, CallbackInfo ci)
    {
        if(!isActive() || mc.player != entity) return false;
        //if(FakePlayer != null)
        //    FakePlayer.turn(yaw, pitch);
        return true;
    }

    @Override
    public void onEnable()
    {
        super.onEnable();
        if(mc.player == null || mc.world == null) return;
        FakePlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
        FakePlayer.isInvisibleToPlayer(mc.player);
        FakePlayer.copyLocationAndAnglesFrom(mc.player);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if(event.getEntity() != mc.player || FakePlayer == null) return;
        mc.player.cameraYaw = FakePlayer.rotationYaw;
        mc.player.cameraPitch = FakePlayer.rotationPitch;
        mc.player.prevCameraPitch = FakePlayer.prevCameraPitch;
        mc.player.prevCameraYaw = FakePlayer.prevCameraYaw;

    }
}
