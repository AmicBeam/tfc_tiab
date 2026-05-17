package com.tfctiab.mixin.accessor.firmalife;

import com.eerussianguy.firmalife.common.blockentities.SimpleItemRecipeBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SimpleItemRecipeBlockEntity.class, remap = false)
public interface SimpleItemRecipeBlockEntityAccessor
{
    @Accessor("startTick")
    long tfcTiab$getStartTick();

    @Accessor("startTick")
    void tfcTiab$setStartTick(long tick);
}
