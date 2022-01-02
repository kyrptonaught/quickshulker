package net.kyrptonaught.quickshulker.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class QuickOpenableRegistry {
    public static HashMap<Class, QuickShulkerData> quickies = new HashMap<>();

    public static void register(Class block, QuickShulkerData quickShulkerData) {
        quickies.put(block, quickShulkerData);
    }

    @Deprecated
    public static void register(Class block, Boolean requiresSingularStack, Boolean supportsBundleing, BiConsumer<PlayerEntity, ItemStack> consumer) {
        register(block, new QuickShulkerData(consumer, supportsBundleing));
    }

    public static void register(Class block, Boolean supportsBundleing, BiConsumer<PlayerEntity, ItemStack> consumer) {
        register(block, new QuickShulkerData(consumer, supportsBundleing));
    }

    public static void register(Class block, BiConsumer<PlayerEntity, ItemStack> consumer) {
        register(block, new QuickShulkerData(consumer, false));
    }

    public static void register(BiConsumer<PlayerEntity, ItemStack> consumer, Class... blocks) {

        for (Class block : blocks) {
            register(block, consumer);
        }
    }

    public static class Builder {
        private Class[] blocks;
        private final QuickShulkerData qsdata;

        public Builder() {
            qsdata = new QuickShulkerData();
        }

        public void register() {
            for (Class block : blocks)
                QuickOpenableRegistry.register(block, qsdata);
        }

        public Builder setBlock(Class block) {
            this.blocks = new Class[]{block};
            return this;
        }

        public Builder setBlocks(Class... blocks) {
            this.blocks = blocks;
            return this;
        }

        public Builder setOpenAction(BiConsumer<PlayerEntity, ItemStack> openAction) {
            qsdata.openConsumer = openAction;
            return this;
        }

        public Builder supportsBundleing(Boolean supportsBundleing) {
            qsdata.supportsBundleing = supportsBundleing;
            return this;
        }

        public Builder ignoreSingleStackCheck(Boolean ignoreSingleStackCheck) {
            qsdata.ignoreSingleStackCheck = ignoreSingleStackCheck;
            return this;
        }
    }
}