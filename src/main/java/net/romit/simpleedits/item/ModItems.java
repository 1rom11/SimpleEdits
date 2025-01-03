package net.romit.simpleedits.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.romit.simpleedits.SimpleEdits;
import net.romit.simpleedits.item.custom.*;

public class ModItems {
    public static final Item CHISEL = registerItem("chisel", new ChiselItem(new Item.Settings().maxDamage(32)));
    public static final Item WAND = registerItem("wand", new WandItem(new Item.Settings().maxDamage(64)));
    public static final Item WATERDRAINERWAND = registerItem("waterdrainwand", new WaterDrainerWandItem(new Item.Settings().maxDamage(64)));
    public static final Item BLOCKSTATECHANGER = registerItem("blockstatechanger", new BlockStateChangerItem(new Item.Settings().maxDamage(64)));
    public static final Item SHAPEWAND = registerItem("shapewand", new ShapeItem(new Item.Settings().maxDamage(64)));
    public static final Item BRUSH = registerItem("brush", new BrushItem(new Item.Settings().maxDamage(64)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SimpleEdits.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SimpleEdits.LOGGER.info("Registering Mod Items for " + SimpleEdits.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(CHISEL);
            entries.add(WAND);
            entries.add(WATERDRAINERWAND);
            entries.add(BLOCKSTATECHANGER);
            entries.add(SHAPEWAND);
            entries.add(BRUSH);
        });
    }
}