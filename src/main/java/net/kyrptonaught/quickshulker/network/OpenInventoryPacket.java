package net.kyrptonaught.quickshulker.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class OpenInventoryPacket {
    public static final Identifier OPEN_INV = new Identifier(QuickShulkerMod.MOD_ID, "openinv");

    public static void send(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, OPEN_INV, new PacketByteBuf(Unpooled.buffer()));
    }

}
