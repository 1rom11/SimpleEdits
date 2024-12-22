package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.romit.simpleedits.item.custom.ShapeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class RadiusCommand {
    private static final Map<UUID, Integer> playerRadii = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("radius")
                .then(CommandManager.argument("radius", IntegerArgumentType.integer(0, 100))
                        .executes(context -> {
                            int radius = IntegerArgumentType.getInteger(context, "radius");
                            ServerCommandSource source = context.getSource();
                            UUID playerUUID = source.getPlayer().getUuid();
                            playerRadii.put(playerUUID, radius);
                            source.sendFeedback((Supplier<Text>) Text.literal("Radius set to: " + radius), false);
                            return 1;
                        })
                )
        );
    }

    public static int getRadius(UUID playerUUID) {
        return playerRadii.getOrDefault(playerUUID, 0);
    }
}