package com.limachi.tfctiab.mixin.accessor.tfc;

import net.dries007.tfc.common.blockentities.PitKilnBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = PitKilnBlockEntity.class, remap = false)
public interface PitKilnBlockEntityAccessor
{
    @Accessor("litTick")
    void tfcTiab$setLitTick(long tick);
}
