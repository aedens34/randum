package com.randomgundrops;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder B = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLED =
            B.comment("Turn random gun drops on or off.").define("enabled", true);
    public static final ForgeConfigSpec.BooleanValue PER_BLOCK =
            B.comment("true = each block type always gives the same gun. false = a new random gun on every break.")
                    .define("perBlockMapping", true);
    public static final ForgeConfigSpec.IntValue CHANCE =
            B.comment("Chance (percent) that a broken block drops a gun.").defineInRange("dropChancePercent", 100, 0, 100);
    public static final ForgeConfigSpec.BooleanValue KEEP_NORMAL =
            B.comment("Also drop the block's normal items next to the gun.").define("keepNormalDrops", false);
    public static final ForgeConfigSpec.LongValue SALT =
            B.comment("Changes which gun each block type maps to.").defineInRange("mappingSalt", 0L, Long.MIN_VALUE, Long.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = B.build();

    public static void apply(SettingsPacket p) {
        ENABLED.set(p.enabled());
        PER_BLOCK.set(p.perBlock());
        CHANCE.set(Math.max(0, Math.min(100, p.chance())));
        KEEP_NORMAL.set(p.keepNormal());
        SALT.set(p.salt());
        try {
            SPEC.save();
        } catch (Exception ignored) {
        }
    }
}
