package fr.heavenmoon.core.common.format.message;

import net.md_5.bungee.api.ChatColor;

public enum PrefixType {

    NONE("none", ""),
    SERVER("server", ChatColor.DARK_GRAY + "(" + ChatColor.DARK_PURPLE + "Heaven" + ChatColor.LIGHT_PURPLE + "Moon" + ChatColor.DARK_GRAY + ")" + ChatColor.GRAY + " "),
    MODO("modo", ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + "Modo" + ChatColor.DARK_GRAY + ")" + ChatColor.LIGHT_PURPLE + " "),
    ADMIN("admin", ChatColor.DARK_GRAY + "(" + ChatColor.RED + "Admin" + ChatColor.DARK_GRAY + ")" + ChatColor.RED + " "),
    ERROR("error", ChatColor.RED + "[Erreur] ");

    private String name;
    private String content;

    PrefixType(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
