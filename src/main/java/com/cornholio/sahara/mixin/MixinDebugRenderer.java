package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.render.WallhackModule;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(DebugRenderer.class)
public class MixinDebugRenderer {
    @Inject(method = "renderDebug", at = @At("RETURN"), cancellable = true, remap = !SaharaClient.isDebug)
    public void renderDebug(float partialTicks, long finishTimeNano, CallbackInfo info)
    {
        SaharaClient.getSahara().getModuleManager().wallhackModule.renderBoxes();
    }
}
