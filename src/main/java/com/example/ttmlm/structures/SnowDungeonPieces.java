package com.example.ttmlm.structures;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.StructureInit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SnowDungeonPieces {
    private static final ResourceLocation SNOW_DUNGEON = TTMLM.getID("snow/snow_dungeon");
    private static final ResourceLocation BASE = TTMLM.getID("snow/snow_base");

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random){

        int x = pos.getX();
        int z = pos.getZ();
        int y = pos.getY();

        BlockPos rotationOffset = new BlockPos(0,0,0).rotate(rotation);
        BlockPos blockPos = rotationOffset.offset(x, y, z);
        pieceList.add(new SnowDungeonPieces.Piece(templateManager, SNOW_DUNGEON, blockPos, rotation));

        rotationOffset = new BlockPos(3, -6, 3).rotate(rotation);
        blockPos = rotationOffset.offset(x, y, z);
        pieceList.add(new SnowDungeonPieces.Piece(templateManager, BASE, blockPos, rotation));

    }

    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation resourceLocation;
        private Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(StructureInit.TTStructures.SDP, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = new BlockPos(0, 1, 0);
            this.templatePosition = pos.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound)
        {
            super(StructureInit.TTStructures.SDP, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        private void setupPiece(TemplateManager templateManager)
        {
            Template template = templateManager.getOrCreate(this.resourceLocation);
            PlacementSettings placementsettings = new PlacementSettings().setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void addAdditionalSaveData(CompoundNBT tagCompound)
        {
            super.addAdditionalSaveData(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(@NotNull String p_186175_1_, @NotNull BlockPos p_186175_2_, @NotNull IServerWorld p_186175_3_, @NotNull Random p_186175_4_, MutableBoundingBox p_186175_5_) {

        }

//        @Override
//        public boolean create(@NotNull IWorld worldIn, ChunkGenerator<?> p_225577_2_, @NotNull Random randomIn, @NotNull MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPos)
//        {
//            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
//            BlockPos blockpos = new BlockPos(0,1,0);
//            this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(-blockpos.getX(), 0, -blockpos.getZ())));
//
//            return super.create(worldIn, p_225577_2_, randomIn, structureBoundingBoxIn, chunkPos);
//        }

    }
}
