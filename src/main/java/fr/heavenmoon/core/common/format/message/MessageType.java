package fr.heavenmoon.core.common.format.message;


import net.md_5.bungee.api.ChatColor;

public enum MessageType {

    PERMISSION("PERMISSION", ChatColor.RED + "Vous n'avez pas la permission d'éxécuter cette commande."),
    CONSOLE("CONSOLE", ChatColor.RED + "Vous ne pouvez pas exectuer cette commande depuis la console."),
    SERVER_DOWN("SERVER_DOWN", ChatColor.RED + "Le serveur est éteind."),
    SERVER_RESTART("SERVER_RESTART", ChatColor.RED + "Le serveur va redémarrer."),
    SYNTAXE("SYNTAXE", ChatColor.RED + "Commande incorrect. Essayez avec: %syntax%."),
    NO_PLAYER("NO_PLAYER", ChatColor.RED + "Le joueur %player% n'existe pas.");

    private String name;
    private String content;

    MessageType(String name, String content) {
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
