package com.microworld;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = "microworld", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LightPointGenerator {

    private static final int TOTAL_POINTS = 300;
    private static final int MIN_SPACING = 1000;
    private static final int XZ_RANGE = 50000;
    private static final int Y_MIN = 28;
    private static final int Y_MAX = 228;

    // 光点数据（坐标+类型）
    public static final List<LightPointData> LIGHT_POINTS = new ArrayList<>();

    public static class LightPointData {
        public final BlockPos pos;
        public final int type;

        public LightPointData(BlockPos pos, int type) {
            this.pos = pos;
            this.type = type;
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getPlayer().world.isRemote) return;
        if (event.getTo() != ModDimensions.COSMOS_TYPE) return;
        triggerGeneration((ServerWorld) event.getPlayer().world);
    }

    public static void triggerGeneration(ServerWorld world) {
        LightPointSaveData data = getSaveData(world);
        if (!data.isGenerated()) {
            generatePoints(world.getSeed(), data);
        }
        // 同步到静态列表
        LIGHT_POINTS.clear();
        LIGHT_POINTS.addAll(data.getPoints());
    }

    public static void generatePoints(long seed, LightPointSaveData data) {
        Random random = new Random(seed + 42);
        List<LightPointData> points = new ArrayList<>();
        data.getPoints().clear();

        // 主世界光点，类型0
        points.add(new LightPointData(new BlockPos(0, 128, 0), 0));
        data.addPoint(new BlockPos(0, 128, 0), 0);

        int attempts = 0;
        int maxAttempts = TOTAL_POINTS * 200;
        while (points.size() < TOTAL_POINTS && attempts < maxAttempts) {
            attempts++;
            int x = random.nextInt(XZ_RANGE * 2) - XZ_RANGE;
            int y = Y_MIN + random.nextInt(Y_MAX - Y_MIN + 1);
            int z = random.nextInt(XZ_RANGE * 2) - XZ_RANGE;

            boolean tooClose = false;
            for (LightPointData existing : points) {
                double dx = existing.pos.getX() - x;
                double dy = existing.pos.getY() - y;
                double dz = existing.pos.getZ() - z;
                if (Math.sqrt(dx * dx + dy * dy + dz * dz) < MIN_SPACING) {
                    tooClose = true;
                    break;
                }
            }
            if (tooClose) continue;

            int type = getRandomType(random);
            BlockPos pos = new BlockPos(x, y, z);
            points.add(new LightPointData(pos, type));
            data.addPoint(pos, type);
        }

        data.setGenerated(true);
        System.out.println("[MicroWorld] 光点生成完成，共 " + points.size() + " 个");
    }
//星球生成概率
    private static int getRandomType(Random random) {
        int r = random.nextInt(100);
        if (r < 8) return 1;   // 生命 8%
        if (r < 23) return 2;  // 矿物 15%
        if (r < 35) return 3;  // 沙漠 12%
        if (r < 47) return 4;  // 水 12%
        if (r < 57) return 5;  // 岩浆 10%
        if (r < 65) return 6;  // 末影 8%
        if (r < 75) return 7;  // 冰冻 10%
        if (r < 83) return 8;  // 暗影 8%
        if (r < 93) return 9;  // 泥土 10%
        if (r < 98) return 10; // 天空 5%
        return 11;             // 混合 2%
    }

    private static LightPointSaveData getSaveData(ServerWorld world) {
        return world.getSavedData().getOrCreate(LightPointSaveData::new, "microworld_lightpoints");
    }

    public static class LightPointSaveData extends WorldSavedData {
        private boolean generated = false;
        private final List<LightPointData> points = new ArrayList<>();

        public LightPointSaveData() {
            super("microworld_lightpoints");
        }

        public boolean isGenerated() {
            return generated;
        }

        public void setGenerated(boolean generated) {
            this.generated = generated;
            markDirty();
        }

        public List<LightPointData> getPoints() {
            return points;
        }

        public void addPoint(BlockPos pos, int type) {
            points.add(new LightPointData(pos, type));
            markDirty();
        }

        @Override
        public void read(CompoundNBT nbt) {
            generated = nbt.getBoolean("Generated");
            points.clear();
            if (nbt.contains("Points")) {
                CompoundNBT pointsTag = nbt.getCompound("Points");
                for (String key : pointsTag.keySet()) {
                    CompoundNBT entry = pointsTag.getCompound(key);
                    int[] posArr = entry.getIntArray("Pos");
                    int type = entry.getInt("Type");
                    if (posArr.length == 3) {
                        points.add(new LightPointData(new BlockPos(posArr[0], posArr[1], posArr[2]), type));
                    }
                }
            }
        }

        @Override
        public CompoundNBT write(CompoundNBT nbt) {
            nbt.putBoolean("Generated", generated);
            CompoundNBT pointsTag = new CompoundNBT();
            for (int i = 0; i < points.size(); i++) {
                LightPointData data = points.get(i);
                CompoundNBT entry = new CompoundNBT();
                entry.putIntArray("Pos", new int[]{data.pos.getX(), data.pos.getY(), data.pos.getZ()});
                entry.putInt("Type", data.type);
                pointsTag.put(String.valueOf(i), entry);
            }
            nbt.put("Points", pointsTag);
            return nbt;
        }
    }
}