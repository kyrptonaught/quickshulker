package net.kyrptonaught.quickshulker.config.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.client.ClientUtil;
import net.kyrptonaught.quickshulker.config.ConfigOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return QuickShulkerMod.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = QuickShulkerMod.getConfig();
            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("Quick Shulker Config"));
            builder.setSavingRunnable(() -> {
                QuickShulkerMod.config.save();
                ClientUtil.keycode = null;
            });
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("key.quickshulker.config.category.activation"));
            category.addEntry(entryBuilder.startKeyCodeField(new TranslatableText("key.quickshulker.config.keybinding"), InputUtil.fromTranslationKey(options.keybinding)).setSaveConsumer(key -> options.keybinding = key.toString()).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.keybind"), options.keybind).setSaveConsumer(val -> options.keybind = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.rightClick"), options.rightClickToOpen).setSaveConsumer(val -> options.rightClickToOpen = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.keybindInInv"), options.keybingInInv).setSaveConsumer(val -> options.keybingInInv = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.rightClickInInv"), options.rightClickInv).setSaveConsumer(val -> options.rightClickInv = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.rightClickInChest"), options.rightClickInChest).setSaveConsumer(val -> options.rightClickInChest = val).setDefaultValue(false).build());

            return builder.build();
        };
    }
}