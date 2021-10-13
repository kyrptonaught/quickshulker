package net.kyrptonaught.quickshulker;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class QuickBundlePacket {
    private static final Identifier QUICK_BUNDLE_PACKET = new Identifier(QuickShulkerMod.MOD_ID, "quick_bundle_packet");

    static void registerReceivePacket() {
        ServerPlayNetworking.registerGlobalReceiver(QUICK_BUNDLE_PACKET, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int slotID = packetByteBuf.readInt();
            ItemStack cursorStack = packetByteBuf.readItemStack();
            server.execute(() -> {
                int invSlot = player.currentScreenHandler.slots.get(slotID).getIndex();
                ItemStack stack = player.getInventory().getStack(invSlot);
                System.out.println("pre: " + slotID + " " + invSlot + " " + stack + " " + cursorStack + " ");
                ItemStack output = bundleItem(player, stack, cursorStack);
                System.out.println("post: " + invSlot + " " + stack + " " + cursorStack + " " + output);
                System.out.println(stack.getNbt() + " " + player.getInventory().getStack(invSlot).getNbt());

                player.currentScreenHandler.getSlot(slotID).setStack(stack);
                
                if (output != null) {
                    player.currentScreenHandler.setCursorStack(output);
                    player.currentScreenHandler.syncState();
                    player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
                }
            });
        });
    }

    public static ItemStack bundleItem(PlayerEntity player, ItemStack bundle, ItemStack bundlingItem) {
        Inventory bundlingInv = Util.getQuickItemInventory(player, bundle);
        if (bundlingInv != null && !ShulkerUtils.isShulkerItem(bundlingItem)) {
            try (Transaction transaction = Transaction.openOuter()) {
                long amount = InventoryStorage.of(bundlingInv, null).insert(ItemVariant.of(bundlingItem), bundlingItem.getCount(), transaction);
                transaction.commit();
                bundlingItem.decrement((int) amount);
                bundlingInv.onClose(player);
                return bundlingItem;
            }
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(int slotID, ItemStack curseStack) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(slotID);
        buf.writeItemStack(curseStack);
        ClientPlayNetworking.send(QUICK_BUNDLE_PACKET, new PacketByteBuf(buf));
    }
}
