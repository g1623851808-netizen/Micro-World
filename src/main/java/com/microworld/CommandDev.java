package com.microworld;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = "microworld")
public class CommandDev {

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent event) {
        CommandDispatcher<net.minecraft.command.CommandSource> dispatcher = event.getCommandDispatcher();

        dispatcher.register(Commands.literal("setenergy")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, 50))
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().asPlayer();
                            ItemStack held = player.getHeldItemMainhand();
                            if (held.getItem() != ModItems.STARGATE_KEY) {
                                ctx.getSource().sendFeedback(new StringTextComponent("请手持星门秘钥"), false);
                                return 0;
                            }
                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                            StargateKeyItem.setDarkEnergy(held, amount);
                            ctx.getSource().sendFeedback(new StringTextComponent("暗能量已设置为 " + amount), false);
                            return 1;
                        })
                )
        );

        dispatcher.register(Commands.literal("setfuel")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, 100))
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().asPlayer();
                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                            int count = 0;
                            EquipmentSlotType[] slots = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
                            for (EquipmentSlotType slot : slots) {
                                ItemStack stack = player.getItemStackFromSlot(slot);
                                if (stack.getItem() instanceof SpacesuitItem) {
                                    ArmorFuelUtil.setFuel(stack, amount);
                                    count++;
                                }
                            }
                            ctx.getSource().sendFeedback(new StringTextComponent("已设置 " + count + " 件宇航服燃料为 " + amount), false);
                            return 1;
                        })
                )
        );
    }
}