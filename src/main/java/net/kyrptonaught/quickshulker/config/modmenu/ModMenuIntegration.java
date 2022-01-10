package net.kyrptonaught.quickshulker.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.KeybindItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.SubItem;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.config.ConfigOptions;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = QuickShulkerMod.getConfig();

            ConfigScreen configScreen = new ConfigScreen(screen, new TranslatableText("Quick Shulker Config"));
            configScreen.setSavingEvent(() -> {
                QuickShulkerMod.config.save();
            });
            ConfigSection activationSection = new ConfigSection(configScreen, new TranslatableText("key.quickshulker.config.category.activation"));
            activationSection.addConfigItem(new KeybindItem(new TranslatableText("key.quickshulker.config.keybinding"), options.keybinding.rawKey, ConfigOptions.defualtKeybind).setSaveConsumer(value -> options.keybinding.setRaw(value)));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.keybind"), options.keybind, true).setSaveConsumer(value -> options.keybind = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClick"), options.rightClickToOpen, true).setSaveConsumer(value -> options.rightClickToOpen = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.keybindInInv"), options.keybingInInv, true).setSaveConsumer(value -> options.keybingInInv = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClickInInv"), options.rightClickInv, true).setSaveConsumer(value -> options.rightClickInv = value));

            ConfigSection optionsSection = new ConfigSection(configScreen, new TranslatableText("key.quickshulker.config.category.options"));
            optionsSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClickClose"), options.rightClickClose, false).setSaveConsumer(value -> options.rightClickClose = value));

            SubItem subItem = (SubItem) optionsSection.addConfigItem(new SubItem(new TranslatableText("key.quickshulker.config.category.bundleing"), true));
            subItem.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.supportsBundlingInsert"), options.supportsBundlingInsert, true).setSaveConsumer(value -> options.supportsBundlingInsert = value));
            subItem.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.supportsBundlingPickup"), options.supportsBundlingPickup, true).setSaveConsumer(value -> options.supportsBundlingPickup = value));
            subItem.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.supportsBundlingExtract"), options.supportsBundlingExtract, true).setSaveConsumer(value -> options.supportsBundlingExtract = value));

            ConfigSection enabledSection = new ConfigSection(configScreen, new TranslatableText("key.quickshulker.config.category.enabled"));
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickShulkerBox"), options.quickShulkerBox, true).setSaveConsumer(value -> options.quickShulkerBox = value).setRequiresRestart());
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickCraftingTable"), options.quickCraftingTables, true).setSaveConsumer(value -> options.quickCraftingTables = value).setRequiresRestart());
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickStonecutter"), options.quickStonecutter, true).setSaveConsumer(value -> options.quickStonecutter = value).setRequiresRestart());
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickEChest"), options.quickEChest, true).setSaveConsumer(value -> options.quickEChest = value).setRequiresRestart());

            return configScreen;
        };
    }
}