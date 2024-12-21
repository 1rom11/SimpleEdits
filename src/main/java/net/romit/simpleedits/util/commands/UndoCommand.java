package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.romit.simpleedits.item.custom.WandItem;

public class UndoCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("undo")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    WandItem.undoFillCommand(source.getPlayer());
                    return 1;
                })
        );
    }
}