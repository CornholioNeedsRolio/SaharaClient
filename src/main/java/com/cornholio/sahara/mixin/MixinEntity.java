package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.cornholio.sahara.modules.Module;
import com.cornholio.sahara.modules.player.NoRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = Integer.MAX_VALUE)
public class MixinEntity
{
    @Inject(method = "turn(FF)V", at = @At(value="HEAD"), cancellable = true, remap = !SaharaClient.isDebug)
    public void turn(float yaw, float pitch, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;

        boolean value = false;
        for(Module m : SaharaClient.getSahara().getModuleManager().modules)
            if(m.onTurn((Entity)(Object)this, yaw, pitch, ci))
                value = true;

        if(!value)
        {
            float f = entity.rotationPitch;
            float f1 = entity.rotationYaw;
            entity.rotationYaw = (float)((double)entity.rotationYaw + (double)yaw * 0.15D);
            entity.rotationPitch = (float)((double)entity.rotationPitch - (double)pitch * 0.15D);
            entity.rotationPitch = MathHelper.clamp(entity.rotationPitch, -90.0F, 90.0F);
            entity.prevRotationPitch += entity.rotationPitch - f;
            entity.prevRotationYaw += entity.rotationYaw - f1;

            //Minecraft mc = Minecraft.getMinecraft();
            //if(entity==mc.player) return;
            //{
            //    NoRotation rotation = SaharaClient.getSahara().getModuleManager().noRotation;
            //    rotation.yaw = mc.player.rotationYaw;
            //    rotation.pitch = mc.player.rotationPitch;
            //    rotation.prevpitch = mc.player.prevRotationPitch;
            //    rotation.prevyaw = mc.player.prevRotationYaw;
            //}

            if (entity.getRidingEntity() != null)
            {
                entity.getRidingEntity().applyOrientationToEntity(entity);
            }
        }
        ci.cancel();
    }

    //@Inject(method = "isEntityInsideOpaqueBlock", at = @At(value="HEAD"), cancellable = true)
    //public void isEntityInsideOpaqueBlock(CallbackInfoReturnable<Boolean> info)
    //{
    //    Entity entity = (Entity)(Object)this;
    //    SaharaClient sh = SaharaClient.getSahara();
    //    if(sh.getModuleManager().viewClipModule.isActive() && entity == sh.getModuleManager().Freecam.getRenderViewEntity()) {
    //        info.setReturnValue(false);
    //    }
    //}

    //@Inject(method = "turn(FF)V", at = @At(value="RETURN"))
    //public void postTurn(float yaw, float pitch, CallbackInfo ci) {
    //    Entity entity = (Entity)(Object)this;
    //    Minecraft mc = Minecraft.getMinecraft();
    //    if(entity!=mc.player) return;
//
    //    NoRotation rotation = SaharaClient.getSahara().getModuleManager().noRotation;
    //    rotation.yaw = mc.player.rotationYaw;
    //    rotation.pitch = mc.player.rotationPitch;
    //    rotation.prevpitch = mc.player.prevRotationPitch;
    //    rotation.prevyaw = mc.player.prevRotationYaw;
    //}
}
