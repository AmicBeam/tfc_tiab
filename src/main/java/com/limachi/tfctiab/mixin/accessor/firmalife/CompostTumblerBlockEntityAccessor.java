package com.limachi.tfctiab.mixin.accessor.firmalife;

import com.eerussianguy.firmalife.common.blockentities.CompostTumblerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CompostTumblerBlockEntity.class, remap = false)
public interface CompostTumblerBlockEntityAccessor
{
    @Accessor("lastUpdateTick")
    long tfcTiab$getLastUpdateTick();

    @Accessor("lastUpdateTick")
    void tfcTiab$setLastUpdateTick(long tick);
}
