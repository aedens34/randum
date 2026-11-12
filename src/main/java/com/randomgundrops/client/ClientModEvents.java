package com.randomgundrops.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.randomgundrops.RandomGunDrops;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public class ClientModEvents {
    public static final KeyMapping OPEN_MENU = new KeyMapping(
            "key.random_gun_drops.menu", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.random_gun_drops");

    @Mod.EventBusSubscriber(modid = RandomGunDrops.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(OPEN_MENU);
        }
    }

    @Mod.EventBusSubscriber(modid = RandomGunDrops.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            while (OPEN_MENU.consumeClick()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.screen == null && mc.player != null) {
                    mc.setScreen(new DropsScreen());
                }
            }
        }
    }
}
