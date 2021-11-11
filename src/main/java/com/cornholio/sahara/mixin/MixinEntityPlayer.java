package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.events.PlayerUpdateEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer
{
    @Inject(method = "jump()V", at = @At(value="HEAD"), cancellable = true, remap = !SaharaClient.isDebug)
    public void jump(CallbackInfo info)
    {
        if(SaharaClient.getSahara().getModuleManager().Freecam.isActive())
            info.cancel();
    }

    @Inject(method="onUpdate", at=@At("HEAD"), remap = !SaharaClient.isDebug)
    public void onPreUpdate(CallbackInfo info)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerUpdateEvent(true));
    }

    @Inject(method="onUpdate", at=@At("RETURN"), remap = !SaharaClient.isDebug)
    public void onPostUpdate(CallbackInfo info)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerUpdateEvent(false));
    }
}
