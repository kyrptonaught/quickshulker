package net.kyrptonaught.quickshulker.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class OpenShulkerPacket {
    private static final Identifier OPEN_SHULKER_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "open_shulker_packet");

    public static void registerReceivePacket() {
        ServerPlayNetworking.registerGlobalReceiver(OPEN_SHULKER_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int invSlot = packetByteBuf.readInt();
	        try{
	            if(PlayerInventory.isValidHotbarIndex(player.currentScreenHandler.slots.get(invSlot).getIndex()) || Util.isOpenableItem(player.getOffHandStack())){
					return;
	            }
			}
			catch (Exception e){
				return;
			}
            server.execute(() -> Util.openItem(player, invSlot));
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendOpenPacket(int invSlot) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(invSlot);
        ClientPlayNetworking.send(OPEN_SHULKER_PACKET, new PacketByteBuf(buf));
    }
}
