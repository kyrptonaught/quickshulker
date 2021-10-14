package net.kyrptonaught.quickshulker.client;

import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public String rawKey;
    public InputUtil.Key keycode;
    public boolean doParseKeycode = true;

    public KeyBinding() {
    }

    public void setRaw(String key) {
        rawKey = key;
        doParseKeycode = true;
        holding = false;
    }

    boolean holding = false;

    public boolean wasPressed() {
        boolean pressed = isKeybindPressed();
        if (!holding) {
            holding = pressed;
            return pressed;
        }
        if (!pressed)
            holding = false;
        return false;
    }

    public void parseKeycode() {
        if (doParseKeycode) {
            keycode = getKeybinding();
            doParseKeycode = false;
        }
    }

    public boolean isKeybindPressed() {
        parseKeycode();
        if (keycode == null) // Invalid key
            return false;
        if (keycode == InputUtil.UNKNOWN_KEY)
            return false;

        if (keycode.getCategory() == InputUtil.Type.MOUSE)
            return GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
        else
            return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;

    }

    public boolean doesMatch(InputUtil.Type type, int code) {
        parseKeycode();
        if (keycode == null) // Invalid key
            return false;
        if (keycode == InputUtil.UNKNOWN_KEY)
            return false;
        return keycode.getCategory() == type && keycode.getCode() == code;
    }

    public InputUtil.Key getKeybinding() {
        if (rawKey == null || rawKey.isEmpty())
            return InputUtil.UNKNOWN_KEY;
        try {
            return InputUtil.fromTranslationKey(rawKey);
        } catch (IllegalArgumentException e) {
            System.out.println(QuickShulkerMod.MOD_ID + ": unknown key entered");
            return InputUtil.UNKNOWN_KEY;
        }
    }
}