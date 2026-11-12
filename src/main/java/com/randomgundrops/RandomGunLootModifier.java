package com.randomgundrops;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/** Replaces the drops of broken blocks with a random TACZ gun. Chests and mobs are left alone. */
public class RandomGunLootModifier extends LootModifier {
    public static final Supplier<Codec<RandomGunLootModifier>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, RandomGunLootModifier::new)));

    public RandomGunLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        if (!Config.ENABLED.get() || loot.isEmpty()) return loot;
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null) return loot;                       // not a block drop

        if (context.getRandom().nextInt(100) >= Config.CHANCE.get()) return loot;

        List<ResourceLocation> guns = GunList.ids();
        if (guns.isEmpty()) return loot;
        Item gunItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tacz", "modern_kinetic_gun"));
        if (gunItem == null || gunItem == Items.AIR) return loot;

        ResourceLocation gunId;
        if (Config.PER_BLOCK.get()) {
            ResourceLocation blockKey = ForgeRegistries.BLOCKS.getKey(state.getBlock());
            long seed = (blockKey == null ? 0 : blockKey.toString().hashCode()) * 31L + Config.SALT.get();
            gunId = guns.get(new Random(seed).nextInt(guns.size()));
        } else {
            gunId = guns.get(context.getRandom().nextInt(guns.size()));
        }

        ItemStack gun = new ItemStack(gunItem);
        CompoundTag tag = gun.getOrCreateTag();
        tag.putString("GunId", gunId.toString());

        ObjectArrayList<ItemStack> result = new ObjectArrayList<>();
        result.add(gun);
        if (Config.KEEP_NORMAL.get()) result.addAll(loot);
        return result;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
