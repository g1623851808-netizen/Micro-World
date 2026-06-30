package com.microworld;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class OxygenGeneratorCache {

    public static final List<BlockPos> GENERATORS = new ArrayList<>();

    public static void add(BlockPos pos) {
        if (!GENERATORS.contains(pos)) {
            GENERATORS.add(pos);
        }
    }

    public static void remove(BlockPos pos) {
        GENERATORS.remove(pos);
    }

    public static boolean isNearGenerator(BlockPos playerPos) {
        for (BlockPos genPos : GENERATORS) {
            int dx = Math.abs(playerPos.getX() - genPos.getX());
            int dz = Math.abs(playerPos.getZ() - genPos.getZ());
            if (dx <= 32 && dz <= 32) {
                return true;
            }
        }
        return false;
    }
}