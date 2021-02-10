package fr.heavenmoon.core.common.format.style;

import net.md_5.bungee.api.ChatColor;

public enum StyleList {

    AQUA(ChatColor.AQUA, "b"),
    MAGIC(ChatColor.MAGIC, "k"),
    BOLD(ChatColor.BOLD, "l"),
    STRIKE(ChatColor.STRIKETHROUGH, "m"),
    UNDERLINE(ChatColor.UNDERLINE, "n"),
    ITALIC(ChatColor.ITALIC, "o"),
    RESET(ChatColor.WHITE, "r"),
    BLACK(ChatColor.BLACK, "0"),
    BLUE(ChatColor.BLUE, "9"),
    DARK_AQUA(ChatColor.DARK_AQUA, "3"),
    DARK_BLUE(ChatColor.DARK_BLUE, "1"),
    DARK_GRAY(ChatColor.DARK_GRAY, "8"),
    DARK_GREEN(ChatColor.DARK_GREEN, "3"),
    DARK_PURPLE(ChatColor.DARK_PURPLE, "5"),
    DARK_RED(ChatColor.DARK_RED, "4"),
    GOLD(ChatColor.GOLD, "6"),
    GRAY(ChatColor.GRAY, "7"),
    GREEN(ChatColor.GREEN, "a"),
    PURPLE(ChatColor.LIGHT_PURPLE, "d"),
    RED(ChatColor.RED, "c"),
    WHITE(ChatColor.WHITE, "f"),
    YELLOW(ChatColor.YELLOW, "e");

    private ChatColor color;
    private String code;

    StyleList(ChatColor color, String code) {
        this.color = color;
        this.code = code;
    }

    public static StyleList get(String code) {
        for (StyleList value : StyleList.values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getCode() {
        return code;
    }

}
