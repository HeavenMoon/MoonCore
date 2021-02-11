package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public PlayerQuitListener(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        
        Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
        
        if (customPlayer.getModerationData().isEnable()) {
            event.setQuitMessage(null);
        }
        
        if (player.isOp()) player.setOp(false);
        
        long timePlayed = System.currentTimeMillis() / 1000L - customPlayer.getLastLogin() / 1000L;
        customPlayer.setTimePlayed(customPlayer.getTimePlayed() + timePlayed);
        persistanceManager.getPlayerManager().commit(customPlayer);
    }
}
