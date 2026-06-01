package com.terrainslabs2;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * 6면 중 위/옆 = grass_block_top (snowy 시 grass_block_snow), 아래 = dirt.
 * 위 블록이 terrainslabs:grass_slab 이고 snowy=true 이면 자신도 snowy.
 * random tick: 직상방(y+1)에 grass_slab 없으면 → 일반 잔디 블록으로 변환.
 */
public class GrassBlock2 extends SnowyDirtBlock {

    private static Block grassSlabCache = null;

    private static Block getGrassSlab() {
        if (grassSlabCache == null) {
            grassSlabCache = BuiltInRegistries.BLOCK.getValue(
                Identifier.fromNamespaceAndPath("terrainslabs", "grass_slab")
            );
        }
        return grassSlabCache;
    }

    public GrassBlock2(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(
        BlockState state,
        LevelReader level,
        ScheduledTickAccess tickAccess,
        BlockPos pos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        RandomSource random
    ) {
        if (direction == Direction.UP) {
            Block grassSlab = getGrassSlab();
            boolean snowy =
                grassSlab != null &&
                grassSlab != Blocks.AIR &&
                neighborState.is(grassSlab) &&
                neighborState.hasProperty(SNOWY) &&
                neighborState.getValue(SNOWY);
            return state.setValue(SNOWY, snowy);
        }
        return super.updateShape(
            state,
            level,
            tickAccess,
            pos,
            direction,
            neighborPos,
            neighborState,
            random
        );
    }

    @Override
    protected void randomTick(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        RandomSource random
    ) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        Block grassSlab = getGrassSlab();

        if (grassSlab == null || grassSlab == Blocks.AIR) return;

        if (!aboveState.is(grassSlab)) {
            level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
        }
    }
}
