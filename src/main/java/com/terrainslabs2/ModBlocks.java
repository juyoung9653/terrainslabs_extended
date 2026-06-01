package com.terrainslabs2;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {

    private ModBlocks() {}

    public static void initialize() {}

    public static <T extends Block> T register(
        String path,
        Function<BlockBehaviour.Properties, T> factory,
        BlockBehaviour.Properties properties
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(
            Terrainslabs2Mod.MOD_ID,
            path
        );
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        @SuppressWarnings("unchecked")
        T block = (T) Blocks.register(
            key,
            (Function<BlockBehaviour.Properties, Block>) factory,
            properties
        );
        Items.registerBlock(block);
        return block;
    }
}
