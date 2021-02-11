package fr.heavenmoon.core.bungee.listeners;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.CustomSanction;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;


public class LoginListener implements Listener {

    private final MoonBungeeCore plugin;
    private final PersistanceManager persistanceManager;

    public LoginListener(MoonBungeeCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @EventHandler
    public void on(LoginEvent event) {
        System.out.println(event.getConnection().getUniqueId());

        String name = event.getConnection().getName();
        UUID uuid = event.getConnection().getUniqueId();

        plugin.executeAsync(() ->
        {
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
            if (persistanceManager.getSanctionManager().isBanned(customPlayer))
            {
                CustomSanction sanction = persistanceManager.getSanctionManager().getCurrentCustomSanction(customPlayer);
                CustomPlayer customPunisher = persistanceManager.getPlayerManager().getCustomPlayer(sanction.getPunisherUuid());

                if (sanction.isValid())
                {
                    event.setCancelled(true);
                    event.setCancelReason(TextComponent.fromLegacyText(plugin.getSanctionUtils().getBanReason(ChatColor.getByChar(customPunisher
                                    .getRankData().getStyleCode()) + customPunisher.getRankData().getPrefix() +customPunisher.getName(),
                            sanction.getReason(), sanction.getCreationTime(), sanction.getExpirationTime())));
                    return;
                }
                else
                {
                    plugin.getSanctionUtils().banRemove(name, event.getConnection().getUniqueId());
                }
            }

            if (customPlayer.getRankData().getPermission() < persistanceManager.getServerManager().getCustomServer(plugin.getCommons().getConfig().getServerName()).getWhitelist().getRank().getPermission())
            {
                event.setCancelled(true);
                event.setCancelReason(TextComponent.fromLegacyText(persistanceManager.getServerManager().getCustomServer(plugin.getCommons().getConfig().getServerName()).getWhitelist().getDescription()));
                return;
            }
        });
    }
}
