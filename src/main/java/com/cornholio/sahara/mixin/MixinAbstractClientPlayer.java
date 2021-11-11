package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer
{
    private ResourceLocation CornholioSkin = new ResourceLocation("minecraft","cornholio.png");

    @Inject(method = "getLocationSkin", at = @At("HEAD"), cancellable = true, remap = !SaharaClient.isDebug)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> info)
    {
        if(SaharaClient.getSahara().getModuleManager().cornholioModule.isActive())
        {
            info.setReturnValue(CornholioSkin);
        }
    }
}
