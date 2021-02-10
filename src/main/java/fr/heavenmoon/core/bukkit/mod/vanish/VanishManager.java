package fr.heavenmoon.core.bukkit.mod.vanish;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.mod.ModItems;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishManager {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public VanishManager(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void refreshVanish() {
        Bukkit.getOnlinePlayers().forEach(this::refreshVanish);
    }

    public void refreshVanish(Player player) {
        CustomPlayer moderator = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        plugin.getServer().getOnlinePlayers().forEach(current -> {
            if (player != current) {
                CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, current.getUniqueId());
                if (customPlayer.getModerationData().isVanish()) {
                    if (moderator.getModerationData().isEnable()) {
                        if (customPlayer.getModerationData().isBypass()) {
                            if (!moderator.getModerationData().isBypass()) {
                                player.hidePlayer(current);
                            } else {
                                player.showPlayer(current);
                            }
                        } else {
                            player.showPlayer(current);
                        }
                    } else {
                        player.hidePlayer(current);
                    }
                } else {
                    player.showPlayer(current);
                }
            }
        });
    }

    public void toggleVanish(Player player) {
        if (persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId()).getModerationData().isVanish()) {
            disableVanish(player);
        } else {
            enableVanish(player);
        }
    }

    public void showAll(Player player) {
        Bukkit.getOnlinePlayers().forEach(player::showPlayer);
    }

    public void enableVanish(Player player) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        customPlayer.getModerationData().setVanish(true);
        persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
        if (customPlayer.getModerationData().isTools())
            player.getInventory().setItem(6, ModItems.VANISHON.getContent());
        refreshVanish();
        new Message(PrefixType.MODO, "Vous êtes invisible.").send(player);
    }

    public void disableVanish(Player player) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        customPlayer.getModerationData().setVanish(false);
        persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
        if (customPlayer.getModerationData().isTools())
            player.getInventory().setItem(6, ModItems.VANISHOFF.getContent());
        refreshVanish();
        new Message(PrefixType.MODO, "Vous êtes visible.").send(player);
    }
}