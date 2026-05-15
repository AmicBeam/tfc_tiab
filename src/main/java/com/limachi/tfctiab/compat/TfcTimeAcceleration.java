package com.limachi.tfctiab.compat;

import java.util.function.Consumer;
import com.limachi.tfctiab.config.TfcTiabConfig;
import com.limachi.tfctiab.mixin.accessor.tfc.BarrelBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.tfc.BloomeryBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.tfc.ComposterBlockEntityAccessor;
import com.limachi.tfctiab.mixin.accessor.tfc.PitKilnBlockEntityAccessor;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.BerryBushBlockEntity;
import net.dries007.tfc.common.blockentities.BloomeryBlockEntity;
import net.dries007.tfc.common.blockentities.ComposterBlockEntity;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.devices.BloomeryBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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
        for (int i = 0; i < timeRate; i++)
        {
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

            advanceTimestampState(blockEntity, state, 1);

            if (blockEntity != null)
            {
                tickBlockEntity(level, pos, state, blockEntity);
            }

            final BlockState randomTickState = level.getBlockState(pos);
            if (serverLevel != null && randomTickState.isRandomlyTicking() && level.random.nextInt(randomTickInterval()) == 0)
            {
                randomTickState.randomTick(serverLevel, pos, level.random);
            }
        }
    }

    private static boolean shouldHandle(BlockState state, BlockEntity blockEntity)
    {
        return isTfcOrFirmalife(blockId(state)) || isTfcOrFirmalife(blockEntityTypeId(blockEntity));
    }

    private static void advanceTimestampState(BlockEntity blockEntity, BlockState state, long ticks)
    {
        if (blockEntity instanceof TickCounterBlockEntity counter)
        {
            if (counter.getLastUpdateTick() != UNINITIALIZED_TICK)
            {
                counter.reduceCounter(-ticks);
            }
        }
        if (blockEntity instanceof CropBlockEntity crop)
        {
            crop.setLastGrowthTick(crop.getLastGrowthTick() - ticks);
        }
        if (blockEntity instanceof BerryBushBlockEntity bush)
        {
            bush.setLastBushTick(Calendars.SERVER.getTicks() - bush.getTicksSinceBushUpdate() - ticks);
            bush.setChanged();
        }
        if (blockEntity instanceof ComposterBlockEntity composter)
        {
            final ComposterBlockEntityAccessor accessor = (ComposterBlockEntityAccessor) composter;
            final long lastUpdateTick = accessor.tfcTiab$getLastUpdateTick();
            if (lastUpdateTick != UNINITIALIZED_TICK)
            {
                accessor.tfcTiab$setLastUpdateTick(lastUpdateTick - ticks);
                composter.setChanged();
            }
        }
        if (blockEntity instanceof BarrelBlockEntity barrel)
        {
            final BarrelBlockEntityAccessor accessor = (BarrelBlockEntityAccessor) barrel;
            if (barrel.getRecipeTick() > 0)
            {
                accessor.tfcTiab$setRecipeTick(barrel.getRecipeTick() - ticks);
            }
            if (barrel.getSealedTick() > 0)
            {
                accessor.tfcTiab$setSealedTick(barrel.getSealedTick() - ticks);
            }
            barrel.setChanged();
        }
        if (blockEntity instanceof BloomeryBlockEntity bloomery && state.getBlock() instanceof BloomeryBlock && state.getValue(BloomeryBlock.LIT))
        {
            final BloomeryBlockEntityAccessor accessor = (BloomeryBlockEntityAccessor) bloomery;
            accessor.tfcTiab$setLitTick(accessor.tfcTiab$getLitTick() - ticks);
            bloomery.setChanged();
        }
        if (blockEntity instanceof net.dries007.tfc.common.blockentities.PitKilnBlockEntity pitKiln && pitKiln.isLit())
        {
            ((PitKilnBlockEntityAccessor) pitKiln).tfcTiab$setLitTick(pitKiln.getLitTick() - ticks);
            pitKiln.setChanged();
        }
        if (ModList.get().isLoaded(FIRMALIFE))
        {
            FirmalifeTimeAcceleration.advanceTimestampState(blockEntity, ticks);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void tickBlockEntity(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity)
    {
        alignCalendarTick(blockEntity);
        final BlockEntityTicker<BlockEntity> ticker = state.getTicker(level, (BlockEntityType<BlockEntity>) blockEntity.getType());
        if (ticker != null)
        {
            ticker.tick(level, pos, state, blockEntity);
        }
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
