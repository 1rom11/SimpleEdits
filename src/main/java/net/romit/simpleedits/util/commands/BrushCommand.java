package net.romit.simpleedits.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.romit.simpleedits.item.custom.BrushItem;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.literal;

public class BrushCommand {
    private static final SuggestionProvider<ServerCommandSource> BLOCK_SUGGESTIONS = (context, builder) -> {
        return net.minecraft.command.CommandSource.suggestIdentifiers(Registries.BLOCK.getIds(), builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("brush")
            .then(CommandManager.argument("size", IntegerArgumentType.integer(1, 10))
                .then(CommandManager.argument("block", IdentifierArgumentType.identifier())
                    .suggests(BLOCK_SUGGESTIONS)
                    .then(CommandManager.argument("shape", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            builder.suggest("cubic");
                            builder.suggest("spherical");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            int size = IntegerArgumentType.getInteger(context, "size");
                            BrushItem.setBrushSize(size);
                            Identifier blockId = IdentifierArgumentType.getIdentifier(context, "block");
                            Block block = Registries.BLOCK.get(blockId);
                            if (block == Blocks.AIR) {
                                context.getSource().sendError(Text.literal("Invalid block type: " + blockId));
                                return 0;
                            }
                            BrushItem.setBlockType(block);
                            String shape = StringArgumentType.getString(context, "shape");
                            BrushItem.setBrushShape(shape);
                            context.getSource().sendFeedback(() -> Text.of("Brush size set to " + size + ", block set to " + blockId + ", and shape set to " + shape), true);
                            return 1;
                        })
                    )
                )
            )
        );
    }
}