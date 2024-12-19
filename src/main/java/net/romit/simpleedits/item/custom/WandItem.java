package net.romit.simpleedits.item.custom;

import net.romit.simpleedits.SimpleEdits;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandItem extends Item {
    private static final Map<UUID, BlockPos[]> playerPositions = new HashMap<>();

    public WandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (player == null || world.isClient) {
            return ActionResult.SUCCESS;
        }

        UUID playerUUID = player.getUuid();
        BlockPos clickedPos = context.getBlockPos();
        BlockPos[] positions = playerPositions.getOrDefault(playerUUID, new BlockPos[2]);

        if (player.isSneaking()) {
            positions[0] = clickedPos;
            player.sendMessage(Text.literal("Start position set to: " + clickedPos), true);
        } else {
            positions[1] = clickedPos;
            player.sendMessage(Text.literal("End position set to: " + clickedPos), true);

            if (positions[0] != null) {
                fillArea(world, positions[0], positions[1], player);
                player.sendMessage(Text.literal("Filled area from " + positions[0] + " to " + positions[1]), true);
                playerPositions.remove(playerUUID);
            }
        }

        playerPositions.put(playerUUID, positions);
        return ActionResult.SUCCESS;
    }

    private void fillArea(World world, BlockPos start, BlockPos end, PlayerEntity player) {
        Block blockToPlace = SimpleEdits.playerBlocks.getOrDefault(player.getUuid(), Blocks.DIRT);
        int minX = Math.min(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());
        int minZ = Math.min(start.getZ(), end.getZ());
        int maxX = Math.max(start.getX(), end.getX());
        int maxY = Math.max(start.getY(), end.getY());
        int maxZ = Math.max(start.getZ(), end.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos targetPos = new BlockPos(x, y, z);
                    if (world.isAir(targetPos)) {
                        world.setBlockState(targetPos, blockToPlace.getDefaultState());
                    }
                }
            }
        }
    }
}
