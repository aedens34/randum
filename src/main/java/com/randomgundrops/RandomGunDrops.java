package com.randomgundrops;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(RandomGunDrops.MODID)
public class RandomGunDrops {
    public static final String MODID = "random_gun_drops";

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    public RandomGunDrops() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        LOOT_MODIFIERS.register("random_gun_drops", RandomGunLootModifier.CODEC);
        LOOT_MODIFIERS.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        Network.register();
    }
}
