package net.romit.simpleedits.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.World;


public class BrushItem extends Item {
    private static Block blockType = Blocks.STONE;
    private static int brushSize = 1;
    private static String brushShape = "cubic";

    public BrushItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient && player != null) {
            BlockHitResult hitResult = (BlockHitResult) player.raycast(5000, 0, false); // Increased raycast distance
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
                draw(world, pos);
            }
        }
        assert player != null;
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    private void draw(World world, BlockPos pos) {
        var blockState = blockType.getDefaultState();
        var mutablePos = new Mutable();
        int radius = brushSize;
        int radiusSquared = radius * radius;

        switch (brushShape) {
            case "spherical":
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + y * y + z * z <= radiusSquared) {
                                mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                                world.setBlockState(mutablePos, blockState);
                            }
                        }
                    }
                }
                break;
            case "cubic":
            default:
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                            world.setBlockState(mutablePos, blockState);
                        }
                    }
                }
                break;
        }
    }

    public static void setBlockType(Block block) {
        blockType = block;
    }

    public static void setBrushSize(int size) {
        brushSize = size;
    }

    public static void setBrushShape(String shape) {
        brushShape = shape;
    }
}