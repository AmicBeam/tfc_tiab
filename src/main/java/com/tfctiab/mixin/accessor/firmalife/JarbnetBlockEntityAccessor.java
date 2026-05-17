package com.tfctiab.mixin.accessor.firmalife;

import com.eerussianguy.firmalife.common.blockentities.JarbnetBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = JarbnetBlockEntity.class, remap = false)
public interface JarbnetBlockEntityAccessor
{
    @Accessor("lastUpdateTick")
    void tfcTiab$setLastUpdateTick(long tick);
}
