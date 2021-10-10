package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer extends ModelBiped
{
    @Inject(method = "setRotationAngles", at = @At(value="RETURN"))
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo info)
    {
        if(SaharaClient.getSahara().getModuleManager().cornholioModule.isActive()) {
            this.bipedRightArm.rotateAngleX += (float) Math.PI;
            this.bipedLeftArm.rotateAngleX += (float) Math.PI;
        }
    }
}
