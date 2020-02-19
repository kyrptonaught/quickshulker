package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenable;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxMixin implements QuickOpenable {
    @Override
    public void quickShulker$QuickAccess(PlayerEntity player, ItemStack usedStack) {
        player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                new ShulkerBoxContainer(i, player.inventory, new ItemStackInventory(usedStack, 27)), new TranslatableText("container.shulkerBox")));
    }
}
