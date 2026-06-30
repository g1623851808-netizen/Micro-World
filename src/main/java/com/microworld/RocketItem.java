package com.microworld;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class RocketItem extends Item {

    public RocketItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return new ActionResult<>(ActionResultType.SUCCESS, stack);

        // 宇宙中不可使用
        if (player.world.getDimension().getType() == ModDimensions.COSMOS_TYPE) {
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }

        if (ModDimensions.COSMOS_TYPE != null) {
            MinecraftServer server = player.world.getServer();
            ServerWorld cosmosWorld = server.getWorld(ModDimensions.COSMOS_TYPE);
            if (cosmosWorld == null) {
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }

            Random random = player.world.rand;
            double targetX = player.posX;
            double targetY = 200;
            double targetZ = player.posZ;

            // 获取来源光点坐标（如果从星球出发）
            BlockPos originPos = PlanetOriginTracker.getOriginPos(player.getUniqueID());
            if (originPos != null) {
                // 随机偏移100格以内
                targetX = originPos.getX() + (random.nextDouble() - 0.5) * 200;
                targetY = originPos.getY() + 2;
                targetZ = originPos.getZ() + (random.nextDouble() - 0.5) * 200;
            } else {
                // 从主世界出发：传送到主世界光点附近（主光点坐标0,128,0）
                // 遍历光点列表找type=0的
                for (LightPointGenerator.LightPointData data : LightPointGenerator.LIGHT_POINTS) {
                    if (data.type == 0) {
                        targetX = data.pos.getX() + (random.nextDouble() - 0.5) * 200;
                        targetY = data.pos.getY() + 2;
                        targetZ = data.pos.getZ() + (random.nextDouble() - 0.5) * 200;
                        break;
                    }
                }
                // 如果还没生成光点，默认0,128,0
                if (targetX == player.posX && targetY == 200 && targetZ == player.posZ) {
                    targetX = (random.nextDouble() - 0.5) * 200;
                    targetY = 130;
                    targetZ = (random.nextDouble() - 0.5) * 200;
                }
            }

            ((ServerPlayerEntity) player).teleport(cosmosWorld, targetX, targetY, targetZ, player.rotationYaw, player.rotationPitch);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.FAIL, stack);
    }
}