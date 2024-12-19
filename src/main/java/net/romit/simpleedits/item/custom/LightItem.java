package net.romit.simpleedits.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class LightItem extends Item {

    public LightItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState currentBlockState = world.getBlockState(pos);

        // Handle Lantern Block (LIT property)
        if (currentBlockState.getBlock() == Blocks.LANTERN) {
            BooleanProperty lit = Properties.LIT;
            boolean newState = !currentBlockState.get(lit);
            BlockState newBlockState = currentBlockState.with(lit, newState);
            if (!world.isClient) {
                world.setBlockState(pos, newBlockState);
            }
            return ActionResult.SUCCESS;
        }

        // Handle Door Block (OPEN property)
        if (currentBlockState.getBlock() == Blocks.OAK_DOOR || currentBlockState.getBlock() == Blocks.IRON_DOOR) {
            BooleanProperty open = Properties.OPEN;
            boolean newState = !currentBlockState.get(open);
            BlockState newBlockState = currentBlockState.with(open, newState);
            if (!world.isClient) {
                world.setBlockState(pos, newBlockState);
            }
            return ActionResult.SUCCESS;
        }

        // Handle Piston Block (EXTENDED property)
        if (currentBlockState.getBlock() == Blocks.PISTON || currentBlockState.getBlock() == Blocks.STICKY_PISTON) {
            BooleanProperty extended = Properties.EXTENDED;
            boolean newState = !currentBlockState.get(extended);
            BlockState newBlockState = currentBlockState.with(extended, newState);
            if (!world.isClient) {
                world.setBlockState(pos, newBlockState);
            }
            return ActionResult.SUCCESS;
        }

        // Lighter functionality: Ignite Fire or Campfires
        if (currentBlockState.getBlock() == Blocks.CAMPFIRE || currentBlockState.getBlock() == Blocks.SOUL_CAMPFIRE) {
            // Turn on fire on campfire
            if (!currentBlockState.get(Properties.LIT)) {
                BlockState newBlockState = currentBlockState.with(Properties.LIT, true);
                if (!world.isClient) {
                    world.setBlockState(pos, newBlockState);
                }
                return ActionResult.SUCCESS;
            }
        }


        // Default behavior if block is not one of the handled types
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.simpleedits.lighter.shift_down"));
        } else {
            tooltip.add(Text.translatable("tooltip.simpleedits.lighter"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
