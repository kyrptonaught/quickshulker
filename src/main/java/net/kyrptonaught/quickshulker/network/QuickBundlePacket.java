package net.kyrptonaught.quickshulker.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class QuickBundlePacket {
    private static final Identifier QUICK_BUNDLE_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "quick_bundle_packet");
    private static final Identifier QUICK_UNBUNDLE_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "quick_unbundle_packet");

    public static void registerReceivePacket() {
        ServerPlayNetworking.registerGlobalReceiver(QUICK_BUNDLE_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int slotID = packetByteBuf.readInt();
            ItemStack stackToBundle = packetByteBuf.readItemStack();
            server.execute(() -> bundleItem(player, player.getInventory().getStack(slotID), stackToBundle));
        });
        Unbundle.registerReceivePacket();
    }

    public static ItemStack bundleItem(PlayerEntity player, ItemStack bundle, ItemStack bundlingItem) {
        Inventory bundlingInv = Util.getQuickItemInventory(player, bundle);
        if (bundlingInv != null && !ShulkerUtils.isShulkerItem(bundlingItem)) {
            try (Transaction transaction = Transaction.openOuter()) {
                long amount = InventoryStorage.of(bundlingInv, null).insert(ItemVariant.of(bundlingItem), bundlingItem.getCount(), transaction);
                if (amount == 0) return null;
                transaction.commit();
                bundlingItem.decrement((int) amount);
                bundlingInv.onClose(player);
                return bundlingItem;
            }
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(int slotID, ItemStack bundleStack) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(slotID);
        buf.writeItemStack(bundleStack);
        ClientPlayNetworking.send(QUICK_BUNDLE_PACKET, new PacketByteBuf(buf));
    }

    public static void sendCreativeSlotUpdate(ItemStack output, Slot slot) {
        MinecraftClient.getInstance().interactionManager.clickCreativeStack(output, slot.id);
    }

    public static class Unbundle {

        public static void registerReceivePacket() {
            ServerPlayNetworking.registerGlobalReceiver(QUICK_UNBUNDLE_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
                int slotID = packetByteBuf.readInt();
                ItemStack unbundleStack = packetByteBuf.readItemStack();
                server.execute(() -> {
                    Inventory inv = Util.getQuickItemInventory(player, unbundleStack);
                    for (int i = inv.size() - 1; i >= 0; i--) {
                        if (!inv.getStack(i).isEmpty()) {
                            player.getInventory().setStack(slotID, inv.removeStack(i));
                            inv.onClose(player);
                            return;
                        }
                    }
                });
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
