package com.tfctiab.mixin.accessor.firmalife;

import com.eerussianguy.firmalife.common.blockentities.FLBeehiveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FLBeehiveBlockEntity.class, remap = false)
public interface FLBeehiveBlockEntityAccessor
{
    @Accessor("lastAreaTick")
    long tfcTiab$getLastAreaTick();

    @Accessor("lastAreaTick")
    void tfcTiab$setLastAreaTick(long tick);
}
