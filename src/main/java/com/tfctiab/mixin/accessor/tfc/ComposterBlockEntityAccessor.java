package com.tfctiab.mixin.accessor.tfc;

import net.dries007.tfc.common.blockentities.ComposterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ComposterBlockEntity.class, remap = false)
public interface ComposterBlockEntityAccessor
{
    @Accessor("lastUpdateTick")
    long tfcTiab$getLastUpdateTick();

    @Accessor("lastUpdateTick")
    void tfcTiab$setLastUpdateTick(long tick);
}
