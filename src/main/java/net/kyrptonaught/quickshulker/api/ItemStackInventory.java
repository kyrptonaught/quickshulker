package net.kyrptonaught.quickshulker.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

public class ItemStackInventory extends BasicInventory {

    private ItemStack itemStack;
    private int SIZE;

    public ItemStackInventory(ItemStack stack, int SIZE) {
        super(getStacks(stack).toArray(new ItemStack[SIZE]));
        itemStack = stack;
        this.SIZE = SIZE;
    }

    private static DefaultedList<ItemStack> getStacks(ItemStack usedStack) {
        CompoundTag compoundTag = usedStack.getSubTag("BlockEntityTag");
        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
        if (compoundTag != null && compoundTag.contains("Items", 9)) {
            Inventories.fromTag(compoundTag, itemStacks);
        }
        return itemStacks;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        CompoundTag compoundTag = itemStack.getSubTag("BlockEntityTag");
        if (isInvEmpty()) {
            itemStack.removeSubTag("BlockEntityTag");
            return;
        } else if (compoundTag == null) {
            compoundTag = itemStack.getOrCreateSubTag("BlockEntityTag");
        }

        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        for (int i = 0; i < getInvSize(); i++)
            itemStacks.set(i, getInvStack(i));
        Inventories.toTag(compoundTag, itemStacks);
    }

    @Override
    public void onInvClose(PlayerEntity playerEntity_1) {
        markDirty();
    }
}