package net.kyrptonaught.quickshulker.api;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class QuickOpenableRegistry{
     public static HashMap<Block, BiConsumer<PlayerEntity, ItemStack>> consumers = new HashMap<>();

    public static void register(Block block, BiConsumer<PlayerEntity, ItemStack> consumer) {
        consumers.put(block, consumer);
    }
}
