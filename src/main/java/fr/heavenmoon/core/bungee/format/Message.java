package fr.heavenmoon.core.bungee.format;

import fr.heavenmoon.core.common.MoonCommons;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Chat;

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

    public void send(ProxiedPlayer sender) {
        if (sender != null)
            sender.sendMessage(TextComponent.fromLegacyText(getMessage()));
    }

    public void send(String playerName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null)
            player.sendMessage(TextComponent.fromLegacyText(getMessage()));
    }


    public void send(CommandSender sender) {
        sender.sendMessage(TextComponent.fromLegacyText(getMessage()));
    }

    public void broadcast() {
        ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(getMessage()));
    }
    
}
