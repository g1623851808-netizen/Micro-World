package com.microworld;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpaceBuilderItem extends Item {

    public SpaceBuilderItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote) return new ActionResult<>(ActionResultType.SUCCESS, stack);

        Vec3d lookVec = player.getLookVec();
        BlockPos targetPos = new BlockPos(
                player.posX + lookVec.x * 2,
                player.posY + player.getEyeHeight() + lookVec.y * 2,
                player.posZ + lookVec.z * 2
        );

        if (world.isAirBlock(targetPos)) {
            world.setBlockState(targetPos, Blocks.STONE.getDefaultState(), 3);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}