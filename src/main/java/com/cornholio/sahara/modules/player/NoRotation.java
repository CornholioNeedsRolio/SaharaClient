package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.mixin.MixinTurnOverride;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRotation extends Module
{
    static public float yaw = 0;
    static public float pitch = 0;
    public NoRotation()
    {
        super("NoRotation", "Cancels rotation packets", ModuleCategory.Player);
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        //does this even work? idk haven't tested it, this module might be broken
        if(event.getPacket() instanceof SPacketPlayerPosLook)
        {
            SPacketPlayerPosLook ev = (SPacketPlayerPosLook)event.getPacket();
            ev.equals(new SPacketPlayerPosLook(ev.getX(), ev.getY(), ev.getZ(), mc.player.rotationYaw, mc.player.rotationPitch, ev.getFlags(), ev.getTeleportId()));
            //ev.yaw = mc.player.rotationYaw;
            //ev.pitch = mc.player.rotationPitch;
        }
    }

    //@SubscribeEvent
    //public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    //{
    //    mc.player.rotationYaw = yaw;
    //    mc.player.rotationPitch = pitch;
    //}
}
