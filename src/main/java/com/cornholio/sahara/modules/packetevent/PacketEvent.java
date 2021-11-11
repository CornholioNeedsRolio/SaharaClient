package com.cornholio.sahara.modules.packetevent;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvent extends Event
{
    private Packet packet;
    private boolean canceled;
    public EventType type;

    public PacketEvent(Packet packet, EventType type)
    {
        super();
        this.packet = packet;
        this.type = type;
    }

    public Packet getPacket()
    {
        return packet;
    }

    @Override
    public boolean isCanceled()
    {
        return canceled;
    }

    @Override
    public void setCanceled(boolean value)
    {
        canceled = value;
    }
}
