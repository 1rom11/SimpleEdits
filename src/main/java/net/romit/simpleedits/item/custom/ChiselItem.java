package net.romit.simpleedits.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a custom item that allows players to chisel certain blocks into a different block.
 * The item is used by right-clicking on a block, and if the block is chiselable, the block is changed to a chiseled variant.
 */

public class ChiselItem extends Item {
    public static final Map<Block, Block> CHISEL_MAP = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Blocks.STONE, Blocks.STONE_BRICKS),
            new AbstractMap.SimpleEntry<>(Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS),
            new AbstractMap.SimpleEntry<>(Blocks.DEEPSLATE, Blocks.CHISELED_DEEPSLATE),
            new AbstractMap.SimpleEntry<>(Blocks.TUFF, Blocks.CHISELED_TUFF),
            new AbstractMap.SimpleEntry<>(Blocks.TUFF_BRICKS, Blocks.CHISELED_TUFF_BRICKS),
            new AbstractMap.SimpleEntry<>(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE),
            new AbstractMap.SimpleEntry<>(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE),
            new AbstractMap.SimpleEntry<>(Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS),
            new AbstractMap.SimpleEntry<>(Blocks.POLISHED_BLACKSTONE, Blocks.CHISELED_POLISHED_BLACKSTONE),
            new AbstractMap.SimpleEntry<>(Blocks.QUARTZ_BLOCK, Blocks.CHISELED_QUARTZ_BLOCK),
            new AbstractMap.SimpleEntry<>(Blocks.COPPER_BLOCK, Blocks.CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.EXPOSED_COPPER, Blocks.EXPOSED_CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.WEATHERED_COPPER, Blocks.WEATHERED_CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.OXIDIZED_COPPER, Blocks.OXIDIZED_CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_WEATHERED_CHISELED_COPPER),
            new AbstractMap.SimpleEntry<>(Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_CHISELED_COPPER),
            //Slabs
            new AbstractMap.SimpleEntry<>(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB),
            new AbstractMap.SimpleEntry<>(Blocks.TUFF_SLAB, Blocks.TUFF_BRICK_SLAB),
            new AbstractMap.SimpleEntry<>(Blocks.PRISMARINE_SLAB, Blocks.PRISMARINE_BRICK_SLAB),
            new AbstractMap.SimpleEntry<>(Blocks.BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB)
            );

    public ChiselItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (CHISEL_MAP.containsKey(clickedBlock)) {
            if (!world.isClient) {
                world.setBlockState(context.getBlockPos(), CHISEL_MAP.get(clickedBlock).getDefaultState());

                context.getStack().damage(1, ((ServerWorld) world), ((ServerPlayerEntity) context.getPlayer()),
                        item -> context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));


                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.BLOCKS);
            }
        }


        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.simpleedits.chisel.shift_down"));
        } else {
            tooltip.add(Text.translatable("tooltip.simpleedits.chisel"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
