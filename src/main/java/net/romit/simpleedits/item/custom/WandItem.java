package net.romit.simpleedits.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandItem extends Item {
    public static final Map<UUID, BlockPos[]> playerPositions = new HashMap<>();

    public WandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && !context.getWorld().isClient) {
            UUID playerId = player.getUuid();
            BlockPos pos = context.getBlockPos();
            BlockPos[] positions = playerPositions.getOrDefault(playerId, new BlockPos[2]);

            if (positions[0] == null) {
                positions[0] = pos;
                player.sendMessage(Text.literal("First position set at: " + pos), false);
            } else {
                positions[1] = pos;
                player.sendMessage(Text.literal("Second position set at: " + pos), false);
                executeFillCommand(player, positions[0], positions[1]);
                positions[0] = null; // Clear positions after filling
                positions[1] = null;
            }

            playerPositions.put(playerId, positions);
        }
        return ActionResult.SUCCESS;
    }

    private void executeFillCommand(PlayerEntity player, BlockPos pos1, BlockPos pos2) {
        ServerCommandSource source = player.getCommandSource();
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

        for (int x = minX; x <= maxX; x += 16) {
            for (int y = minY; y <= maxY; y += 16) {
                for (int z = minZ; z <= maxZ; z += 16) {
                    int endX = Math.min(x + 15, maxX);
                    int endY = Math.min(y + 15, maxY);
                    int endZ = Math.min(z + 15, maxZ);

                    int volume = (endX - x + 1) * (endY - y + 1) * (endZ - z + 1);
                    if (volume <= maxVolume) {
                        String command = String.format("/fill %d %d %d %d %d %d air", x, y, z, endX, endY, endZ);
                        source.getServer().getCommandManager().executeWithPrefix(source, command);
                    }
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            UUID playerId = player.getUuid();
            BlockPos[] positions = playerPositions.get(playerId);

            if (positions != null && positions[0] != null && positions[1] != null) {
                player.sendMessage(Text.literal("Positions set: " + positions[0] + " and " + positions[1]), false);
            } else {
                player.sendMessage(Text.literal("Please set both positions using the wand."), false);
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }
}