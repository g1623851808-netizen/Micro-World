package com.microworld;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DarkEnergyEvents {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) return;
        if (event.phase != TickEvent.Phase.END) return;

        // 每20分钟（24000 tick）恢复1点暗能量
        tickCounter++;
        if (tickCounter >= 1200) {
            tickCounter = 0;

            // 遍历所有玩家
            for (PlayerEntity player : event.world.getPlayers()) {
                // 主世界不恢复
                if (player.world.getDimension().getType() == net.minecraft.world.dimension.DimensionType.OVERWORLD) {
                    continue;
                }

                // 遍历背包找星门秘钥
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if (stack.getItem() == ModItems.STARGATE_KEY) {
                        int energy = StargateKeyItem.getDarkEnergy(stack);
                        if (energy < StargateKeyItem.MAX_DARK_ENERGY) {
                            StargateKeyItem.addDarkEnergy(stack, 1);
                        }
                    }
                }
            }
        }
    }
}