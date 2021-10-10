package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.network.handshake.client.C00Handshake;

public class StrangeModule extends Module
{
    public StrangeModule()
    {
        super("StrangeModule", "Shit", ModuleCategory.Player);
    }
    public void onPacketEvent(PacketEvent event)
    {
        if(mc.player == null) return;
        if(event.getPacket() instanceof C00Handshake)
        {
            mc.player.sendChatMessage("helo");
        }
    }
}
