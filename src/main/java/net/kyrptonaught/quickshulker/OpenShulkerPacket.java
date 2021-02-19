package net.kyrptonaught.quickshulker;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.kyrptonaught.quickshulker.api.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class OpenShulkerPacket {
    private static final Identifier OPEN_SHULKER_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "open_shulker_packet");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(OPEN_SHULKER_PACKET, (packetContext, packetByteBuf) -> {
            int type = packetByteBuf.readInt();
            int invSlot = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> {
                Util.openItem(packetContext.getPlayer(), invSlot, type);
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendOpenPacket(int invSlot, int type) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(type);
        buf.writeInt(invSlot);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(OPEN_SHULKER_PACKET, new PacketByteBuf(buf)));
    }
}