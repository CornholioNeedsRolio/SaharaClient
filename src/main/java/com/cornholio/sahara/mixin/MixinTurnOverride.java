package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.player.NoRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
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
        //Minecraft mc = Minecraft.getMinecraft();
        //if(entity == mc.player)
        //{
        //    float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        //    float f1 = f * f * f * 8.0F;
        //    float f2 = (float)mc.mouseHelper.deltaX * f1;
        //    float f3 = (float)mc.mouseHelper.deltaY * f1;
        //    NoRotation.yaw += f2;
        //    NoRotation.pitch += f3;
        //    NoRotation.yaw = (float)((double)NoRotation.yaw + (double)yaw * 0.15D);
        //    NoRotation.pitch  = (float)((double)NoRotation.pitch - (double)pitch * 0.15D);
        //    NoRotation.pitch  = MathHelper.clamp(NoRotation.pitch , -90.0F, 90.0F);
//
        //    NoRotation.prevpitch += NoRotation.yaw - f;
        //    NoRotation.prevyaw += NoRotation.pitch - f1;
        //}

        boolean value = false;
        for(Module m : SaharaClient.getSahara().getModuleManager().modules)
            if(m.onTurn((Entity)(Object)this, yaw, pitch, ci))
                value = true;

        if(value)
            ci.cancel();
    }
}
