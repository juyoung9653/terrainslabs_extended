package com.terrainslabs2;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Terrainslabs2Mod implements ModInitializer {

    public static final String MOD_ID = "terrainslabs2";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block GRASS_BLOCK2 = ModBlocks.register(
        "grass_block2",
        GrassBlock2::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.GRASS)
            .strength(0.6F)
            .sound(SoundType.GRASS)
            .randomTicks()
    );

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        Terrainslabs2Config.load();

        // 청크 생성 시 grass_slab 주변 흙 변환
        ServerChunkEvents.CHUNK_GENERATE.register((serverWorld, chunk) -> {
            Block grassSlab = GrassSlabHelper.getGrassSlab();
            if (grassSlab == null) return;

            // 이중 지연: 다른 모드의 블록 배치 이후 실행 보장
            serverWorld
                .getServer()
                .execute(() ->
                    serverWorld
                        .getServer()
                        .execute(() ->
                            GrassSlabHelper.convertDirtAroundGrassSlabs(
                                serverWorld,
                                chunk.getPos().x,
                                chunk.getPos().z,
                                chunk
                            )
                        )
                );
        });

        LOGGER.info("Terrain Slabs 2 initialized");
    }
}
