package com.limachi.tfctiab.mixin;

import com.limachi.tfctiab.compat.TfcTimeAcceleration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.haoict.tiab.common.entities.TimeAcceleratorEntity", remap = false)
public abstract class TimeAcceleratorEntityMixin extends Entity
{
    @Shadow(remap = false)
    private BlockPos pos;

    @Shadow(remap = false)
    public abstract int getTimeRate();

    @Shadow(remap = false)
    public abstract int getRemainingTime();

    @Shadow(remap = false)
    public abstract void setRemainingTime(int remainingTime);

    protected TimeAcceleratorEntityMixin(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Inject(method = {"tick", "m_8119_"}, at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void tfcTiab$tickTimestampAwareTargets(CallbackInfo ci)
    {
        final Level level = level();
        if (level.isClientSide || !TfcTimeAcceleration.shouldHandle(level, pos))
        {
            return;
        }

        super.tick();

        if (pos == null)
        {
            remove(RemovalReason.KILLED);
            ci.cancel();
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
