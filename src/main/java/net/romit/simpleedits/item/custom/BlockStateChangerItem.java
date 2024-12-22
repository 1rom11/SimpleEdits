package net.romit.simpleedits.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * This class represents a custom item that allows players to change the state of certain blocks.
 * The item is used by right-clicking on a block, and if the block is a stair or slab, the block state is changed.
 */
//Shayan wanted this or smth

public class BlockStateChangerItem extends Item {

    public BlockStateChangerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof StairsBlock) {
            BlockState newState = state.cycle(Properties.HORIZONTAL_FACING);
            world.setBlockState(pos, newState, 3);
            world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        } else if (state.getBlock() instanceof SlabBlock) {
            BlockState newState = state.cycle(Properties.SLAB_TYPE);
            world.setBlockState(pos, newState, 3);
            world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.simpleedits.blockstatechanger.tooltip"));
        super.appendTooltip(stack, context, tooltip, type);
    }
}