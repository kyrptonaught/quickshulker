package net.kyrptonaught.quickshulker.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.quickshulker.BundleHelper;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class QuickBundlePacket {
    private static final Identifier QUICK_BUNDLE_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "quick_bundle_packet");
    private static final Identifier QUICK_BUNDLEHELD_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "quick_bundleheld_packet");
    private static final Identifier QUICK_UNBUNDLE_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "quick_unbundle_packet");

    public static void registerReceivePacket() {
        ServerPlayNetworking.registerGlobalReceiver(QUICK_BUNDLE_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            if (player.isCreative()) {
                int playerInvSlotID = packetByteBuf.readInt();
                ItemStack stackToBundle = packetByteBuf.readItemStack();
                server.execute(() -> BundleHelper.bundleItemIntoStack(player, player.getInventory().getStack(playerInvSlotID), stackToBundle, null));
            }
        });
        Unbundle.registerReceivePacket();
        BundleIntoHeld.registerReceivePacket();
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(int slotID, ItemStack stackToBundle) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(slotID);
        buf.writeItemStack(stackToBundle);
        ClientPlayNetworking.send(QUICK_BUNDLE_PACKET, new PacketByteBuf(buf));
    }

    public static void sendCreativeSlotUpdate(ItemStack output, Slot slot) {
        MinecraftClient.getInstance().interactionManager.clickCreativeStack(output, slot.id);
    }

    public static class BundleIntoHeld {
        public static void registerReceivePacket() {
            ServerPlayNetworking.registerGlobalReceiver(QUICK_BUNDLEHELD_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
                if (player.isCreative()) {
                    ItemStack stackToBundle = packetByteBuf.readItemStack();
                    ItemStack bundleStack = packetByteBuf.readItemStack();
                    server.execute(() -> BundleHelper.bundleItemIntoStack(player, bundleStack, stackToBundle, null));
                }
            });
        }

        @Environment(EnvType.CLIENT)
        public static void sendPacket(ItemStack stackToBundle, ItemStack bundleStack) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeItemStack(stackToBundle);
            buf.writeItemStack(bundleStack);
            ClientPlayNetworking.send(QUICK_BUNDLEHELD_PACKET, new PacketByteBuf(buf));
        }
    }

    public static class Unbundle {
        public static void registerReceivePacket() {
            ServerPlayNetworking.registerGlobalReceiver(QUICK_UNBUNDLE_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
                if (player.isCreative()) {
                    int playerInvSlotID = packetByteBuf.readInt();
                    ItemStack unbundleStack = packetByteBuf.readItemStack();
                    server.execute(() -> {
                        ItemStack output = BundleHelper.unbundleItem(player, unbundleStack);
                        if (output != null)
                            player.getInventory().setStack(playerInvSlotID, output);
                    });
                }
            });
        }

        @Environment(EnvType.CLIENT)
        public static void sendPacket(int slotID, ItemStack unbundleStack) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(slotID);
            buf.writeItemStack(unbundleStack);
            ClientPlayNetworking.send(QUICK_UNBUNDLE_PACKET, new PacketByteBuf(buf));
        }
    }
}
