package net.kyrptonaught.quickshulker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

public class ShulkerInventory extends BasicInventory {

    private ItemStack shulkerStack;

    public ShulkerInventory(ItemStack stack, DefaultedList<ItemStack> stacks) {
        super(stacks.toArray(new ItemStack[stacks.size()]));
        shulkerStack = stack;
    }


    @Override
    public void markDirty() {
        super.markDirty();
        CompoundTag compoundTag = shulkerStack.getSubTag("BlockEntityTag");
        if (isInvEmpty()) {
            shulkerStack.removeSubTag("BlockEntityTag");
            return;
        } else if (compoundTag == null) {
            compoundTag = shulkerStack.getOrCreateSubTag("BlockEntityTag");
        }

        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
        for (int i = 0; i < getInvSize(); i++)
            itemStacks.set(i, getInvStack(i));
        Inventories.toTag(compoundTag, itemStacks);
    }

    @Override
    public void onInvClose(PlayerEntity playerEntity_1) {
        markDirty();
    }
}