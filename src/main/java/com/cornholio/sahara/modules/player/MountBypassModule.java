package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MountBypassModule extends Module {
    public MountBypassModule()
    {
        super("Mount Bypass", "Does some packet magic :P", ModuleCategory.Player);
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {
        if(mc.world == null) return;

        if(event.getPacket() instanceof CPacketUseEntity)
        {
            CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if((packet.getEntityFromWorld(mc.world) instanceof AbstractHorse) && packet.getAction() == CPacketUseEntity.Action.INTERACT_AT)
                event.setCanceled(true);
        }
    }
}
