package fr.heavenmoon.core.bungee.listeners;

import fr.heavenmoon.core.common.utils.builders.motd.MotdBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingEventListener implements Listener {

    @EventHandler
    public void on(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();
        PendingConnection connection = event.getConnection();
        if (serverPing == null) {
            return;
        }
        MotdBuilder motd = new MotdBuilder(3, ChatColor.RED + "Développement",
                ChatColor.DARK_PURPLE + " HeavenMoon " + ChatColor.GRAY + "- Serveur " + ChatColor.LIGHT_PURPLE + "Faction " + ChatColor.AQUA + "1.8 " + ChatColor.GRAY + "- " + ChatColor.AQUA + "1.15 " + ChatColor.GRAY + "!",
                ChatColor.RED + "     Serveur en développement !");

        int protocolId = motd.getProtocolId();
        String protocolValue = motd.getProtocolValue();

        if (connection.getVersion() < 47) {
            protocolValue = "Version 1.8.8";
            protocolId = 4;
        }

        serverPing.setDescription(motd.getDescription());
        if (protocolId == -1)
            serverPing.setVersion(new ServerPing.Protocol(protocolValue, serverPing.getVersion().getProtocol()));
        else {
            serverPing.setVersion(new ServerPing.Protocol(protocolValue, protocolId));
        }

    }
}
