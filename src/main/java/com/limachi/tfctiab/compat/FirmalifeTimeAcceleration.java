package com.limachi.tfctiab.compat;

import com.eerussianguy.firmalife.common.blockentities.CompostTumblerBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBeehiveBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.GrapePlantBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.JarbnetBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.LargePlanterBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.SimpleItemRecipeBlockEntity;
import com.limachi.tfctiab.mixin.accessor.firmalife.CompostTumblerBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.FLBeehiveBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.SimpleItemRecipeBlockEntityAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class FirmalifeTimeAcceleration
{
    private static final long LONG_UNINITIALIZED_TICK = Long.MIN_VALUE;

    public static void advanceTimestampState(BlockEntity blockEntity, long ticks)
    {
        if (blockEntity instanceof LargePlanterBlockEntity planter)
        {
            planter.setLastGrowthTick(planter.getLastGrowthTick() - ticks);
        }
        if (blockEntity instanceof GrapePlantBlockEntity grape)
        {
            grape.setLastGrowthTick(grape.getLastGrowthTick() - ticks);
        }
        if (blockEntity instanceof CompostTumblerBlockEntity tumbler)
        {
            final CompostTumblerBlockEntityAccessor accessor = (CompostTumblerBlockEntityAccessor) tumbler;
            accessor.tfcTiab$setLastUpdateTick(accessor.tfcTiab$getLastUpdateTick() - ticks);
            tumbler.setChanged();
        }
        if (blockEntity instanceof JarbnetBlockEntity jarbnet)
        {
            if (jarbnet.getLastUpdateTick() != LONG_UNINITIALIZED_TICK)
            {
                jarbnet.setLastUpdateTick(jarbnet.getLastUpdateTick() - ticks);
            }
        }
        if (blockEntity instanceof SimpleItemRecipeBlockEntity<?> recipeBlock)
        {
            final SimpleItemRecipeBlockEntityAccessor accessor = (SimpleItemRecipeBlockEntityAccessor) recipeBlock;
            final long startTick = accessor.tfcTiab$getStartTick();
            if (startTick > 0)
            {
                accessor.tfcTiab$setStartTick(startTick - ticks);
                recipeBlock.setChanged();
            }
        }
        if (blockEntity instanceof FLBeehiveBlockEntity beehive)
        {
            final FLBeehiveBlockEntityAccessor accessor = (FLBeehiveBlockEntityAccessor) beehive;
            accessor.tfcTiab$setLastAreaTick(accessor.tfcTiab$getLastAreaTick() - ticks);
            beehive.setChanged();
        }
    }

    private FirmalifeTimeAcceleration() {}
}
