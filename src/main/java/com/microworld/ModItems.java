package com.microworld;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder("microworld")
public class ModItems {

    // 材料
    public static final Item ALUMINUM_INGOT = null;
    public static final Item TITANIUM_INGOT = null;
    public static final Item DARK_ENERGY_SHARD = null;
    public static final Item OXYGEN_FRUIT = null;
    public static final Item ELASTIC_VINE = null;
    public static final Item COMPRESSED_CARBON = null;
    public static final Item SEALED_GLASS = null;
    public static final Item PROPELLANT = null;
    public static final Item ASTRO_FUEL = null;
    public static final Item STARGATE_KEY = null;
    public static final Item ROCKET = null;
    public static final Item SPACE_BUILDER = null;
    public static final Item PLANET_LOCATOR = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "aluminum_ingot"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "titanium_ingot"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "dark_energy_shard"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "oxygen_fruit"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "elastic_vine"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "compressed_carbon"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "sealed_glass"),
                new Item(new Item.Properties().group(ItemGroup.MATERIALS))
                        .setRegistryName("microworld", "propellant"),
                new Item(new Item.Properties().group(ItemGroup.BREWING).maxStackSize(16))
                        .setRegistryName("microworld", "astro_fuel"),
                new StargateKeyItem(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1))
                        .setRegistryName("microworld", "stargate_key"),
                new RocketItem(new Item.Properties().group(ItemGroup.TRANSPORTATION).maxStackSize(1))
                        .setRegistryName("microworld", "rocket"),
                new SpaceBuilderItem(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(64))
                        .setRegistryName("microworld", "space_builder"),
                new PlanetLocatorItem(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1))
                        .setRegistryName("microworld", "planet_locator")
        );
        System.out.println("[MicroWorld] 物品注册完成");
    }
}