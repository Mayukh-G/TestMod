package com.example.examplemod;

import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModItems;
import com.example.examplemod.init.WorldGenOres;
import com.example.examplemod.item.weapons.IngotVariantSwords;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
//@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class SideProxy {
    SideProxy() {
        //Life-cycle events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::loadComplete);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerALL);


        //Other
        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::onAttackVariantSword);
    }

    private static void commonSetup(FMLCommonSetupEvent event){
        ExampleMod.LOGGER.debug("commonSetup Test log");
    }
    private static void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private static void processIMC(final InterModProcessEvent event) {

    }
    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event){
        
    }

    public static void loadComplete(FMLLoadCompleteEvent event){
        //Generate ores
        WorldGenOres.onInitBiomesGen();
    }

    public static void onAttackVariantSword(LivingAttackEvent event){
        Object attacker = event.getSource().getTrueSource();
        if(attacker instanceof LivingEntity){
            LivingEntity Entityattacker = (LivingEntity)attacker;
            ItemStack HeldItemStack = Entityattacker.getHeldItemMainhand();
            Item HeldItem = HeldItemStack.getItem();
            if(HeldItem instanceof IngotVariantSwords){
                ((IngotVariantSwords) HeldItem).onAttack(event.getEntityLiving());
            }
        }
    }

    static class Client extends  SideProxy {
        Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::clientSetup);
        }

        private static void clientSetup(FMLClientSetupEvent event) {

        }
    }

    static class Server extends SideProxy {
        Server() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Server::serverSetup);
        }

        private static void serverSetup(FMLDedicatedServerSetupEvent event) {

        }
    }
}
