package com.tfctiab.mixin.accessor.tfc;

import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BarrelBlockEntity.class, remap = false)
public interface BarrelBlockEntityAccessor
{
    @Accessor("sealedTick")
    void tfcTiab$setSealedTick(long tick);

    @Accessor("recipeTick")
    void tfcTiab$setRecipeTick(long tick);
}
