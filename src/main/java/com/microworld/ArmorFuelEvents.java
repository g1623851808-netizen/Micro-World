package com.microworld;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorFuelEvents {

    // 物品制作完成时初始化燃料
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        if (isSpacesuit(result)) {
            ArmorFuelUtil.initFuel(result);
        }
    }

    // 穿上宇航服时确保有燃料NBT
    @SubscribeEvent
    public static void onEquip(LivingEquipmentChangeEvent event) {
        ItemStack to = event.getTo();
        if (isSpacesuit(to)) {
            ArmorFuelUtil.initFuel(to);
        }
    }

    private static boolean isSpacesuit(ItemStack stack) {
        return stack.getItem() == ModArmor.SPACESUIT_HELMET
                || stack.getItem() == ModArmor.SPACESUIT_CHEST
                || stack.getItem() == ModArmor.SPACESUIT_LEGS
                || stack.getItem() == ModArmor.SPACESUIT_BOOTS;
    }
}