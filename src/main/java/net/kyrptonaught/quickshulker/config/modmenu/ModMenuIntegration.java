package net.kyrptonaught.quickshulker.config.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.Util;
import net.kyrptonaught.quickshulker.config.ConfigOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;

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
            ConfigOptions options = QuickShulkerMod.config.getConfig();
            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Quick Shulker Config");
            builder.setSavingRunnable(() -> {
                QuickShulkerMod.config.saveConfig();
                Util.keycode = null;
            });
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory category = builder.getOrCreateCategory("key.quickshulker.config.category.activation");
            category.addEntry(entryBuilder.startKeyCodeField("key.quickshulker.config.keybinding", InputUtil.fromName(options.keybinding)).setSaveConsumer(key -> options.keybinding = key.getName()).build());
            category.addEntry(entryBuilder.startBooleanToggle("key.quickshulker.config.keybind", options.keybind).setSaveConsumer(val -> options.keybind = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle("key.quickshulker.config.rightClick", options.rightClickToOpen).setSaveConsumer(val -> options.rightClickToOpen = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle("key.quickshulker.config.keybindInInv", options.keybingInInv).setSaveConsumer(val -> options.keybingInInv = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle("key.quickshulker.config.rightClickInInv", options.rightClickInv).setSaveConsumer(val -> options.rightClickInv = val).setDefaultValue(true).build());

            return builder.build();
        };
    }
}