package net.kyrptonaught.quickshulker.config;

import blue.endless.jankson.Comment;

public class ConfigOptions {
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
}
