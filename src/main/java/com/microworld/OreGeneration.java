package com.microworld;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreGeneration {

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            if (biome.getCategory() == Biome.Category.NETHER ||
                    biome.getCategory() == Biome.Category.THEEND) {
                continue;
            }

            // 铝矿
            biome.addFeature(
                    GenerationStage.Decoration.UNDERGROUND_ORES,
                    Biome.createDecoratedFeature(
                            Feature.ORE,
                            new OreFeatureConfig(
                                    OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                    ModBlocks.ALUMINUM_ORE.getDefaultState(),
                                    4
                            ),
                            Placement.COUNT_RANGE,
                            new CountRangeConfig(8, 10, 0, 60)
                    )
            );

            // 钛矿
            biome.addFeature(
                    GenerationStage.Decoration.UNDERGROUND_ORES,
                    Biome.createDecoratedFeature(
                            Feature.ORE,
                            new OreFeatureConfig(
                                    OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                    ModBlocks.TITANIUM_ORE.getDefaultState(),
                                    3
                            ),
                            Placement.COUNT_RANGE,
                            new CountRangeConfig(4, 5, 0, 30)
                    )
            );

            // 暗能量水晶矿
            biome.addFeature(
                    GenerationStage.Decoration.UNDERGROUND_ORES,
                    Biome.createDecoratedFeature(
                            Feature.ORE,
                            new OreFeatureConfig(
                                    OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                                    ModBlocks.DARK_ENERGY_ORE.getDefaultState(),
                                    2
                            ),
                            Placement.COUNT_RANGE,
                            new CountRangeConfig(2, 1, 0, 15)
                    )
            );
        }
        System.out.println("[MicroWorld] 矿石生成注册完成");
    }
}