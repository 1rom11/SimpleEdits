package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.romit.simpleedits.item.custom.ShapeItem;
import net.romit.simpleedits.item.custom.WandItem;

public class UndoShapeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("undoshape")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ShapeItem.undoShapeCommand(source.getPlayer());
                    return 1;
                })
        );
    }
}