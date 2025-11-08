package com.mochi_753.mega_chorus.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void onRandomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        int age = pState.getValue(ChorusFlowerBlock.AGE);
        if (age >= 5) {
            pLevel.setBlock(pPos, Blocks.CHORUS_FLOWER.defaultBlockState().setValue(ChorusFlowerBlock.AGE, 0), 3);
        }
    }

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void alwaysTick(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "placeDeadFlower", at = @At("HEAD"), cancellable = true)
    private void preventPlaceDeadFlower(Level pLevel, BlockPos pPos, CallbackInfo ci) {
        ci.cancel();
        BlockState state = pLevel.getBlockState(pPos).setValue(ChorusFlowerBlock.AGE, 0);
        pLevel.setBlock(pPos, state, 3);
    }
}
