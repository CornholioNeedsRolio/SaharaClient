package com.cornholio.sahara.mixin;

import com.cornholio.sahara.SaharaClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid
{
    @Inject(method = "canCollideCheck(Lnet/minecraft/block/state/IBlockState;Z)Z", at = @At(value="RETURN"), cancellable = true, remap = !SaharaClient.isDebug)
    public void canCollideCheck(IBlockState state, boolean hitIfLiquid, CallbackInfoReturnable<Boolean> info)
    {
        info.setReturnValue(SaharaClient.getSahara().getModuleManager().jesusModule.canCollide(info.getReturnValue()));
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("RETURN"), cancellable = true, remap = !SaharaClient.isDebug)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, final CallbackInfoReturnable<AxisAlignedBB> info)
    {
        info.setReturnValue(SaharaClient.getSahara().getModuleManager().jesusModule.getCollisionBoundingBox(info.getReturnValue()));
    }
}
