package com.microworld;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

/**
 * 星球类型配置
 * 添加新星球类型只需在这里加一行
 */
public class PlanetTypeConfig {

    // 每种星球类型的方块生成规则
    public static final PlanetRule[] RULES = new PlanetRule[12];

    // 初始化所有类型
    static {
        // 类型0：主世界光点（不可进入，不需要规则）
        RULES[0] = new PlanetRule("主世界光点", 0xFFFFFF, null);

        // 类型1：生命世界 - 草地
        RULES[1] = new PlanetRule("生命世界", 0x00FF00,
                new BlockLayer()
                        .add(0, 60, Blocks.STONE)      // 0~60层：石头
                        .add(60, 65, Blocks.DIRT)      // 60~65层：泥土
                        .add(65, 66, Blocks.GRASS_BLOCK) // 65层：草方块
        );

        // 类型2：矿物世界 - 矿石丰富
        RULES[2] = new PlanetRule("矿物世界", 0xFF8800,
                new BlockLayer()
                        .add(0, 64, Blocks.STONE)       // 石基
                        .addOre(0, 64, Blocks.IRON_ORE, 0.1f)   // 10%铁矿
                        .addOre(0, 40, Blocks.GOLD_ORE, 0.05f)  // 5%金矿
                        .addOre(0, 20, Blocks.DIAMOND_ORE, 0.02f) // 2%钻石
        );

        // 类型3：沙漠世界 - 全沙
        RULES[3] = new PlanetRule("沙漠世界", 0xFFFF00,
                new BlockLayer()
                        .add(0, 60, Blocks.SANDSTONE)
                        .add(60, 65, Blocks.SAND)
        );

        // 类型4：水世界 - 海洋
        RULES[4] = new PlanetRule("水世界", 0x0088FF,
                new BlockLayer()
                        .add(0, 50, Blocks.STONE)
                        .add(50, 64, Blocks.WATER)
        );

        // 类型5：岩浆世界
        RULES[5] = new PlanetRule("岩浆世界", 0xFF0000,
                new BlockLayer()
                        .add(0, 50, Blocks.NETHERRACK)
                        .add(50, 60, Blocks.LAVA)
        );

        // 类型6：末影世界
        RULES[6] = new PlanetRule("末影世界", 0xAA00FF,
                new BlockLayer()
                        .add(0, 60, Blocks.END_STONE)
        );

        // 类型7：冰冻世界
        RULES[7] = new PlanetRule("冰冻世界", 0xFFFFFF,
                new BlockLayer()
                        .add(0, 60, Blocks.STONE)
                        .add(60, 64, Blocks.ICE)
                        .add(64, 65, Blocks.SNOW_BLOCK)
        );

        // 类型8：暗影世界
        RULES[8] = new PlanetRule("暗影世界", 0x444444,
                new BlockLayer()
                        .add(0, 60, Blocks.OBSIDIAN)
        );

        // 类型9：泥土世界
        RULES[9] = new PlanetRule("泥土世界", 0x8B4513,
                new BlockLayer()
                        .add(0, 65, Blocks.DIRT)
        );

        // 类型10：天空世界
        RULES[10] = new PlanetRule("天空世界", 0x00FFFF,
                new BlockLayer()
                        .addPattern(0, 256, Blocks.AIR) // 全是空气（需要特殊处理空岛）
        );

        // 类型11：混合世界
        RULES[11] = new PlanetRule("混合世界", 0xFF00FF,
                new BlockLayer()
                        .add(0, 60, Blocks.STONE)
                        .add(60, 65, Blocks.DIRT)
                        .add(65, 66, Blocks.GRASS_BLOCK)
        );
    }

    /**
     * 根据高度获取应该生成的方块
     */
    public static Block getBlock(int planetType, int y, int x, int z) {
        if (planetType < 0 || planetType >= RULES.length) return Blocks.AIR;
        PlanetRule rule = RULES[planetType];
        if (rule == null || rule.layers == null) return Blocks.AIR;
        return rule.layers.getBlock(y, x, z);
    }

    /**
     * 星球规则（名称+颜色+层）
     */
    public static class PlanetRule {
        public final String name;
        public final int color;
        public final BlockLayer layers;

        public PlanetRule(String name, int color, BlockLayer layers) {
            this.name = name;
            this.color = color;
            this.layers = layers;
        }
    }

    /**
     * 方块层配置
     * 支持：
     * - add(最低y, 最高y, 方块)  固定层
     * - addOre(最低y, 最高y, 方块, 概率)  随机矿石
     */
    public static class BlockLayer {
        private final java.util.List<LayerEntry> entries = new java.util.ArrayList<>();

        public BlockLayer add(int minY, int maxY, Block block) {
            entries.add(new LayerEntry(minY, maxY, block, 1.0f, false));
            return this;
        }

        public BlockLayer addOre(int minY, int maxY, Block block, float chance) {
            entries.add(new LayerEntry(minY, maxY, block, chance, true));
            return this;
        }

        public BlockLayer addPattern(int minY, int maxY, Block block) {
            entries.add(new LayerEntry(minY, maxY, block, 1.0f, false));
            return this;
        }

        public Block getBlock(int y, int x, int z) {
            for (LayerEntry entry : entries) {
                if (y >= entry.minY && y < entry.maxY) {
                    if (entry.isOre) {
                        return (Math.random() < entry.chance) ? entry.block : Blocks.STONE;
                    }
                    return entry.block;
                }
            }
            return Blocks.AIR;
        }

        private static class LayerEntry {
            final int minY, maxY;
            final Block block;
            final float chance;
            final boolean isOre;

            LayerEntry(int minY, int maxY, Block block, float chance, boolean isOre) {
                this.minY = minY;
                this.maxY = maxY;
                this.block = block;
                this.chance = chance;
                this.isOre = isOre;
            }
        }
    }
}