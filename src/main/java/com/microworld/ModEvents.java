package com.microworld;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    // 破坏草丛掉落氧气果（15%几率）
    @SubscribeEvent
    public static void onGrassBreak(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;

        if (event.getState().getBlock() == Blocks.GRASS ||
                event.getState().getBlock() == Blocks.TALL_GRASS ||
                event.getState().getBlock() == Blocks.FERN ||
                event.getState().getBlock() == Blocks.LARGE_FERN) {

            if (Math.random() < 0.15) {
                Block.spawnAsEntity(
                        (World) event.getWorld(),
                        event.getPos(),
                        new ItemStack(ModItems.OXYGEN_FRUIT, 1)
                );
            }
        }
    }

    // 破坏树叶掉落弹性藤蔓（8%几率）
    @SubscribeEvent
    public static void onLeafBreak(BlockEvent.BreakEvent event) {
        if (event.getWorld().isRemote()) return;

        if (event.getState().getBlock() == Blocks.OAK_LEAVES ||
                event.getState().getBlock() == Blocks.BIRCH_LEAVES ||
                event.getState().getBlock() == Blocks.SPRUCE_LEAVES ||
                event.getState().getBlock() == Blocks.JUNGLE_LEAVES ||
                event.getState().getBlock() == Blocks.ACACIA_LEAVES ||
                event.getState().getBlock() == Blocks.DARK_OAK_LEAVES) {

            if (Math.random() < 0.08) {
                Block.spawnAsEntity(
                        (World) event.getWorld(),
                        event.getPos(),
                        new ItemStack(ModItems.ELASTIC_VINE, 1)
                );
            }
        }
    }
}