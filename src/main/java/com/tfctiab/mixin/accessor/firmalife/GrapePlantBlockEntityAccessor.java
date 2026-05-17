package com.tfctiab.mixin.accessor.firmalife;

import com.eerussianguy.firmalife.common.blockentities.GrapePlantBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GrapePlantBlockEntity.class, remap = false)
public interface GrapePlantBlockEntityAccessor
{
    @Accessor("lastGrowthTick")
    void tfcTiab$setLastGrowthTick(long tick);
}
