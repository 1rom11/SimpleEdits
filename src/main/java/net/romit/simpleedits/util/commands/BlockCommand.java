package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.romit.simpleedits.item.custom.WandItem;
import net.minecraft.util.Identifier;

public class BlockCommand {
    private static final SuggestionProvider<ServerCommandSource> BLOCK_SUGGESTIONS = (context, builder) -> {
        return net.minecraft.command.CommandSource.suggestMatching(
            Registries.BLOCK.getIds().stream()
                .map(Identifier::toString)
                .map(id -> id.contains(":") ? id.split(":")[1] : id),
            builder
        );
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("block")
            .then(CommandManager.argument("blockType", StringArgumentType.string())
                .suggests(BLOCK_SUGGESTIONS)
                .executes(context -> {
                    String blockType = StringArgumentType.getString(context, "blockType");
                    ServerCommandSource source = context.getSource();
                    WandItem.setBlockType(source.getPlayer().getUuid(), blockType);
                    source.sendFeedback(() -> Text.literal("Block type set to: " + blockType), false);
                    return 1;
                })
            )
        );
    }
}