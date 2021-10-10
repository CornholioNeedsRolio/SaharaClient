package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP
{
    @Inject(method = "isCurrentViewEntity", at = @At("RETURN"), cancellable = true)
    protected void isCurrentViewEntity(CallbackInfoReturnable<Boolean> info)
    {
        info.setReturnValue(info.getReturnValueZ() || SaharaClient.getSahara().getModuleManager().Freecam.isActive());
    }
}
