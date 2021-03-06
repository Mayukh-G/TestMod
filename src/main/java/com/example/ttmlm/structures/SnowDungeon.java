package com.example.ttmlm.structures;

import com.example.ttmlm.TTMLM;
import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;


public class SnowDungeon extends Structure<NoFeatureConfig> {
    public SnowDungeon(Codec<NoFeatureConfig> codec){
        super(codec);
    }

//    @NotNull
//    @Override
//    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
//        int maxDistance = 15;
//        int minDistance = 10;
//
//        int xTemp = x + maxDistance * spacingOffsetsX;
//        int ztemp = z + maxDistance * spacingOffsetsZ;
//        int xTemp2 = xTemp < 0 ? xTemp - maxDistance + 1 : xTemp;
//        int zTemp2 = ztemp < 0 ? ztemp - maxDistance + 1 : ztemp;
//        int validChunkX = xTemp2 / maxDistance;
//        int validChunkZ = zTemp2 / maxDistance;
//
//        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), validChunkX, validChunkZ, this.getSeedModifier());
//        validChunkX = validChunkX * maxDistance;
//        validChunkZ = validChunkZ * maxDistance;
//        validChunkX = validChunkX + random.nextInt(maxDistance - minDistance);
//        validChunkZ = validChunkZ + random.nextInt(maxDistance - minDistance);
//
//        return new ChunkPos(validChunkX, validChunkZ);
//    }

//    @Override
//    public boolean canBeGenerated(@NotNull BiomeManager biomeManagerIn, @NotNull ChunkGenerator<?> generatorIn, @NotNull Random randIn, int chunkX, int chunkZ, @NotNull Biome biomeIn) {
//        ChunkPos chunkpos = this.getStartPositionForPosition(generatorIn, randIn, chunkX, chunkZ, 0, 0);
//        if(chunkpos.x == chunkX && chunkpos.z == chunkZ){
//            if(randIn.nextInt(10) <= 3){
//                return generatorIn.hasStructure(biomeIn, this);
//            }
//        }
//        return false;
//    }

    @NotNull
    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return SnowDungeon.Start::new;
    }

    @NotNull
    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(@NotNull DynamicRegistries p_230364_1_, @NotNull ChunkGenerator p_230364_2_, @NotNull TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, @NotNull Biome p_230364_6_, @NotNull NoFeatureConfig p_230364_7_) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];

            int x = (p_230364_4_ << 4) + 7;
            int z = (p_230364_5_ << 4) + 7;

            int surfaceY = p_230364_2_.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);
            SnowDungeonPieces.start(p_230364_3_, blockpos, rotation, this.pieces, this.random);

            this.calculateBoundingBox();
            if (TTMLM.isDevBuild()) TTMLM.LOGGER.log(Level.DEBUG, "SnowDungeon at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));

        }
    }
}
