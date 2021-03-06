package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.init.IngotVariantTiers;
import com.example.ttmlm.item.tools.capabilities.ESLCapability;
import com.example.ttmlm.item.tools.capabilities.IEnderStorageLink;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class EnderTouchModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected EnderTouchModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        /*
            For Chest Linking, Used a capability that Stores the NBT data of a chest tile entity then wrote deserializers
            to convert the NBT back into the original Chest tile entity. Used IItemhandler to put items into chests.
         */
        if (!context.getLevel().isClientSide) {
            ItemStack toolItemStack = context.getParamOrNull(LootParameters.TOOL);
            if (((ToolItem) toolItemStack.getItem()).getTier() == IngotVariantTiers.ENDER) {
                LazyOptional<IEnderStorageLink> toolOptional = toolItemStack.getCapability(ESLCapability.ENDER_STORAGE_LINK_CAPABILITY, null);
                IEnderStorageLink linker = toolOptional.orElseThrow(Error::new);
                if (linker.getContainer(context.getLevel()) != null) {
                    ChestTileEntity container = linker.getContainer(context.getLevel());
                    LazyOptional<IItemHandler> containerOptional = container.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                    IItemHandler itemHandler = containerOptional.orElseThrow(Error::new);

                    int maxSlot = itemHandler.getSlots();
                    ItemStack remainder;
                    int j = 0;
                    for (int i = 0; i < maxSlot && generatedLoot.size() > 0; i++) {
                        remainder = itemHandler.insertItem(i, generatedLoot.get(j), false);
                        if (remainder.isEmpty()) {
                            generatedLoot.remove(j);
                            j++;
                        }
                    }
                }
            }
            Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                PlayerInventory inv = player.inventory;
                generatedLoot.removeIf(inv::add);
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<EnderTouchModifier> {
        @Override
        public EnderTouchModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new EnderTouchModifier(conditionsIn);
        }

        @Override
        public JsonObject write(EnderTouchModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
