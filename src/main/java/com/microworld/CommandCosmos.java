package com.microworld;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = "microworld")
public class CommandCosmos {

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent event) {
        CommandDispatcher<net.minecraft.command.CommandSource> dispatcher = event.getCommandDispatcher();

        dispatcher.register(Commands.literal("cosmos")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().asPlayer();
                    MinecraftServer server = player.server;
                    if (ModDimensions.COSMOS_TYPE != null) {
                        ServerWorld cosmosWorld = server.getWorld(ModDimensions.COSMOS_TYPE);
                        if (cosmosWorld != null) {
                            player.teleport(cosmosWorld, 0.5, 200, 0.5, player.rotationYaw, player.rotationPitch);
                        }
                    }
                    return 1;
                })
        );

        dispatcher.register(Commands.literal("overworld")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().asPlayer();
                    ServerWorld overworld = player.server.getWorld(DimensionType.OVERWORLD);
                    if (overworld != null) {
                        player.teleport(overworld, player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                    }
                    return 1;
                })
        );

        dispatcher.register(Commands.literal("findstar")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().asPlayer();
                    List<LightPointGenerator.LightPointData> allPoints = LightPointGenerator.LIGHT_POINTS;
                    if (allPoints.isEmpty()) {
                        ctx.getSource().sendFeedback(new StringTextComponent("光点数据尚未生成"), false);
                        return 0;
                    }
                    ctx.getSource().sendFeedback(new StringTextComponent("宇宙中共有光点: " + allPoints.size() + " 个"), false);

                    LightPointGenerator.LightPointData nearest = allPoints.get(0);
                    double minDist = player.getDistanceSq(nearest.pos.getX(), nearest.pos.getY(), nearest.pos.getZ());
                    for (LightPointGenerator.LightPointData data : allPoints) {
                        double dist = player.getDistanceSq(data.pos.getX(), data.pos.getY(), data.pos.getZ());
                        if (dist < minDist) {
                            minDist = dist;
                            nearest = data;
                        }
                    }
                    player.connection.setPlayerLocation(nearest.pos.getX() + 0.5, nearest.pos.getY() + 2, nearest.pos.getZ() + 0.5, 0, 0);
                    ctx.getSource().sendFeedback(new StringTextComponent("最近光点坐标: " + nearest.pos.getX() + " " + nearest.pos.getY() + " " + nearest.pos.getZ()), false);
                    return 1;
                })
        );
    }
}