package com.microworld;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModDimensionEvents {

    @SubscribeEvent
    public static void onRegisterDimensions(RegisterDimensionsEvent event) {
        ModDimensions.COSMOS_TYPE = DimensionManager.registerOrGetDimension(
                new ResourceLocation("microworld", "cosmos"),
                ModDimensions.COSMOS,
                null,
                true
        );
        System.out.println("[MicroWorld] DimensionType 注册完成: " + ModDimensions.COSMOS_TYPE);
    }
}