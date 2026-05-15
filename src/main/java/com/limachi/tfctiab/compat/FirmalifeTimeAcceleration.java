package com.limachi.tfctiab.compat;

import com.eerussianguy.firmalife.common.blockentities.CompostTumblerBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBeehiveBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.GrapePlantBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.JarbnetBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.LargePlanterBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.SimpleItemRecipeBlockEntity;
import com.limachi.tfctiab.mixin.accessor.firmalife.CompostTumblerBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.FLBeehiveBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.GrapePlantBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.JarbnetBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.LargePlanterBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.firmalife.SimpleItemRecipeBlockEntityAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class FirmalifeTimeAcceleration
{
    private static final long LONG_UNINITIALIZED_TICK = Long.MIN_VALUE;

    public static boolean advanceTimestampState(BlockEntity blockEntity, long ticks)
    {
        boolean changed = false;
        if (blockEntity instanceof LargePlanterBlockEntity planter)
        {
            ((LargePlanterBlockEntityAccessor) planter).tfcTiab$setLastGrowthTick(TfcTimeAcceleration.subtractTimestamp(planter.getLastGrowthTick(), ticks));
            changed = true;
        }
        if (blockEntity instanceof GrapePlantBlockEntity grape)
        {
            ((GrapePlantBlockEntityAccessor) grape).tfcTiab$setLastGrowthTick(TfcTimeAcceleration.subtractTimestamp(grape.getLastGrowthTick(), ticks));
            changed = true;
        }
        if (blockEntity instanceof CompostTumblerBlockEntity tumbler)
        {
            final CompostTumblerBlockEntityAccessor accessor = (CompostTumblerBlockEntityAccessor) tumbler;
            final long lastUpdateTick = accessor.tfcTiab$getLastUpdateTick();
            if (lastUpdateTick != LONG_UNINITIALIZED_TICK)
            {
                accessor.tfcTiab$setLastUpdateTick(TfcTimeAcceleration.subtractTimestamp(lastUpdateTick, ticks));
                changed = true;
            }
        }
        if (blockEntity instanceof JarbnetBlockEntity jarbnet)
        {
            if (jarbnet.getLastUpdateTick() != LONG_UNINITIALIZED_TICK)
            {
                ((JarbnetBlockEntityAccessor) jarbnet).tfcTiab$setLastUpdateTick(TfcTimeAcceleration.subtractTimestamp(jarbnet.getLastUpdateTick(), ticks));
                changed = true;
            }
        }
        if (blockEntity instanceof SimpleItemRecipeBlockEntity<?> recipeBlock)
        {
            final SimpleItemRecipeBlockEntityAccessor accessor = (SimpleItemRecipeBlockEntityAccessor) recipeBlock;
            final long startTick = accessor.tfcTiab$getStartTick();
            if (startTick > 0)
            {
                accessor.tfcTiab$setStartTick(TfcTimeAcceleration.subtractTimestamp(startTick, ticks));
                changed = true;
            }
        }
        if (blockEntity instanceof FLBeehiveBlockEntity beehive)
        {
            final FLBeehiveBlockEntityAccessor accessor = (FLBeehiveBlockEntityAccessor) beehive;
            accessor.tfcTiab$setLastAreaTick(TfcTimeAcceleration.subtractTimestamp(accessor.tfcTiab$getLastAreaTick(), ticks));
            changed = true;
        }
        return changed;
    }

    private FirmalifeTimeAcceleration() {}
}
