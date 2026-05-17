package com.tfctiab.mixin.accessor.firmalife;

import com.eerussianguy.firmalife.common.blockentities.LargePlanterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = LargePlanterBlockEntity.class, remap = false)
public interface LargePlanterBlockEntityAccessor
{
    @Accessor("lastGrowthTick")
    void tfcTiab$setLastGrowthTick(long tick);
}
