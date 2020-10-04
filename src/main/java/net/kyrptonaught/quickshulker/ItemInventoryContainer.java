package net.kyrptonaught.quickshulker;

import net.minecraft.item.ItemStack;

public interface ItemInventoryContainer {
    ItemStack getOpenedItem();

    default boolean hasItem() {
        return getOpenedItem() != null;
    }

    void setOpenedItem(ItemStack openedItem);


}
