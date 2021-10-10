package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer
{
    @Inject(method = "jump()V", at = @At(value="HEAD"), cancellable = true)
    public void jump(CallbackInfo info)
    {
        if(SaharaClient.getSahara().getModuleManager().Freecam.isActive())
            info.cancel();
    }

}
