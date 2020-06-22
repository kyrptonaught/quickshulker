package net.kyrptonaught.quickshulker.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class QuickOpenableRegistry {
    public static HashMap<Class, BiConsumer<PlayerEntity, ItemStack>> consumers = new HashMap<>();

    public static void register(Class block, BiConsumer<PlayerEntity, ItemStack> consumer) {
        consumers.put(block, consumer);
    }

    public static void register(BiConsumer<PlayerEntity, ItemStack> consumer, Class... blocks) {
        for (Class block : blocks) {
            register(block, consumer);
        }
    }
}
