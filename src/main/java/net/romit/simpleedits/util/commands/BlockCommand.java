package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.romit.simpleedits.item.custom.WandItem;

public class BlockCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("block")
            .then(CommandManager.argument("blockType", StringArgumentType.string())
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