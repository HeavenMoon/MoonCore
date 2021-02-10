package fr.heavenmoon.core.bungee.listeners;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.address.CustomAddress;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.UniqueID;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class CacheListener implements Listener {

    private final MoonBungeeCore plugin;
    private final PersistanceManager persistanceManager;

    public CacheListener(MoonBungeeCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @EventHandler(priority = 10)
    public void on(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        String address = player.getPendingConnection().getAddress().getAddress().getHostAddress();

        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            //Load Address
            CustomAddress customAddress = persistanceManager.getAddressManager().get(address);
            customAddress.addAccounts(name);
            persistanceManager.getAddressManager().update(customAddress);

            //Load Player
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, UUID.fromString(uuid));
            if (!customPlayer.isExist()) {
                plugin.getCommons().getLogger().warn("The player does not exist !");
                customPlayer.setExist(true);
                customPlayer.setName(name);
                customPlayer.setDisplayName(name);
                customPlayer.setPremium(UniqueID.getMojang(name) != null);
            }
            customPlayer.setOnline(true);
            customPlayer.setServerName(null);
            customPlayer.setProxyName(plugin.getCommons().getServerName());
            customPlayer.setLastLogin(System.currentTimeMillis());
            customPlayer.setLastIP(address);
            List<String> allAddress = customPlayer.getAllAddress();
            if (!allAddress.contains(address)) {
                allAddress.add(address);
            }
            customPlayer.setAllAddress(allAddress);
           persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
            plugin.getCommons().getLogger().info("Player " + name + " registered with success !");
        });
    }

    @EventHandler(priority = 10)
    public void on(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String name = player.getName();
        UUID uuid = player.getUniqueId();

        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, uuid);
            if (customPlayer.getModerationData().isBypass()) customPlayer.getModerationData().setBypass(false);
            customPlayer.setOnline(false);
            customPlayer.setServerName(null);
            persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
            persistanceManager.getPlayerManager().update(customPlayer);
            persistanceManager.getPlayerManager().removeFromCache(customPlayer);
            persistanceManager.getPlayerManager().remove(RedisKey.PLAYER, customPlayer);
        });
    }

}
