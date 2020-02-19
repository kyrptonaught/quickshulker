package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.api.QuickOpenable;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderChestBlock.class)
public class EnderChestMixin implements QuickOpenable {

    @Override
    public void quickShulker$QuickAccess(PlayerEntity playerEntity, ItemStack usedStack) {
        playerEntity.openContainer(new SimpleNamedContainerFactory((i, playerInventory, player) ->
                GenericContainer.createGeneric9x3(i, playerInventory, player.getEnderChestInventory()), new TranslatableText("container.enderchest")));
    }
}
