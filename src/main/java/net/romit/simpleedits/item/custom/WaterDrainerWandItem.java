package net.romit.simpleedits.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class WaterDrainerWandItem extends Item {
    public WaterDrainerWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (player != null && !world.isClient) {
            boolean waterFound = false;
            for (BlockPos neighbor : getNeighbors(pos)) {
                if (world.getBlockState(neighbor).getBlock() == Blocks.WATER) {
                    drainWater(world, neighbor);
                    waterFound = true;
                }
            }
            if (waterFound) {
                player.sendMessage(Text.literal("Water drained!"), false);
            } else {
                player.sendMessage(Text.literal("No adjacent water blocks found!"), false);
            }
        }
        return ActionResult.SUCCESS;
    }

    private void drainWater(World world, BlockPos startPos) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toVisit = new LinkedList<>();
        toVisit.add(startPos);

        while (!toVisit.isEmpty()) {
            BlockPos pos = toVisit.poll();
            visited.add(pos);

            if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());

                for (BlockPos neighbor : getNeighbors(pos)) {
                    if (!visited.contains(neighbor) && !toVisit.contains(neighbor)) {
                        toVisit.add(neighbor);
                    }
                }
            }
        }
    }

    private Set<BlockPos> getNeighbors(BlockPos pos) {
        Set<BlockPos> neighbors = new HashSet<>();
        neighbors.add(pos.north());
        neighbors.add(pos.south());
        neighbors.add(pos.east());
        neighbors.add(pos.west());
        neighbors.add(pos.up());
        neighbors.add(pos.down());
        return neighbors;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.simpleedits.waterdrainwand.shift_down"));
        } else {
            tooltip.add(Text.translatable("tooltip.simpleedits.waterdrainwand"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}