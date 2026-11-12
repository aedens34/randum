package com.randomgundrops;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/** Reads every gun TACZ has loaded (base game and all addon gunpacks) through reflection. */
public class GunList {
    private static boolean warned = false;

    public static List<ResourceLocation> ids() {
        List<ResourceLocation> out = new ArrayList<>();
        try {
            Class<?> api = Class.forName("com.tacz.guns.api.TimelessAPI");
            Object result = api.getMethod("getAllCommonGunIndex").invoke(null);
            if (result instanceof Iterable<?> it) {
                for (Object o : it) {
                    if (o instanceof Map.Entry<?, ?> e && e.getKey() instanceof ResourceLocation rl) {
                        out.add(rl);
                    }
                }
            }
        } catch (Throwable t) {
            if (!warned) {
                warned = true;
                System.err.println("[Random Gun Drops] Could not read TACZ's gun list: " + t);
            }
        }
        out.sort(Comparator.comparing(ResourceLocation::toString));
        return out;
    }
}
