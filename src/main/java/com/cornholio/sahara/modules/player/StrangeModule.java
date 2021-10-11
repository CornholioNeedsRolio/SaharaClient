package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StrangeModule extends Module
{
    public StrangeModule()
    {
        super("StrangeModule", "Kicks you off the server might be used for a potential dupe", ModuleCategory.Player);
    }
    //public void onPacketEvent(PacketEvent event)
    //{
    //    if(mc.player == null) return;
    //    if(event.getPacket() instanceof C00Handshake)
    //    {
    //        mc.player.sendChatMessage("helo");
    //    }
    //}

    @Override
    public void onEnable()
    {
        super.onEnable();
        if(mc.player == null) return;
        mc.player.connection.sendPacket(new CPacketUseEntity(mc.player, EnumHand.OFF_HAND));
        onToggle(false);
    }
}
