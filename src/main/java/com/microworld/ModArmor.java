package com.microworld;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder("microworld")
public class ModArmor {

    public static final Item SPACESUIT_HELMET = null;
    public static final Item SPACESUIT_CHEST = null;
    public static final Item SPACESUIT_LEGS = null;
    public static final Item SPACESUIT_BOOTS = null;

    private static final ModArmorMaterial MATERIAL = new ModArmorMaterial();

    @SubscribeEvent
    public static void registerArmor(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new SpacesuitItem(MATERIAL, EquipmentSlotType.HEAD,
                        new Item.Properties().group(ItemGroup.COMBAT))
                        .setRegistryName("microworld", "spacesuit_helmet"),
                new SpacesuitItem(MATERIAL, EquipmentSlotType.CHEST,
                        new Item.Properties().group(ItemGroup.COMBAT))
                        .setRegistryName("microworld", "spacesuit_chest"),
                new SpacesuitItem(MATERIAL, EquipmentSlotType.LEGS,
                        new Item.Properties().group(ItemGroup.COMBAT))
                        .setRegistryName("microworld", "spacesuit_legs"),
                new SpacesuitItem(MATERIAL, EquipmentSlotType.FEET,
                        new Item.Properties().group(ItemGroup.COMBAT))
                        .setRegistryName("microworld", "spacesuit_boots")
        );
        System.out.println("[MicroWorld] 宇航服注册完成");
    }
}