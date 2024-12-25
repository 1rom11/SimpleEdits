package net.romit.simpleedits.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.romit.simpleedits.SimpleEdits;

public class ModItemGroups {
    public static final ItemGroup TOOLS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(SimpleEdits.MOD_ID, "tools"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.WAND))
                    .displayName(Text.translatable("itemgroup.simpleedits.tools"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.CHISEL);
                        entries.add(ModItems.WAND);
                        entries.add(ModItems.WATERDRAINERWAND);
                        entries.add(ModItems.BLOCKSTATECHANGER);
                        entries.add(ModItems.SHAPEWAND);
                        entries.add(ModItems.BRUSH);
                    }).build());

    public static void registerItemGroups() {
        SimpleEdits.LOGGER.info("Registering Item Groups for " + SimpleEdits.MOD_ID);
    }
}
