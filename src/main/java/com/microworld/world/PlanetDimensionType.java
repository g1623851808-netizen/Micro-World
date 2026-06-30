package com.microworld.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class PlanetDimensionType extends Dimension {

    private final int planetRadius;
    private final int planetType;

    public PlanetDimensionType(World world, DimensionType type, int planetRadius, int planetType) {
        super(world, type);
        this.planetRadius = planetRadius;
        this.planetType = planetType;
    }

    @Override
    public ChunkGenerator<? extends GenerationSettings> createChunkGenerator() {
        return new PlanetChunkGenerator(this.world, planetRadius, planetType);
    }

    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos chunkPos, boolean checkValid) {
        return new BlockPos(0, 66, 0);
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int x, int z, boolean checkValid) {
        return new BlockPos(x, 66, z);
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.5f;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return new Vec3d(0.0, 0.0, 0.05);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }
}