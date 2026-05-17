package com.tfctiab.compat;

import java.util.function.Consumer;
import com.tfctiab.config.TfcTiabConfig;
import com.tfctiab.mixin.accessor.tfc.BarrelBlockEntityAccessor;
import com.tfctiab.mixin.accessor.tfc.BloomeryBlockEntityAccessor;
import com.tfctiab.mixin.accessor.tfc.ComposterBlockEntityAccessor;
import com.tfctiab.mixin.accessor.tfc.CropBlockEntityAccessor;
import com.tfctiab.mixin.accessor.tfc.PitKilnBlockEntityAccessor;
import com.tfctiab.mixin.accessor.tfc.TickCounterBlockEntityAccessor;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.BerryBushBlockEntity;
import net.dries007.tfc.common.blockentities.BloomeryBlockEntity;
import net.dries007.tfc.common.blockentities.ComposterBlockEntity;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.devices.BloomeryBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public final class TfcTimeAcceleration
{
    private static final String TFC = "tfc";
    private static final String FIRMALIFE = "firmalife";
    private static final long UNINITIALIZED_TICK = Integer.MIN_VALUE;

    public static boolean shouldHandle(Level level, BlockPos pos)
    {
        if (pos == null || level == null || level.isClientSide)
        {
            return false;
        }
        final BlockState state = level.getBlockState(pos);
        return shouldHandle(state, level.getBlockEntity(pos));
    }

    public static void tick(Level level, BlockPos pos, int timeRate, Consumer<Entity.RemovalReason> remove)
    {
        final ServerLevel serverLevel = level instanceof ServerLevel server ? server : null;
        final BlockState state = level.getBlockState(pos);
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!shouldHandle(state, blockEntity))
        {
            if (blockEntity == null && !state.isRandomlyTicking())
            {
                remove.accept(Entity.RemovalReason.KILLED);
            }
            return;
        }

        final boolean changed = advanceTimestampState(blockEntity, state, timeRate);
        if (blockEntity != null && tickerFor(level, state, blockEntity) != null)
        {
            tickBlockEntity(level, pos, state, blockEntity);
        }

        runRandomTicks(serverLevel, level, pos, timeRate);
        if (changed)
        {
            syncTimestampState(level, pos);
        }
    }

    private static boolean shouldHandle(BlockState state, BlockEntity blockEntity)
    {
        return isTfcOrFirmalife(blockId(state)) || isTfcOrFirmalife(blockEntityTypeId(blockEntity));
    }

    private static boolean advanceTimestampState(BlockEntity blockEntity, BlockState state, long ticks)
    {
        boolean changed = false;
        if (blockEntity instanceof TickCounterBlockEntity counter)
        {
            if (counter.getLastUpdateTick() != UNINITIALIZED_TICK)
            {
                ((TickCounterBlockEntityAccessor) counter).tfcTiab$setLastUpdateTick(subtractTimestamp(counter.getLastUpdateTick(), ticks));
                changed = true;
            }
        }
        if (blockEntity instanceof CropBlockEntity crop)
        {
            ((CropBlockEntityAccessor) crop).tfcTiab$setLastGrowthTick(subtractTimestamp(crop.getLastGrowthTick(), ticks));
            changed = true;
        }
        if (blockEntity instanceof BerryBushBlockEntity bush)
        {
            bush.setLastBushTick(subtractTimestamp(Calendars.SERVER.getTicks() - bush.getTicksSinceBushUpdate(), ticks));
            changed = true;
        }
        if (blockEntity instanceof ComposterBlockEntity composter)
        {
            final ComposterBlockEntityAccessor accessor = (ComposterBlockEntityAccessor) composter;
            final long lastUpdateTick = accessor.tfcTiab$getLastUpdateTick();
            if (lastUpdateTick != UNINITIALIZED_TICK)
            {
                accessor.tfcTiab$setLastUpdateTick(subtractTimestamp(lastUpdateTick, ticks));
                changed = true;
            }
        }
        if (blockEntity instanceof BarrelBlockEntity barrel)
        {
            final BarrelBlockEntityAccessor accessor = (BarrelBlockEntityAccessor) barrel;
            if (barrel.getRecipeTick() > 0)
            {
                accessor.tfcTiab$setRecipeTick(subtractTimestamp(barrel.getRecipeTick(), ticks));
                changed = true;
            }
            if (barrel.getSealedTick() > 0)
            {
                accessor.tfcTiab$setSealedTick(subtractTimestamp(barrel.getSealedTick(), ticks));
                changed = true;
            }
        }
        if (blockEntity instanceof BloomeryBlockEntity bloomery && state.getBlock() instanceof BloomeryBlock && state.getValue(BloomeryBlock.LIT))
        {
            final BloomeryBlockEntityAccessor accessor = (BloomeryBlockEntityAccessor) bloomery;
            accessor.tfcTiab$setLitTick(subtractTimestamp(accessor.tfcTiab$getLitTick(), ticks));
            changed = true;
        }
        if (blockEntity instanceof net.dries007.tfc.common.blockentities.PitKilnBlockEntity pitKiln && pitKiln.isLit())
        {
            ((PitKilnBlockEntityAccessor) pitKiln).tfcTiab$setLitTick(subtractTimestamp(pitKiln.getLitTick(), ticks));
            changed = true;
        }
        if (ModList.get().isLoaded(FIRMALIFE))
        {
            changed |= FirmalifeTimeAcceleration.advanceTimestampState(blockEntity, ticks);
        }
        return changed;
    }

    private static void syncTimestampState(Level level, BlockPos pos)
    {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TFCBlockEntity tfcBlockEntity)
        {
            tfcBlockEntity.markForSync();
        }
        else if (blockEntity != null)
        {
            blockEntity.setChanged();
            final BlockState state = level.getBlockState(pos);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void tickBlockEntity(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity)
    {
        alignCalendarTick(blockEntity);
        final BlockEntityTicker<BlockEntity> ticker = tickerFor(level, state, blockEntity);
        if (ticker != null)
        {
            ticker.tick(level, pos, state, blockEntity);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static BlockEntityTicker<BlockEntity> tickerFor(Level level, BlockState state, BlockEntity blockEntity)
    {
        return state.getTicker(level, (BlockEntityType<BlockEntity>) blockEntity.getType());
    }

    private static void runRandomTicks(ServerLevel serverLevel, Level level, BlockPos pos, int attempts)
    {
        if (serverLevel == null || attempts <= 0)
        {
            return;
        }
        final int interval = randomTickInterval();
        int remainingAttempts = attempts;
        while (remainingAttempts > 0)
        {
            final BlockState state = level.getBlockState(pos);
            if (!shouldHandle(state, level.getBlockEntity(pos)) || !state.isRandomlyTicking())
            {
                return;
            }
            final int skippedFailures = randomTickFailuresBeforeSuccess(level, interval);
            if (skippedFailures >= remainingAttempts)
            {
                return;
            }
            remainingAttempts -= skippedFailures + 1;

            final BlockState randomTickState = level.getBlockState(pos);
            if (shouldHandle(randomTickState, level.getBlockEntity(pos)) && randomTickState.isRandomlyTicking())
            {
                randomTickState.randomTick(serverLevel, pos, level.random);
            }
        }
    }

    private static int randomTickFailuresBeforeSuccess(Level level, int interval)
    {
        if (interval <= 1)
        {
            return 0;
        }
        // Match repeated nextInt(interval) == 0 checks while skipping failed attempts.
        return (int) Math.floor(Math.log1p(-level.random.nextDouble()) / Math.log1p(-1.0D / interval));
    }

    private static void alignCalendarTick(BlockEntity blockEntity)
    {
        if (blockEntity instanceof ICalendarTickable calendarTickable)
        {
            final long currentTick = Calendars.SERVER.getTicks();
            final long lastTick = calendarTickable.getLastCalendarUpdateTick();
            if (lastTick == currentTick)
            {
                calendarTickable.setLastCalendarUpdateTick(currentTick - 1);
            }
        }
    }

    private static int randomTickInterval()
    {
        return Math.max(1, TfcTiabConfig.SERVER.averageRandomTickInterval.get());
    }

    public static long subtractTimestamp(long tick, long ticks)
    {
        if (ticks <= 0)
        {
            return tick;
        }
        final long result = tick - ticks;
        return result > tick ? Long.MIN_VALUE : result;
    }

    private static boolean isTfcOrFirmalife(ResourceLocation id)
    {
        if (id == null)
        {
            return false;
        }
        final String namespace = id.getNamespace();
        return TFC.equals(namespace) || FIRMALIFE.equals(namespace);
    }

    private static ResourceLocation blockId(BlockState state)
    {
        return ForgeRegistries.BLOCKS.getKey(state.getBlock());
    }

    private static ResourceLocation blockEntityTypeId(BlockEntity blockEntity)
    {
        return blockEntity == null ? null : ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(blockEntity.getType());
    }

    private TfcTimeAcceleration() {}
}
