package net.kyrptonaught.quickshulker.optigui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class InteractionWrapper {
    private static InteractionWrapper _instance;

    public static InteractionWrapper getInstance() {
        return _instance;
    }

    public static void setInstance(@NotNull InteractionWrapper instance) {
        if (_instance != null) {
            throw new IllegalStateException("Cannot set InteractionWrapper instance after being set.");
        }
        _instance = instance;
    }

    public abstract void startInteraction(@NotNull PlayerEntity player, @NotNull World world, @NotNull Hand hand, @NotNull ItemStack item);
}
