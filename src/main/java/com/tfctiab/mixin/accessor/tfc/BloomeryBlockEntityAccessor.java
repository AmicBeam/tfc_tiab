package com.tfctiab.mixin.accessor.tfc;

import net.dries007.tfc.common.blockentities.BloomeryBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BloomeryBlockEntity.class, remap = false)
public interface BloomeryBlockEntityAccessor
{
    @Accessor("litTick")
    long tfcTiab$getLitTick();

    @Accessor("litTick")
    void tfcTiab$setLitTick(long tick);
}
