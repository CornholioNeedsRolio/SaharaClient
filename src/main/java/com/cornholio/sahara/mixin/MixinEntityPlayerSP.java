package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer
{
    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "isCurrentViewEntity", at = @At("RETURN"), cancellable = true)
    protected void isCurrentViewEntity(CallbackInfoReturnable<Boolean> info)
    {
        info.setReturnValue(info.getReturnValueZ() || SaharaClient.getSahara().getModuleManager().Freecam.isActive());
    }

    @Inject(method = "startRiding", at = @At("HEAD"), cancellable = true)
    public void startRiding(Entity entityIn, boolean force, CallbackInfoReturnable<Boolean> info)
    {
        if (!super.startRiding(entityIn, force))
        {
            info.setReturnValue(false);

        }
        else
        {
            if (entityIn instanceof EntityMinecart)
            {
                Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
            }

            if (entityIn instanceof EntityBoat && !SaharaClient.getSahara().getModuleManager().boatFly.getFixYaw())
            {
                this.prevRotationYaw = entityIn.rotationYaw;
                this.rotationYaw = entityIn.rotationYaw;
                this.setRotationYawHead(entityIn.rotationYaw);
            }

            info.setReturnValue(true);
        }
    }
}
