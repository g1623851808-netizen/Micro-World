package com.microworld;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlanetLocatorItem extends Item {

    private static final int SEARCH_RADIUS = 4500; // 直径9000

    public PlanetLocatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return new ActionResult<>(ActionResultType.SUCCESS, stack);

        if (player.world.getDimension().getType() != ModDimensions.COSMOS_TYPE) {
            player.sendMessage(new StringTextComponent("星球定位器只能在宇宙中使用"));
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }

        if (LightPointGenerator.LIGHT_POINTS.isEmpty()) {
            player.sendMessage(new StringTextComponent("光点数据尚未生成"));
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }

        ItemStack key = findStargateKey(player);
        if (key == null) {
            player.sendMessage(new StringTextComponent("背包中没有星门秘钥"));
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }

        if (StargateKeyItem.getDarkEnergy(key) < 50) {
            player.sendMessage(new StringTextComponent("暗能量不足！需要50点"));
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }

        StargateKeyItem.consumeDarkEnergy(key, 50);

        BlockPos playerPos = player.getPosition();
        List<LightPointGenerator.LightPointData> nearby = new ArrayList<>();

        for (LightPointGenerator.LightPointData data : LightPointGenerator.LIGHT_POINTS) {
            if (data.type == 0) continue;
            double dist = Math.sqrt(playerPos.distanceSq(data.pos));
            if (dist <= SEARCH_RADIUS) {
                nearby.add(data);
            }
        }

        nearby.sort(Comparator.comparingDouble(p -> Math.sqrt(playerPos.distanceSq(p.pos))));

        if (nearby.isEmpty()) {
            player.sendMessage(new StringTextComponent("直径9000格范围内没有发现星球光点"));
        } else {
            player.sendMessage(new StringTextComponent("§6===== 星球定位器扫描结果 ====="));
            player.sendMessage(new StringTextComponent("§e范围内共发现 " + nearby.size() + " 个星球光点"));

            int showCount = Math.min(10, nearby.size());
            for (int i = 0; i < showCount; i++) {
                LightPointGenerator.LightPointData data = nearby.get(i);
                BlockPos pos = data.pos;
                int dist = (int) Math.sqrt(playerPos.distanceSq(pos));
                String typeName = PlanetTypeConfig.RULES[data.type] != null ?
                        PlanetTypeConfig.RULES[data.type].name : "未知";
                String dir = getDirection(playerPos, pos);
                player.sendMessage(new StringTextComponent(
                        "§b" + (i + 1) + ". [" + typeName + "] " + dir + " " + dist + "格 " +
                                "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")"
                ));
            }
            if (nearby.size() > 10) {
                player.sendMessage(new StringTextComponent("§7...还有 " + (nearby.size() - 10) + " 个光点未显示"));
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    private ItemStack findStargateKey(PlayerEntity player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == ModItems.STARGATE_KEY) {
                return stack;
            }
        }
        return null;
    }

    private String getDirection(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dz = to.getZ() - from.getZ();
        StringBuilder dir = new StringBuilder();
        if (Math.abs(dx) > Math.abs(dz)) {
            dir.append(dx > 0 ? "东" : "西");
        } else {
            dir.append(dz > 0 ? "南" : "北");
        }
        int dy = to.getY() - from.getY();
        if (Math.abs(dy) > 10) {
            dir.append(dy > 0 ? "上" : "下");
        }
        return dir.toString();
    }
}