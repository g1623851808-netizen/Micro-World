package com.microworld;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ArmorFuelUtil {

    public static final int MAX_FUEL = 100;
    public static final int REFILL_AMOUNT = 25;

    // 获取燃料
    public static int getFuel(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.getInt("Fuel");
    }

    // 设置燃料
    public static void setFuel(ItemStack stack, int amount) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("Fuel", Math.min(amount, MAX_FUEL));
    }

    // 消耗燃料，返回剩余
    public static int consumeFuel(ItemStack stack, int amount) {
        int current = getFuel(stack);
        int newFuel = Math.max(0, current - amount);
        setFuel(stack, newFuel);
        return newFuel;
    }

    // 尝试自动补充
    public static boolean tryRefill(PlayerEntity player, ItemStack armorStack) {
        int fuel = getFuel(armorStack);
        if (fuel <= 1) {
            // 遍历背包找宇航燃料
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invStack = player.inventory.getStackInSlot(i);
                if (invStack.getItem() == ModItems.ASTRO_FUEL) {
                    invStack.shrink(1);  // 消耗1瓶燃料
                    setFuel(armorStack, fuel + REFILL_AMOUNT);
                    return true;
                }
            }
        }
        return false;
    }

    // 初始化燃料
    public static void initFuel(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains("Fuel")) {
            setFuel(stack, MAX_FUEL);
        }
    }
}