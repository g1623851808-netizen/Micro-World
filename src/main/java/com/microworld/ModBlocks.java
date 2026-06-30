package com.microworld;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder("microworld")
public class ModBlocks {

    public static final Block ALUMINUM_ORE = null;
    public static final Block TITANIUM_ORE = null;
    public static final Block DARK_ENERGY_ORE = null;
    public static final Block OXYGEN_GENERATOR = null;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new OreBlock(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(3.0f, 3.0f))
                        .setRegistryName("microworld", "aluminum_ore"),
                new OreBlock(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(5.0f, 5.0f))
                        .setRegistryName("microworld", "titanium_ore"),
                new OreBlock(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(5.0f, 5.0f)
                        .lightValue(7))
                        .setRegistryName("microworld", "dark_energy_ore"),
                new OxygenGeneratorBlock(Block.Properties.create(Material.IRON)
                        .hardnessAndResistance(5.0f, 6.0f)
                        .sound(SoundType.METAL))
                        .setRegistryName("microworld", "oxygen_generator")
        );
        System.out.println("[MicroWorld] 方块注册完成");
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new BlockItem(ALUMINUM_ORE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                        .setRegistryName("microworld", "aluminum_ore"),
                new BlockItem(TITANIUM_ORE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                        .setRegistryName("microworld", "titanium_ore"),
                new BlockItem(DARK_ENERGY_ORE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                        .setRegistryName("microworld", "dark_energy_ore"),
                new BlockItem(OXYGEN_GENERATOR, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                        .setRegistryName("microworld", "oxygen_generator")
        );
        System.out.println("[MicroWorld] 方块物品注册完成");
    }
}