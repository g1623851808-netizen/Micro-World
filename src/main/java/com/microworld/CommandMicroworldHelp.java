package com.microworld;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = "microworld")
public class CommandMicroworldHelp {

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent event) {
        CommandDispatcher<net.minecraft.command.CommandSource> dispatcher = event.getCommandDispatcher();

        dispatcher.register(Commands.literal("microworld")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.GOLD + "===== 微观宇宙 Mod 指令列表 ====="), false);
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.AQUA + "/cosmos" + TextFormatting.WHITE + " - 进入宇宙维度"), false);
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.AQUA + "/overworld" + TextFormatting.WHITE + " - 返回主世界"), false);
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.AQUA + "/findstar" + TextFormatting.WHITE + " - 传送到最近的光点"), false);
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.AQUA + "/setenergy <数值>" + TextFormatting.WHITE + " - 设置手持星门秘钥的暗能量（0~50）"), false);
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.AQUA + "/setfuel <数值>" + TextFormatting.WHITE + " - 设置当前装备宇航服的燃料（0~100）"), false);
                    ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.GRAY + "星球定位器：手持右键在宇宙中搜索光点（需星门秘钥+50暗能量）"), false);
                    return 1;
                })
        );
    }
}