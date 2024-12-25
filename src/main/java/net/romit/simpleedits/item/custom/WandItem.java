package net.romit.simpleedits.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents a custom item that allows players to fill a region with a specified block type.
 * The item is used by right-clicking on two blocks to set the region, and then right-clicking again to fill the region.
 * The item stores the original blocks in the region and allows players to undo the fill operation.
 */

public class WandItem extends Item {
    public static final Map<UUID, BlockPos[]> playerPositions = new HashMap<>();
    public static final Map<UUID, String> playerBlockTypes = new HashMap<>();
    public static final Map<UUID, Map<BlockPos, BlockState>> playerUndoData = new HashMap<>();

    public WandItem(Settings settings) {
        super(settings);
    }

    public static void setBlockType(UUID playerId, String blockType) {
        playerBlockTypes.put(playerId, blockType);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) { //right click to set positions
        PlayerEntity player = context.getPlayer();
        if (player != null && !context.getWorld().isClient) {
            UUID playerId = player.getUuid();
            BlockPos pos = context.getBlockPos();
            BlockPos[] positions = playerPositions.getOrDefault(playerId, new BlockPos[2]);
            World world = context.getWorld();

            if (positions[0] == null) {
                positions[0] = pos;
                player.sendMessage(Text.literal("First position set at: " + pos), false);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                positions[1] = pos;
                player.sendMessage(Text.literal("Second position set at: " + pos), false);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                String blockType = playerBlockTypes.getOrDefault(playerId, "minecraft:air");
                storeOriginalBlocks(player, positions[0], positions[1]);
                executeFillCommand(player, positions[0], positions[1], blockType);
                positions[0] = null; // Clear positions after filling
                positions[1] = null;
            }

            playerPositions.put(playerId, positions);
        }

        return ActionResult.SUCCESS;
    }

    private void storeOriginalBlocks(PlayerEntity player, BlockPos pos1, BlockPos pos2) {
        UUID playerId = player.getUuid();
        Map<BlockPos, BlockState> originalBlocks = new HashMap<>();

        int x1 = pos1.getX();
        int y1 = pos1.getY();
        int z1 = pos1.getZ();
        int x2 = pos2.getX();
        int y2 = pos2.getY();
        int z2 = pos2.getZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    originalBlocks.put(pos, player.getWorld().getBlockState(pos));
                }
            }
        }

        playerUndoData.put(playerId, originalBlocks);
    }

    public static void clearPositions(UUID playerId) {
        playerPositions.remove(playerId);
    }

    private void executeFillCommand(PlayerEntity player, BlockPos pos1, BlockPos pos2, String blockType) {
        ServerCommandSource source = player.getCommandSource();
        boolean originalFeedback = source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);
        source.getWorld().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(false, source.getServer());

        int maxVolume = 32768;

        int x1 = pos1.getX();
        int y1 = pos1.getY();
        int z1 = pos1.getZ();
        int x2 = pos2.getX();
        int y2 = pos2.getY();
        int z2 = pos2.getZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x += 32) {
            for (int y = minY; y <= maxY; y += 32) {
                for (int z = minZ; z <= maxZ; z += 32) {
                    int endY = Math.min(y + 31, maxY);
                    int endZ = Math.min(z + 31, maxZ);
                    int endX = Math.min(x + 31, maxX);

                    int volume = (endX - x + 1) * (endY - y + 1) * (endZ - z + 1);
                    if (volume <= maxVolume) {
                        String command = String.format("/fill %d %d %d %d %d %d %s", x, y, z, endX, endY, endZ, blockType);
                        source.getServer().getCommandManager().executeWithPrefix(source, command);
                    } else {
                        for (int subX = x; subX <= endX; subX += 16) {
                            for (int subY = y; subY <= endY; subY += 16) {
                                for (int subZ = z; subZ <= endZ; subZ += 16) {
                                    int subEndX = Math.min(subX + 15, endX);
                                    int subEndY = Math.min(subY + 15, endY);
                                    int subEndZ = Math.min(subZ + 15, endZ);

                                    String subCommand = String.format("/fill %d %d %d %d %d %d %s", subX, subY, subZ, subEndX, subEndY, subEndZ, blockType);
                                    source.getServer().getCommandManager().executeWithPrefix(source, subCommand);
                                }
                            }
                        }
                    }
                }
            }
        }

        source.getWorld().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(originalFeedback, source.getServer());
    }

    public static void undoFillCommand(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        Map<BlockPos, BlockState> originalBlocks = playerUndoData.get(playerId);

        if (originalBlocks != null) {
            for (Map.Entry<BlockPos, BlockState> entry : originalBlocks.entrySet()) {
                player.getWorld().setBlockState(entry.getKey(), entry.getValue());
            }
            player.sendMessage(Text.literal("Undo operation completed."), false);
            playerUndoData.remove(playerId);
        } else {
            player.sendMessage(Text.literal("No undo data available."), false);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.simpleedits.wand.shift_down"));
        } else {
            tooltip.add(Text.translatable("tooltip.simpleedits.wand"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}