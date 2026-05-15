package com.limachi.tfctiab.mixin.accessor.tfc;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = TickCounterBlockEntity.class, remap = false)
public interface TickCounterBlockEntityAccessor
{
    @Accessor("lastUpdateTick")
    void tfcTiab$setLastUpdateTick(long tick);
}
