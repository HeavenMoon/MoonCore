package fr.heavenmoon.core.bukkit.mod.speed;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.mod.ModItems;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import org.bukkit.entity.Player;

public class SpeedManager {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public SpeedManager(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void toggleSpeed(Player player) {
        if (persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId()).getModerationData().isSpeed()) {
            disableSpeed(player);
        } else {
            enableSpeed(player);
        }
    }

    public void enableSpeed(Player player) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
        customPlayer.getModerationData().setSpeed(true);
        persistanceManager.getPlayerManager().commit(customPlayer);
        player.setFlySpeed(0.8F);
        player.setWalkSpeed(0.2F);
        if (customPlayer.getModerationData().isTools())
            player.getInventory().setItem(5, ModItems.SPEEDON.getContent());
    }

    public void resetSpeed(Player player) {
        player.setFlySpeed(0.1F);
        player.setWalkSpeed(0.2F);
    }

    public void refreshSpeed(Player player) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
        if (customPlayer.getModerationData().isSpeed()) {
            disableSpeed(player);
        } else {
            enableSpeed(player);
        }
    }

    public void disableSpeed(Player player) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
        customPlayer.getModerationData().setSpeed(false);
        persistanceManager.getPlayerManager().commit(customPlayer);
        player.setFlySpeed(0.1F);
        player.setWalkSpeed(0.2F);
        if (customPlayer.getModerationData().isTools())
            player.getInventory().setItem(5, ModItems.SPEEDOFF.getContent());
    }
}