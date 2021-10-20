package com.cornholio.sahara.modules.player;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BoatPlaceConst extends Module {
    public BoatPlaceConst() {
        super("Boat Place", "Bypass for noboat place", ModuleCategory.Player);
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent ev)
    {
        if((ev.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) && mc.player.getHeldItemMainhand().getItem() instanceof ItemBoat)
            ev.setCanceled(true);
    }
}
