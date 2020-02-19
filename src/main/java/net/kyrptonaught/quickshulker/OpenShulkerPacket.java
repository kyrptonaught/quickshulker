package net.kyrptonaught.quickshulker;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class OpenShulkerPacket {
    private static final Identifier OPEN_SHULKER_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "open_shulker_packet");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(OPEN_SHULKER_PACKET, (packetContext, packetByteBuf) -> {
            int invSlot = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                Item item = player.inventory.getInvStack(invSlot).getItem();
                if (((BlockItem) item).getBlock() instanceof QuickOpenable)
                    ((QuickOpenable) ((BlockItem) item).getBlock()).quickShulker$QuickAccess(player, player.inventory.getInvStack(invSlot));
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendOpenPacket(int invSlot) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(invSlot);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(OPEN_SHULKER_PACKET, new PacketByteBuf(buf)));
    }
}