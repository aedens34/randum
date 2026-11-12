package com.randomgundrops;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public record SettingsPacket(boolean enabled, boolean perBlock, int chance, boolean keepNormal, long salt) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(enabled);
        buf.writeBoolean(perBlock);
        buf.writeVarInt(chance);
        buf.writeBoolean(keepNormal);
        buf.writeLong(salt);
    }

    public static SettingsPacket decode(FriendlyByteBuf buf) {
        return new SettingsPacket(buf.readBoolean(), buf.readBoolean(), buf.readVarInt(), buf.readBoolean(), buf.readLong());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        c.enqueueWork(() -> {
            ServerPlayer player = c.getSender();
            if (player == null) return;
            boolean allowed = player.hasPermissions(2) || player.server.isSingleplayerOwner(player.getGameProfile());
            if (!allowed) {
                player.sendSystemMessage(Component.literal("You need operator permission to change Random Gun Drops settings."));
                return;
            }
            Config.apply(this);
            player.sendSystemMessage(Component.literal("Random Gun Drops settings saved."));
        });
        c.setPacketHandled(true);
    }
}
