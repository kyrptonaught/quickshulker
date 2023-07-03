package net.kyrptonaught.quickshulker.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class OpenQuickiePacket {
    public static final Identifier OPEN_QUICKIE = new Identifier(QuickShulkerMod.MOD_ID, "open_quickie");

    public static void send(ServerPlayerEntity player, ItemStack stack) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeItemStack(stack);
        ServerPlayNetworking.send(player, OPEN_QUICKIE, new PacketByteBuf(buf));
    }
}
