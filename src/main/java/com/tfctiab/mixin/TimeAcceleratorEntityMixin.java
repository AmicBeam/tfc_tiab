package com.tfctiab.mixin;

import com.tfctiab.compat.TfcTimeAcceleration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TimeAcceleratorEntity.class, remap = false)
public abstract class TimeAcceleratorEntityMixin extends Entity
{
    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract int getTimeRate();

    @Shadow
    public abstract int getRemainingTime();

    @Shadow
    public abstract void setRemainingTime(int remainingTime);

    protected TimeAcceleratorEntityMixin(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Inject(
        method = "tick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"),
        cancellable = true
    )
    private void tfcTiab$tickTimestampAwareTargets(CallbackInfo ci)
    {
        // TIAB has already checked the side, target position, and blacklist.
        final Level level = level();
        final BlockPos pos = getBlockPos();
        if (!TfcTimeAcceleration.shouldHandle(level, pos))
        {
            return;
        }

        TfcTimeAcceleration.tick(level, pos, getTimeRate(), this::remove);

        setRemainingTime(getRemainingTime() - 1);
        if (getRemainingTime() <= 0)
        {
            remove(RemovalReason.KILLED);
        }
        ci.cancel();
    }
}
