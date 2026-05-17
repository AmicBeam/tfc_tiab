package com.tfctiab.mixin.accessor.tfc;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CropBlockEntity.class, remap = false)
public interface CropBlockEntityAccessor
{
    @Accessor("lastGrowthTick")
    void tfcTiab$setLastGrowthTick(long tick);
}
