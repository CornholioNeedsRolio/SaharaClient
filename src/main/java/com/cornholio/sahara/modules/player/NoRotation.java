package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.events.PlayerUpdateEvent;
import com.cornholio.sahara.mixin.MixinTurnOverride;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.ModuleSetting;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class NoRotation extends Module
{
    public float yaw = 0;
    public float pitch = 0;
    public float prevyaw = 0;
    public float prevpitch = 0;


    ModuleSetting classic;
    public NoRotation()
    {
        super("NoRotation", "Cancels rotation packets", ModuleCategory.Player);
        registerSetting(classic = new ModuleSetting("Packet", false));
    }

    //@Override
    //public boolean onTurn(Entity entity, float yaw, float pitch, CallbackInfo ci)
    //{
    //    if(isActive() && entity == mc.player && !classic.getBoolean())
    //    {
    //        //doMagic();
    //        return true;
    //    }
    //    return false;
    //}

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        if(!classic.getBoolean() || mc.player==null) return;
        //does this even work? idk haven't tested it, this module might be broken
        if(event.getPacket() instanceof SPacketPlayerPosLook)
        {
            SPacketPlayerPosLook ev = (SPacketPlayerPosLook)event.getPacket();
            ev.equals(new SPacketPlayerPosLook(ev.getX(), ev.getY(), ev.getZ(), mc.player.rotationYaw, mc.player.rotationPitch, ev.getFlags(), ev.getTeleportId()));
            //ev.yaw = mc.player.rotationYaw;
            //ev.pitch = mc.player.rotationPitch;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.PlayerTickEvent event)
    {
        //if(!event.isPre()) return;
        updatePlayer();
    }

    public void updatePlayer()
    {
        if(mc.player == null) return;
        mc.player.rotationYaw = this.yaw;
        mc.player.rotationPitch = this.pitch;
        mc.player.prevRotationPitch = this.prevpitch;
        mc.player.prevRotationYaw = this.prevyaw;

    }

    private void doMagic()
    {
        if(classic.getBoolean() || mc.player == null || !mc.inGameHasFocus || SaharaClient.getSahara().getModuleManager().Freecam.isActive()) return;

        this.mc.mouseHelper.mouseXYChange();
        this.mc.getTutorial().handleMouse(this.mc.mouseHelper);
        float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F * 0.15F;
        float f2 = (float)this.mc.mouseHelper.deltaX * f1;
        float f3 = (float)this.mc.mouseHelper.deltaY * f1;

        //prevyaw = yaw;
        //prevpitch = pitch;

        float f4 = yaw;
        float f5 = pitch;
        yaw += (float)((double)f2);
        pitch -= (this.mc.gameSettings.invertMouse ? -f3 : f3);
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        prevyaw += yaw-f4;
        prevpitch += pitch-f5;

        if(yaw <= -180)
            yaw += 360.0F;
        if(yaw >= 180)
            yaw -= 360.0F;

        mc.player.prevRotationPitch = prevpitch;
        mc.player.prevRotationYaw = prevyaw;
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;

        if (mc.player.getRidingEntity() != null)
            mc.player.getRidingEntity().applyOrientationToEntity(mc.player);
    }
}
