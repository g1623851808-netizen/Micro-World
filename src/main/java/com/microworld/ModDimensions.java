package com.microworld;

import com.microworld.world.CosmosDimensionType;
import com.microworld.world.PlanetDimensionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDimensions {

    public static ModDimension COSMOS;
    public static DimensionType COSMOS_TYPE;

    // 存储星球维度：光点索引 -> DimensionType
    public static final Map<Integer, DimensionType> PLANET_TYPES = new HashMap<>();

    @SubscribeEvent
    public static void registerModDimension(RegistryEvent.Register<ModDimension> event) {
        COSMOS = ModDimension.withFactory(CosmosDimensionType::new)
                .setRegistryName("microworld", "cosmos");
        event.getRegistry().register(COSMOS);
        System.out.println("[MicroWorld] ModDimension 注册完成");
    }

    // 动态创建星球维度
    public static DimensionType createPlanetDimension(int planetId, int radius, int planetType) {
        String name = "planet_" + planetId;
        ModDimension modDim = ModDimension.withFactory((world, type) -> new PlanetDimensionType(world, type, radius, planetType));
        modDim.setRegistryName("microworld", name);
        DimensionType type = DimensionManager.registerOrGetDimension(
                new ResourceLocation("microworld", name), modDim, null, true
        );
        PLANET_TYPES.put(planetId, type);
        System.out.println("[MicroWorld] 星球维度创建: " + name + " 类型=" + planetType + " 半径=" + radius);
        return type;
    }
}