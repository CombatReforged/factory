package com.combatreforged.factory.api.world.item.container.menu;

import java.util.Objects;

public class Button {
    public static final Button
            LEFT_CLICK = new Button("LEFT_CLICK"),
            RIGHT_CLICK = new Button("RIGHT_CLICK"),
            MIDDLE_CLICK = new Button("MIDDLE_CLICK"),
            DROP = new Button("DROP"),
            NONE = new Button("NONE");

    private final String id;
    private final boolean isHotkey;
    private final int hotkeyTarget;

    public static Button hotkey(int hotkeyTarget) {
        return new Button(hotkeyTarget);
    }

    private Button(String id) {
        this.id = id;
        this.isHotkey = false;
        this.hotkeyTarget = -1;
    }

    private Button(int hotkey) {
        this.isHotkey = true;
        this.hotkeyTarget = hotkey;
        this.id = "HOTKEY_" + hotkey;
    }

    public boolean isHotkey() {
        return isHotkey;
    }

    public int getHotkeyTarget() {
        return hotkeyTarget;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Button)) {
            return false;
        }
        Button button = (Button) obj;
        return button.isHotkey && this.isHotkey
                ? button.hotkeyTarget == this.hotkeyTarget
                : super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.isHotkey
                ? Objects.hash(this.hotkeyTarget, true)
                : Objects.hash(super.hashCode(), false);
    }

    @Override
    public String toString() {
        return id;
    }
}
