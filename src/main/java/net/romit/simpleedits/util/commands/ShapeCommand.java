package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class ShapeCommand {
    private static final Map<UUID, String> playerShapes = new HashMap<>();
    private static final String DEFAULT_SHAPE = "circle";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("shape")
                .then(CommandManager.argument("shape", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            builder.suggest("circle");
                            builder.suggest("square");
                            builder.suggest("cube");
                            builder.suggest("sphere");
                            builder.suggest("triangle");
                            builder.suggest("hexagon");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String shape = StringArgumentType.getString(context, "shape");
                            ServerCommandSource source = context.getSource();
                            UUID playerUUID = source.getPlayer().getUuid();
                            playerShapes.put(playerUUID, shape);
                            source.sendFeedback((Supplier<Text>) Text.literal("Shape set to: " + shape), false);
                            return 1;
                        })
                )
        );
    }

    public static String getShape(UUID playerUUID) {
        return playerShapes.getOrDefault(playerUUID, DEFAULT_SHAPE);
    }
}