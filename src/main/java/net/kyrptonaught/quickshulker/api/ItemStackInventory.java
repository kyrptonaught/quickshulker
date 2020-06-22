package net.kyrptonaught.quickshulker.api;

import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;


public class ItemStackInventory extends SimpleInventory {
    private ItemStack itemStack;
    private int SIZE;

    public ItemStackInventory(ItemStack stack, int SIZE) {
        super(getStacks(stack, SIZE).toArray(new ItemStack[SIZE]));
        itemStack = stack;
        this.SIZE = SIZE;
    }

    private static DefaultedList<ItemStack> getStacks(ItemStack usedStack, int SIZE) {
        CompoundTag compoundTag = usedStack.getSubTag("BlockEntityTag");
        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        if (compoundTag != null && compoundTag.contains("Items", 9)) {
            Inventories.fromTag(compoundTag, itemStacks);
        }
        return itemStacks;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        CompoundTag compoundTag = itemStack.getSubTag("BlockEntityTag");
        if (isEmpty()) {
            itemStack.removeSubTag("BlockEntityTag");
            return;
        } else if (compoundTag == null) {
            compoundTag = itemStack.getOrCreateSubTag("BlockEntityTag");
        }

        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        for (int i = 0; i < size(); i++) {
            itemStacks.set(i, getStack(i));
        }
        Inventories.toTag(compoundTag, itemStacks);
    }

    @Override
    public void onClose(PlayerEntity playerEntity_1) {
        markDirty();
        itemStack.getOrCreateSubTag(QuickShulkerMod.MOD_ID).remove("opened");
    }
}