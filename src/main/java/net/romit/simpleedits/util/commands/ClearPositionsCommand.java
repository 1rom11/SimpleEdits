package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.romit.simpleedits.item.custom.WandItem;

import java.util.function.Supplier;

public class ClearPositionsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("clearpos")
            .executes(context -> {
                ServerCommandSource source = context.getSource();
                WandItem.clearPositions(source.getPlayer().getUuid());
                source.sendFeedback(() -> Text.of("Positions cleared."), false);
                return 1;
            })
        );
    }
}