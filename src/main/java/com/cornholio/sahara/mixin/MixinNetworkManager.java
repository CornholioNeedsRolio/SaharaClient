package com.cornholio.sahara.mixin;

import com.cornholio.sahara.modules.packetevent.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager
{
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At(value="HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> PacketOut, CallbackInfo callbackInfo)
    {
        PacketEvent packetout = new PacketEvent(PacketOut);
        MinecraftForge.EVENT_BUS.post(packetout);
        if (packetout.isCanceled())
            callbackInfo.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At(value = "HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> PacketIn, CallbackInfo callbackInfo)
    {
        PacketEvent packetin = new PacketEvent(PacketIn);
        MinecraftForge.EVENT_BUS.post(packetin);
        if (packetin.isCanceled())
            callbackInfo.cancel();
    }
}
