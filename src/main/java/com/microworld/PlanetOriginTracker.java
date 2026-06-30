package com.microworld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 记录玩家从哪个光点进入星球世界
 */
public class PlanetOriginTracker {
    private static final Map<UUID, Integer> ORIGIN_MAP = new HashMap<>();

    public static void setOrigin(UUID playerId, int lightPointIndex) {
        ORIGIN_MAP.put(playerId, lightPointIndex);
    }

    public static Integer getOrigin(UUID playerId) {
        return ORIGIN_MAP.get(playerId);
    }

    public static void removeOrigin(UUID playerId) {
        ORIGIN_MAP.remove(playerId);
    }

    // 获取来源光点坐标（如果在范围内）
    public static net.minecraft.util.math.BlockPos getOriginPos(UUID playerId) {
        Integer index = getOrigin(playerId);
        if (index != null && index >= 0 && index < LightPointGenerator.LIGHT_POINTS.size()) {
            return LightPointGenerator.LIGHT_POINTS.get(index).pos;
        }
        return null;
    }
}