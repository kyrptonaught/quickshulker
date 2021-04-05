package net.kyrptonaught.quickshulker.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class ConfigOptions implements AbstractConfigFile {
    @Comment("Activation key")
    public String keybinding = "key.keyboard.p";
    @Comment("Right Clicking with shulker in hand opens it")
    public boolean rightClickToOpen = true;
    @Comment("Hitting the keybind with shulker in hand opens it")
    public boolean keybind = true;
    @Comment("Hitting the keybind while hovering over shulker in inv opens it")
    public boolean keybingInInv = true;
    @Comment("Right Clicking a shulker in your inv opens it")
    public boolean rightClickInv = true;
    @Comment("Right Clicking a shulker in a chest opens it (requires rightClickInv)")
    public boolean rightClickInChest = false;
}
