package net.romit.simpleedits.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.romit.simpleedits.SimpleEdits;
import net.romit.simpleedits.block.custom.LampBlock;

public class ModBlocks {
    public static final Block LAMP = registerBlock("lamp",
            new LampBlock(AbstractBlock.Settings.create()
                    .strength(1f).luminance(state -> state.get(LampBlock.CLICKED) ? 15 : 0)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(SimpleEdits.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(SimpleEdits.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        SimpleEdits.LOGGER.info("Registering Mod Blocks for " + SimpleEdits.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {

        });
    }
}
