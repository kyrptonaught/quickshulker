package net.kyrptonaught.quickshulker;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class OpenShulkerPacket {
    private static final Identifier OPEN_SHULKER_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "open_shulker_packet");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(OPEN_SHULKER_PACKET, (packetContext, packetByteBuf) -> {
            int invSlot = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                if (player.inventory.getInvStack(invSlot).getItem().equals(Items.ENDER_CHEST)) {
                    player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                            GenericContainer.createGeneric9x3(i, playerInventory, player.getEnderChestInventory()), new TranslatableText("container.enderchest")));
                } else
                    player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                            Util.getContainer(i, playerEntity, invSlot), new TranslatableText("container.shulkerBox")));
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