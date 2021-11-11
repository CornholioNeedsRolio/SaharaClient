package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerAbilities;

public class AntiHungerModule extends Module {
    public AntiHungerModule()
    {
        super("Anti Hunger", "Prevents hunger drainage", ModuleCategory.Player);
    }

    public void onPacketEvent(PacketEvent event)
    {
        if(event.getPacket() instanceof CPacketEntityAction)
        {
            CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
            if(packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING)
                event.setCanceled(true);
        }
    }
}
