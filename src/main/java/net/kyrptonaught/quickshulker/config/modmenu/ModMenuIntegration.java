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
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = QuickShulkerMod.getConfig();
            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("Quick Shulker Config"));
            builder.setSavingRunnable(() -> {
                QuickShulkerMod.config.save();
                QuickShulkerModClient.quickKey.setRaw(options.keybinding);
            });
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("key.quickshulker.config.category.activation"));
            category.addEntry(entryBuilder.startKeyCodeField(new TranslatableText("key.quickshulker.config.keybinding"), QuickShulkerModClient.quickKey.getKeybinding()).setSaveConsumer(key -> options.keybinding = key.toString()).setDefaultValue(InputUtil.fromTranslationKey(ConfigOptions.defualtKeybind)).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.keybind"), options.keybind).setSaveConsumer(val -> options.keybind = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.rightClick"), options.rightClickToOpen).setSaveConsumer(val -> options.rightClickToOpen = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.keybindInInv"), options.keybingInInv).setSaveConsumer(val -> options.keybingInInv = val).setDefaultValue(true).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.rightClickInInv"), options.rightClickInv).setSaveConsumer(val -> options.rightClickInv = val).setDefaultValue(true).build());

            ConfigCategory optionsCat = builder.getOrCreateCategory(new TranslatableText("key.quickshulker.config.category.options"));
            optionsCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.rightClickClose"), options.rightClickClose).setSaveConsumer(val -> options.rightClickClose = val).setDefaultValue(false).build());
            optionsCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.supportsBundlingInsert"), options.supportsBundlingInsert).setSaveConsumer(val -> options.supportsBundlingInsert = val).setDefaultValue(true).build());

            ConfigCategory enabledCat = builder.getOrCreateCategory(new TranslatableText("key.quickshulker.config.category.enabled"));
            enabledCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.quickShulkerBox"), options.quickShulkerBox).setSaveConsumer(val -> options.quickShulkerBox = val).setDefaultValue(true).requireRestart().build());
            enabledCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.quickCraftingTable"), options.quickCraftingTables).setSaveConsumer(val -> options.quickCraftingTables = val).setDefaultValue(true).requireRestart().build());
            enabledCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.quickStonecutter"), options.quickStonecutter).setSaveConsumer(val -> options.quickStonecutter = val).setDefaultValue(true).requireRestart().build());
            enabledCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.quickshulker.config.quickEChest"), options.quickEChest).setSaveConsumer(val -> options.quickEChest = val).setDefaultValue(true).requireRestart().build());

            return builder.build();
        };
    }
}