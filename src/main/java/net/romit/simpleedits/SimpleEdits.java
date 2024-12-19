package net.romit.simpleedits;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;
import net.romit.simpleedits.util.Identifier; // Your custom Identifier
import net.romit.simpleedits.item.ModItemGroups;
import net.romit.simpleedits.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleEdits implements ModInitializer {
	public static final String MOD_ID = "simpleedits";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Map<UUID, Block> playerBlocks = new HashMap<>();

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("setfillblock")
					.then(CommandManager.argument("block_id", StringArgumentType.string())
							.executes(context -> {
								ServerCommandSource source = context.getSource();
								String blockId = StringArgumentType.getString(context, "block_id");
								UUID playerUUID = source.getPlayer().getUuid();

								try {
									Identifier customId = new Identifier(blockId);
									Block block = Registries.BLOCK.get(net.romit.simpleedits.util.Identifier.of(blockId)); //I don't know how to fix this :(

									if (block != Blocks.AIR) {
										playerBlocks.put(playerUUID, block);
										source.sendFeedback(Text.literal("Selected block: " + blockId), false);
									} else {
										source.sendError(Text.literal("Invalid block ID: " + blockId));
									}
								} catch (IllegalArgumentException e) {
									source.sendError(Text.literal("Invalid format for block ID: " + blockId));
								}

								return 1;
							})
					)
			);
		});

		LOGGER.info("Initialized!");
	}
}
