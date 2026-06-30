package com.microworld.world;

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

public class CosmosChunkGenerator extends ChunkGenerator<GenerationSettings> {

    public CosmosChunkGenerator(World world) {
        super(world, new CosmosBiomeProvider(), new GenerationSettings());
    }

    @Override
    public void generateSurface(IChunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), false);
                }
            }
        }
    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public void makeBase(IWorld worldIn, IChunk chunkIn) {
    }

    @Override
    public int func_222529_a(int x, int z, Heightmap.Type type) {
        return 0;
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