package net.romit.simpleedits.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.romit.simpleedits.util.commands.BlockCommand;
import net.romit.simpleedits.util.commands.RadiusCommand;
import net.romit.simpleedits.util.commands.ShapeCommand;

import java.util.List;
import java.util.UUID;

import static net.romit.simpleedits.item.custom.WandItem.playerBlockTypes;

public class ShapeItem extends Item {

    public ShapeItem(Settings settings) {
        super(settings);
    }

    public static void setBlockType(UUID playerId, String blockType) {
        playerBlockTypes.put(playerId, blockType);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (!world.isClient && player != null) {
            UUID playerId = player.getUuid();
            String blockType = playerBlockTypes.getOrDefault(playerId, "minecraft:air");
            int radius = RadiusCommand.getRadius(player.getUuid());
            String shape = ShapeCommand.getShape(player.getUuid());

            // Disable command feedback
            boolean originalFeedback = world.getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);
            world.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(false, world.getServer());

            // Create the shape
            switch (shape) {
                case "square":
                    createSquare(world, pos, blockType, radius);
                    break;
                case "cube":
                    createCube(world, pos, blockType, radius);
                    break;
                case "sphere":
                    createSphere(world, pos, blockType, radius);
                    break;
                case "triangle":
                    createTriangle(world, pos, blockType, radius);
                    break;
                case "hexagon":
                    createHexagon(world, pos, blockType, radius);
                    break;
                case "circle":
                default:
                    createCircle(world, pos, blockType, radius);
                    break;
            }

            // Restore command feedback
            world.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(originalFeedback, world.getServer());

            world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private void createSphere(World world, BlockPos pos, String blockType, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource(),
                                String.format("/setblock %d %d %d %s", pos.getX() + x, pos.getY() + y, pos.getZ() + z, blockType));
                    }
                }
            }
        }
    }

    private void createCube(World world, BlockPos pos, String blockType, int radius) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource(),
                            String.format("/setblock %d %d %d %s", x + i, y + j, z + k, blockType));
                }
            }
        }
    }

    private void createSquare(World world, BlockPos pos, String blockType, int radius) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource(),
                        String.format("/setblock %d %d %d %s", x + i, y, z + j, blockType));
            }
        }
    }

    private void createCircle(World world, BlockPos center, String blockType, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos pos = center.add(x, 0, z);
                    world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource(),
                            String.format("/setblock %d %d %d %s", pos.getX(), pos.getY(), pos.getZ(), blockType));
                }
            }
        }
    }

    private void createTriangle(World world, BlockPos center, String blockType, int radius) {
    for (int y = 0; y <= radius; y++) {
        for (int x = -y; x <= y; x++) {
            int z = radius - y;
            BlockPos pos = center.add(x, 0, z);
            world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource(),
                    String.format("/setblock %d %d %d %s", pos.getX(), pos.getY(), pos.getZ(), blockType));
        }
    }
}

    private void createHexagon(World world, BlockPos center, String blockType, int radius) {
    for (int x = -radius; x <= radius; x++) {
        for (int z = -radius; z <= radius; z++) {
            if (Math.abs(x) + Math.abs(z) + Math.abs(x + z) <= radius * 2) {
                BlockPos pos = center.add(x, 0, z);
                world.getServer().getCommandManager().executeWithPrefix(world.getServer().getCommandSource(),
                        String.format("/setblock %d %d %d %s", pos.getX(), pos.getY(), pos.getZ(), blockType));
            }
        }
    }
}


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.simpleedits.shapewand.tooltip"));

        super.appendTooltip(stack, context, tooltip, type);
    }
}