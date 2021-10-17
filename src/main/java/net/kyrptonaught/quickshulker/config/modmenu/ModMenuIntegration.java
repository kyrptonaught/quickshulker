package net.kyrptonaught.quickshulker.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.client.QuickShulkerModClient;
import net.kyrptonaught.quickshulker.config.ConfigOptions;
import net.kyrptonaught.quickshulker.config.screen.items.BooleanItem;
import net.kyrptonaught.quickshulker.config.screen.ConfigScreen;
import net.kyrptonaught.quickshulker.config.screen.ConfigSection;
import net.kyrptonaught.quickshulker.config.screen.items.DoubleItem;
import net.kyrptonaught.quickshulker.config.screen.items.IntegerItem;
import net.kyrptonaught.quickshulker.config.screen.items.KeybindItem;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
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
                QuickShulkerModClient.quickKey.setRaw(options.keybinding);
            });
            ConfigSection activationSection = new ConfigSection(configScreen, new TranslatableText("key.quickshulker.config.category.activation"));
            activationSection.addConfigItem(new KeybindItem(new TranslatableText("key.quickshulker.config.keybinding"), options.keybinding, ConfigOptions.defualtKeybind).setSaveConsumer(value -> options.keybinding = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.keybind"), options.keybind, true).setSaveConsumer(value -> options.keybind = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClick"), options.rightClickToOpen, true).setSaveConsumer(value -> options.rightClickToOpen = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.keybindInInv"), options.keybingInInv, true).setSaveConsumer(value -> options.keybingInInv = value));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClickInInv"), options.rightClickInv, true).setSaveConsumer(value -> options.rightClickInv = value));

            ConfigSection optionsSection = new ConfigSection(configScreen, new TranslatableText("key.quickshulker.config.category.options"));
            optionsSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClickClose"), options.rightClickClose, false).setSaveConsumer(value -> options.rightClickClose = value));
            optionsSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.supportsBundlingInsert"), options.supportsBundlingInsert, true).setSaveConsumer(value -> options.supportsBundlingInsert = value));

            ConfigSection enabledSection = new ConfigSection(configScreen, new TranslatableText("key.quickshulker.config.category.enabled"));
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickShulkerBox"), options.quickShulkerBox, true).setSaveConsumer(value -> options.quickShulkerBox = value));
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickCraftingTable"), options.quickCraftingTables, true).setSaveConsumer(value -> options.quickCraftingTables = value));
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickStonecutter"), options.quickStonecutter, true).setSaveConsumer(value -> options.quickStonecutter = value));
            enabledSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.quickEChest"), options.quickEChest, true).setSaveConsumer(value -> options.quickEChest = value));

            activationSection.addConfigItem(new IntegerItem(new LiteralText("Number of kids in my basement"), 5, 0).setMinMax(-5, 10));
            activationSection.addConfigItem(new DoubleItem(new LiteralText("Number of kids in my basement 2"), 5.0d, 5d).setMinMax(-5d, 10d));
            activationSection.addConfigItem(new BooleanItem(new TranslatableText("key.quickshulker.config.rightClickInInv"), options.rightClickInv, true).setSaveConsumer(value -> options.rightClickInv = value));

            new ConfigSection(configScreen, new LiteralText("yooooooooooooooo"));
            return configScreen;
        };
    }
}