package com.cornholio.sahara.modules.combat;

import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.packetevent.ModuleCategory;
import com.cornholio.sahara.modules.packetevent.PacketEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CriticalsModule extends Module {
    public CriticalsModule()
    {
        super("Critics", "Crits entities", ModuleCategory.Combat);
    }

    @SubscribeEvent
    public void onPacketEvent(PacketEvent event)
    {

        if(mc.player == null || mc.world == null || !mc.player.onGround) return;
        if(event.getPacket() instanceof CPacketUseEntity)
        {
            CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if(packet.getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase) {

                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
            }
        }
    }
}
