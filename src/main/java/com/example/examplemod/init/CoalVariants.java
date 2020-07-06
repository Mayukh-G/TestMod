package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.OreBlocks;
import com.example.examplemod.item.CoalVariantsItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.LazyValue;

import java.util.Locale;

public enum CoalVariants {
    BLAZING_CARBON,
    FREEZING_CARBON;

    private final LazyValue<OreBlocks> oreBlocks;
    private final LazyValue<Block> coalVariantBlock;
    private final LazyValue<Item> coalVariantItem;
    /*

    -FIGURE OUT HOW TO MAKE COAL INTO FUEL ITEM
        To do this override the method getBurnTime and make it return a burn time properly
        0 for non burn -1 for default vanilla logic
         **** Didn't Work Try Something Else

     */

    CoalVariants() {
        oreBlocks = new LazyValue<>(OreBlocks::new);
        coalVariantBlock = new LazyValue<>(() -> new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(3f,10f)
                .sound(SoundType.STONE)
        ));
        coalVariantItem = new LazyValue<>(() -> new CoalVariantsItems(this.getName(), new Item.Properties()));
    }

    public String getName() { return name().toLowerCase(Locale.ROOT); }

    public OreBlocks getOreBlocks(){ return oreBlocks.getValue(); }

    public Block getCoalVariantBlock() { return coalVariantBlock.getValue(); }

    public Item getCoalVariantItem(){ return coalVariantItem.getValue(); }

}
