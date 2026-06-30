package com.microworld.world;

import com.microworld.PlanetTypeConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;

public class PlanetChunkGenerator extends ChunkGenerator<GenerationSettings> {

    private final int planetRadius;
    private final int planetType;

    public PlanetChunkGenerator(World world, int planetRadius, int planetType) {
        super(world, new PlanetBiomeProvider(), new GenerationSettings());
        this.planetRadius = planetRadius;
        this.planetType = planetType;
    }

    @Override
    public void generateSurface(IChunk chunk) {
        int chunkX = chunk.getPos().x * 16;
        int chunkZ = chunk.getPos().z * 16;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                boolean insidePlanet = (worldX * worldX + worldZ * worldZ) <= planetRadius * planetRadius;
                for (int y = 0; y < 256; y++) {
                    if (insidePlanet) {
                        Block block = PlanetTypeConfig.getBlock(planetType, y, worldX, worldZ);
                        chunk.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), false);
                    } else {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), false);
                    }
                }
            }
        }
    }

    @Override
    public int getGroundHeight() {
        return 64;
    }

    @Override
    public void makeBase(IWorld worldIn, IChunk chunkIn) {
    }

    @Override
    public int func_222529_a(int x, int z, Heightmap.Type type) {
        return 64;
    }

    @Override
    public boolean hasStructure(Biome biomeIn, Structure<?> structureIn) {
        return false;
    }

    @Nullable
    @Override
    public BlockPos findNearestStructure(World worldIn, String name, BlockPos pos, int radius, boolean skipExistingChunks) {
        return null;
    }
}