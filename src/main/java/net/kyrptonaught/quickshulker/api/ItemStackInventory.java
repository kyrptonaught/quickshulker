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

    public ItemStackInventory(ItemStack stack, DefaultedList<ItemStack> stacks) {
        super(stacks.toArray(new ItemStack[stacks.size()]));
        itemStack = stack;
        this.SIZE = stacks.size();
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