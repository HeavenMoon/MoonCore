package fr.heavenmoon.core.bukkit.format;

import fr.heavenmoon.core.common.MoonCommons;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    private String prefix;
    private String content;
    private MoonCommons plugin;

    public Message(PrefixType prefix, MessageType error) {
        this.prefix = prefix.getContent();
        this.content = error.getContent();
    }

    public Message(PrefixType prefix, String content) {
        this.prefix = prefix.getContent();
        this.content = content;
    }

    public Message(MessageType error, String search, String replace) {
        this.prefix = "";
        this.content = error.getContent().replace(search, replace);
    }

    public Message(String content) {
        this.prefix = "";
        this.content = content;
    }

    public Message(MessageType error) {
        this.prefix = "";
        this.content = error.getContent();
    }

    private String getMessage() {
        return prefix + content;
    }

    public void send(CustomPlayer sender) {
        send(sender.getName());
    }

    public void send(Player sender) {
        if (sender != null)
            sender.sendMessage(getMessage());
    }

    public void send(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            player.sendMessage(getMessage());
    }

    public void send(CommandSender sender) {
        sender.sendMessage(getMessage());
    }

    public void broadcast() {
        Bukkit.broadcastMessage(getMessage());
    }

}