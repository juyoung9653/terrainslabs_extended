package com.terrainslabs2;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

public final class GrassSlabHelper {

    private GrassSlabHelper() {}

    private static Block grassSlabCache = null;

    public static Block getGrassSlab() {
        if (grassSlabCache == null) {
            grassSlabCache = BuiltInRegistries.BLOCK.getValue(
                Identifier.fromNamespaceAndPath("terrain_slabs", "grass_slab")
            );
        }
        return grassSlabCache;
    }

    public static void convertDirtAroundGrassSlabs(
        ServerLevel level,
        int chunkX,
        int chunkZ,
        LevelChunk chunk
    ) {
        Block grassSlab = getGrassSlab();
        if (grassSlab == null || grassSlab == Blocks.AIR) return;

        Terrainslabs2Config config = Terrainslabs2Config.load();
        int depth = Math.max(1, config.dirtDepth);
        boolean chainGrassBlock = config.convertDirtUnderGrassBlock;

        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int minY = level.getMinY();
        Heightmap heightmap = chunk.getOrCreateHeightmapUnprimed(
            Heightmap.Types.MOTION_BLOCKING
        );
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = minX + dx;
                int z = minZ + dz;
                int topY = heightmap.getFirstAvailable(dx, dz) - 1;

                // 위가 공기인 블록만 변환 소스로 취급. 내부 고체는 스킵.
                boolean aboveIsAir = true;

                for (int y = topY; y >= minY; y--) {
                    BlockState state = chunk.getBlockState(pos.set(x, y, z));
                    boolean isSource =
                        aboveIsAir &&
                        (state.is(grassSlab) ||
                            (chainGrassBlock &&
                                (state.is(Blocks.GRASS_BLOCK) ||
                                    state.is(Terrainslabs2Mod.GRASS_BLOCK2))));

                    if (isSource) {
                        boolean snowy =
                            state.hasProperty(GrassBlock2.SNOWY) &&
                            state.getValue(GrassBlock2.SNOWY);

                        for (int d = 1; d <= depth; d++) {
                            int by = y - d;
                            if (by < minY) break;
                            BlockState belowState = chunk.getBlockState(
                                pos.set(x, by, z)
                            );
                            if (!belowState.is(Blocks.DIRT)) break;
                            chunk.setBlockState(
                                pos,
                                Terrainslabs2Mod.GRASS_BLOCK2.defaultBlockState().setValue(
                                    GrassBlock2.SNOWY,
                                    snowy
                                ),
                                2
                            );
                        }
                    }

                    aboveIsAir = state.isAir();
                }
            }
        }
    }
}
