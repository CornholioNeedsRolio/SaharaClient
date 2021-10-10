package com.cornholio.sahara.modules.packetevent;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

public class PacketListener extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Packet) {
            PacketEvent inPacket = new PacketEvent((Packet)msg);
            MinecraftForge.EVENT_BUS.post(inPacket);
            if(inPacket.isCanceled())
                return;
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof Packet) {
            PacketEvent outPacket = new PacketEvent((Packet)msg);

            MinecraftForge.EVENT_BUS.post(outPacket);
            if(outPacket.isCanceled())
                return;
        }
        super.write(ctx, msg, promise);
    }
}

