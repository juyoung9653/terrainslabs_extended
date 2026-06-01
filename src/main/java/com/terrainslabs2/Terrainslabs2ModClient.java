package com.terrainslabs2;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.GrassColor;

@Environment(EnvType.CLIENT)
public class Terrainslabs2ModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(
            (state, world, pos, tintIndex) -> world != null && pos != null
                ? BiomeColors.getAverageGrassColor(world, pos)
                : GrassColor.getDefaultColor(),
            Terrainslabs2Mod.GRASS_BLOCK2
        );
    }
}
