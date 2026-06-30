package com.microworld;

import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DimensionBlocker {

    // 阻止点燃地狱门
    @SubscribeEvent
    public static void onFireUse(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        // 阻止用打火石点燃黑曜石框架
        if (event.getItemStack().getItem() == Items.FLINT_AND_STEEL) {
            // 检查点击的方块是否是黑曜石，周围是否有地狱门结构
            if (world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                event.setCanceled(true);
                if (!world.isRemote) {
                    player.sendMessage(new StringTextComponent("地狱维度已被封印"));
                }
            }
        }

        // 阻止用末影之眼激活末地传送门
        if (event.getItemStack().getItem() == Items.ENDER_EYE) {
            if (world.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME) {
                event.setCanceled(true);
                if (!world.isRemote) {
                    player.sendMessage(new StringTextComponent("末地维度已被封印"));
                }
            }
        }
    }

    // 阻止地狱门自然生成
    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

    // 阻止玩家通过已有地狱门进入
    @SubscribeEvent
    public static void onPlayerEnterPortal(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        if (world.getBlockState(pos).getBlock() instanceof NetherPortalBlock) {
            event.setCanceled(true);
        }
    }
}