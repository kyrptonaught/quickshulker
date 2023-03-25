package net.kyrptonaught.quickshulker.optigui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class InteractionWrapper {
    private static final InteractionWrapper dummy = new InteractionWrapper() {
        @Override
        public void interact(@NotNull PlayerEntity player, @NotNull World world, @NotNull Hand hand, @NotNull ItemStack item) {
        }
    };
    private static InteractionWrapper impl = dummy;

    public static InteractionWrapper getImpl() {
        return impl;
    }

    public static void setImpl(InteractionWrapper impl) {
        InteractionWrapper.impl = impl != null ? impl : dummy;
    }

    public abstract void interact(@NotNull PlayerEntity player, @NotNull World world, @NotNull Hand hand, @NotNull ItemStack item);
}
