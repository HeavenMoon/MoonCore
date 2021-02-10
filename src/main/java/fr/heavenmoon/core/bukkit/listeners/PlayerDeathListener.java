package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public PlayerDeathListener(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (event.getEntity().getKiller() == null) {
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " s'est pris un bain de lave !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " a suffoqué !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " s'est pris pour un avion !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort par le feu !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort dans le vide !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort noyé !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.STARVATION) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort de faim !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort d'une TNT !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort d'un creeper chargé !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " s'est fait piqué par un cactus !");
                return;
            }
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.POISON) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " s'est fait empoisonné !");
            }
        } else {
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                if(player.getKiller() == null) {
                    event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort par un " + player.getLastDamageCause().getEntityType().name() + " ! " + ChatColor.GRAY + "[" + ChatColor.GREEN + Math.round(player.getKiller().getHealth()) + ChatColor.RED + "❤" + ChatColor.GRAY + "]");
                } else {
                    Player killer = event.getEntity().getKiller();
                    CustomPlayer customKiller = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, killer.getUniqueId());
                    if (customKiller.getModerationData().isVanish()) {
                        event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort !");
                    } else {
                        event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort par " + killer.getName() + " ! " + ChatColor.GRAY + "[" + ChatColor.GREEN + Math.round(killer.getHealth()) + ChatColor.RED + "❤" + ChatColor.GRAY + "]");
                    }
                }
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                event.setDeathMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " est mort par un superbe projectile lancé par " + player.getKiller().getName() + " ! " + ChatColor.GRAY + "[" + ChatColor.GREEN + Math.round(player.getKiller().getHealth()) + ChatColor.RED + "❤" + ChatColor.GRAY + "]");
            }
        }
    }
}