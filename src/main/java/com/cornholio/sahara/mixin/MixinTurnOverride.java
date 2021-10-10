package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.player.NoRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, priority = Integer.MAX_VALUE)
public class MixinTurnOverride
{
    @Inject(method = "turn(FF)V", at = @At(value="HEAD"), cancellable = true)
    public void turn(float yaw, float pitch, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if(entity == Minecraft.getMinecraft().player)
        {
            NoRotation.yaw += yaw;
            NoRotation.pitch += pitch;
        }

        boolean value = false;
        for(Module m : SaharaClient.getSahara().getModuleManager().modules)
            if(m.onTurn((Entity)(Object)this, yaw, pitch, ci))
                value = true;

        if(value)
            ci.cancel();
    }
}
