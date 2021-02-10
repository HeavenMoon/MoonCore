package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.info.InfoGUI;
import fr.heavenmoon.core.bukkit.mod.ModItems;
import fr.heavenmoon.core.bukkit.mod.sanctions.templategui.SanctionGUI;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class ModListener implements Listener {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public ModListener(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (customPlayer.getModerationData().isEnable() && customPlayer.getModerationData().isTools())
            player.getInventory().clear();
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (customPlayer.getModerationData().isEnable() && customPlayer.getModerationData().isTools())
            event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (customPlayer.getModerationData().isTools())
            event.setCancelled(true);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
            if (customPlayer.getModerationData().isEnable())
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (customPlayer.getModerationData().isEnable()) {
            event.setKeepInventory(true);
            event.setDroppedExp(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            CustomPlayer customDamager = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, damager.getUniqueId());
            if (customDamager.getModerationData().isEnable()) {
                if (event.isCancelled())
                    event.setCancelled(false);
                event.setDamage(0.0D);
            }
        }
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
            if (customPlayer.getModerationData().isEnable())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (!customPlayer.getModerationData().isEnable()) return;
        if (event.getItem() == null || event.getItem().getType() == Material.AIR)
            return;
        if (customPlayer.getModerationData().isTools() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            if (!customPlayer.getModerationData().isTools())
                return;

            Material material = player.getInventory().getItemInHand().getType();
            String itemName = player.getItemInHand().getItemMeta().getDisplayName();
            if (material == ModItems.STRIP.getContent().getType())
                event.setCancelled(true);
            if (material == ModItems.FREEZE.getContent().getType())
                event.setCancelled(true);
            if (itemName.equalsIgnoreCase(ModItems.VANISHOFF.getContent().getItemMeta().getDisplayName())) {
                plugin.getVanishManager().enableVanish(player);
            } else if (itemName.equalsIgnoreCase(ModItems.VANISHON.getContent().getItemMeta().getDisplayName())) {
                plugin.getVanishManager().disableVanish(player);
            }
            if (itemName.equalsIgnoreCase(ModItems.SPEEDOFF.getContent().getItemMeta().getDisplayName())) {
                plugin.getSpeedManager().enableSpeed(player);
            } else if (itemName.equalsIgnoreCase(ModItems.SPEEDON.getContent().getItemMeta().getDisplayName())) {
                plugin.getSpeedManager().disableSpeed(player);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player))
            return;
        Player player = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (!customPlayer.getModerationData().isEnable())
            return;
        if (!customPlayer.getModerationData().isTools())
            return;
        Material material = player.getItemInHand().getType();
        if (material == ModItems.STRIP.getContent().getType())
            target.getInventory().setArmorContents(null);
        if (material == ModItems.FREEZE.getContent().getType())
            plugin.getFreezeManager().toggleFreeze(player, target);
        if (material == ModItems.INFO.getContent().getType())
            plugin.getGuiManager().openGui(player, new InfoGUI(plugin, target.getName()));
        if (material == ModItems.SANCTION.getContent().getType())
            plugin.getGuiManager().openGui(player, new SanctionGUI(plugin, target.getName()));
    }

    @EventHandler
    public void on(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
            if (customPlayer.getModerationData().isVanish())
                event.setCancelled(true);
        }
    }
}