package net.romit.simpleedits.item.custom;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandTool implements ModInitializer {

    private final Map<UUID, BlockPos[]> playerSelections = new HashMap<>();

    @Override
    public void onInitialize() {
        // Register event to handle block usage for setting selection positions
        net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient && player.getMainHandStack().getItem() == Items.WOODEN_AXE) {
                BlockPos clickedBlockPos = hitResult.getBlockPos();
                UUID playerUUID = player.getUuid();

                // Set first or second position based on whether the player is sneaking
                if (player.isSneaking()) {
                    playerSelections.computeIfAbsent(playerUUID, k -> new BlockPos[2])[1] = clickedBlockPos;
                    player.sendMessage(Text.literal("Second position set to: " + clickedBlockPos), false);
                } else {
                    playerSelections.computeIfAbsent(playerUUID, k -> new BlockPos[2])[0] = clickedBlockPos;
                    player.sendMessage(Text.literal("First position set to: " + clickedBlockPos), false);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        // Register the fillregion command
        net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("fillregion")
                    .then(CommandManager.argument("block", StringArgumentType.string())
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                ServerPlayerEntity player = source.getPlayer();
                                if (player != null) {
                                    UUID playerUUID = player.getUuid();
                                    BlockPos[] positions = playerSelections.get(playerUUID);

                                    // Check if both positions are set
                                    if (positions != null && positions[0] != null && positions[1] != null) {
                                        BlockPos pos1 = positions[0];
                                        BlockPos pos2 = positions[1];
                                        String blockName = StringArgumentType.getString(context, "block");
                                        Block block = Registries.BLOCK.get(new Iden(blockName));

                                        // Check if the block is valid
                                        fillRegion(player.getWorld(), pos1, pos2, block.getDefaultState());
                                        player.sendMessage(Text.literal("Region filled with block: " + blockName), false);
                                        return 1;
                                    } else {
                                        player.sendMessage(Text.literal("Both positions must be set before using this command!"), false);
                                    }
                                }
                                return 0;
                            })));
        });
    }

    // Fill the selected region with the specified block state
    private void fillRegion(World world, BlockPos pos1, BlockPos pos2, BlockState blockState) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        // Loop through the area and fill with the block
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    world.setBlockState(currentPos, blockState);
                }
            }
        }
    }
}
