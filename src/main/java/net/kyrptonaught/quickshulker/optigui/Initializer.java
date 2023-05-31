package net.kyrptonaught.quickshulker.optigui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import opekope2.optigui.EntryPoint;
import opekope2.optigui.InitializerContext;
import opekope2.optigui.interaction.InteractionTarget;
import opekope2.optigui.properties.DefaultProperties;
import opekope2.optigui.service.InteractionService;
import opekope2.optigui.service.RegistryLookupService;
import opekope2.optigui.service.Services;
import org.jetbrains.annotations.NotNull;

public class Initializer implements EntryPoint {
    @Override
    public void onInitialize(@NotNull InitializerContext initializerContext) {
        InteractionWrapper.setImpl(new InteractionWrapper() {
            private final InteractionService interactionService = Services.getService(InteractionService.class);

            @Override
            public void interact(@NotNull PlayerEntity player, @NotNull World world, @NotNull Hand hand, @NotNull ItemStack item) {
                interactionService.interact(player, world, hand, new InteractionTarget.Preprocessed(getInteractionTargetData(item)), null);
            }
        });
    }

    private static Object getInteractionTargetData(ItemStack stack) {
        MinecraftClient client = MinecraftClient.getInstance();
        RegistryLookupService lookup = Services.getService(RegistryLookupService.class);

        Identifier biome = lookup.lookupBiomeId(client.world, client.player.getBlockPos());
        String name = stack.hasCustomName() ? stack.getName().getString() : null;

        return new DefaultProperties(
                Registries.ITEM.getId(stack.getItem()),
                name,
                biome,
                client.player.getBlockY()
        );
    }
}
