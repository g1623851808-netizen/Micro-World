package com.microworld;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class StargateKeyItem extends Item {

    public static final int MAX_DARK_ENERGY = 50;
    public static final int TELEPORT_COST = 10;

    public StargateKeyItem(Properties properties) {
        super(properties);
    }

    public static int getDarkEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt("DarkEnergy");
    }

    public static void setDarkEnergy(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("DarkEnergy", Math.min(amount, MAX_DARK_ENERGY));
    }

    public static boolean consumeDarkEnergy(ItemStack stack, int amount) {
        int current = getDarkEnergy(stack);
        if (current >= amount) {
            setDarkEnergy(stack, current - amount);
            return true;
        }
        return false;
    }

    public static void addDarkEnergy(ItemStack stack, int amount) {
        int current = getDarkEnergy(stack);
        setDarkEnergy(stack, current + amount);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        int energy = getDarkEnergy(stack);
        tooltip.add(new StringTextComponent("暗能量: " + energy + " / " + MAX_DARK_ENERGY));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return new ActionResult<>(ActionResultType.SUCCESS, stack);

        boolean inCosmos = player.world.getDimension().getType() == ModDimensions.COSMOS_TYPE;
        DimensionType currentDim = player.world.getDimension().getType();
        MinecraftServer server = player.world.getServer();

        // ===== 在星球世界 → 返回宇宙（到光点旁）=====
        boolean inPlanet = !inCosmos
                && currentDim != DimensionType.OVERWORLD
                && currentDim != DimensionType.THE_NETHER
                && currentDim != DimensionType.THE_END;

        if (inPlanet && ModDimensions.COSMOS_TYPE != null) {
            if (getDarkEnergy(stack) < TELEPORT_COST) {
                player.sendMessage(new StringTextComponent("暗能量不足！需要 " + TELEPORT_COST + " 点"));
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }

            consumeDarkEnergy(stack, TELEPORT_COST);
            ServerWorld cosmosWorld = server.getWorld(ModDimensions.COSMOS_TYPE);

            double targetX = player.posX;
            double targetY = 200;
            double targetZ = player.posZ;

            BlockPos originPos = PlanetOriginTracker.getOriginPos(player.getUniqueID());
            if (originPos != null) {
                targetX = originPos.getX() + 0.5;
                targetY = originPos.getY() + 2;
                targetZ = originPos.getZ() + 0.5;
            }

            if (cosmosWorld != null) {
                ((ServerPlayerEntity) player).teleport(cosmosWorld, targetX, targetY, targetZ, player.rotationYaw, player.rotationPitch);
                player.sendMessage(new StringTextComponent("返回宇宙"));
            }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        // ===== 在宇宙中：查找附近光点 =====
        if (inCosmos && ModDimensions.COSMOS_TYPE != null) {
            BlockPos playerPos = player.getPosition();
            LightPointGenerator.LightPointData nearest = null;
            double nearestDist = 5.0;

            for (LightPointGenerator.LightPointData data : LightPointGenerator.LIGHT_POINTS) {
                double dist = Math.sqrt(playerPos.distanceSq(data.pos));
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearest = data;
                }
            }

            if (nearest == null) {
                player.sendMessage(new StringTextComponent("附近没有光点"));
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }

            if (getDarkEnergy(stack) < TELEPORT_COST) {
                player.sendMessage(new StringTextComponent("暗能量不足！需要 " + TELEPORT_COST + " 点"));
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }

            consumeDarkEnergy(stack, TELEPORT_COST);

            if (nearest.type == 0) {
                // 白色光点 → 回主世界
                ServerWorld overworld = server.getWorld(DimensionType.OVERWORLD);
                if (overworld != null) {
                    ((ServerPlayerEntity) player).teleport(overworld, player.posX, 64, player.posZ, player.rotationYaw, player.rotationPitch);
                    player.sendMessage(new StringTextComponent("返回主世界"));
                }
            } else {
                // 彩色光点 → 进入星球
                int planetId = LightPointGenerator.LIGHT_POINTS.indexOf(nearest);
                PlanetOriginTracker.setOrigin(player.getUniqueID(), planetId);

                DimensionType planetType = ModDimensions.PLANET_TYPES.get(planetId);
                if (planetType == null) {
                    int radius = 1000 + player.world.rand.nextInt(2001);
                    planetType = ModDimensions.createPlanetDimension(planetId, radius, nearest.type);
                }

                ServerWorld planetWorld = server.getWorld(planetType);
                if (planetWorld != null) {
                    ((ServerPlayerEntity) player).teleport(planetWorld, 0.5, 66, 0.5, player.rotationYaw, player.rotationPitch);
                    player.sendMessage(new StringTextComponent("进入星球世界！"));
                }
            }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        // ===== 在主世界 → 进入宇宙 =====
        if (!inCosmos && !inPlanet && ModDimensions.COSMOS_TYPE != null) {
            if (getDarkEnergy(stack) < TELEPORT_COST) {
                player.sendMessage(new StringTextComponent("暗能量不足！需要 " + TELEPORT_COST + " 点"));
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }
            consumeDarkEnergy(stack, TELEPORT_COST);
            ServerWorld cosmosWorld = server.getWorld(ModDimensions.COSMOS_TYPE);
            if (cosmosWorld != null) {
                ((ServerPlayerEntity) player).teleport(cosmosWorld, player.posX, 200, player.posZ, player.rotationYaw, player.rotationPitch);
                player.sendMessage(new StringTextComponent("进入宇宙"));
            }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}