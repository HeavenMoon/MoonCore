package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class FreezeListener implements Listener
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public FreezeListener(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (customPlayer.getModerationData().isFreeze())
			player.damage(1000.0D);
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent event)
	{
		if (!(event.getDamager() instanceof Player))
			return;
		Player damager = (Player) event.getDamager();
		CustomPlayer customDamager = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, damager.getUniqueId());
		if (customDamager.getModerationData().isFreeze())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void on(EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (customPlayer.getModerationData().isFreeze())
		{
			event.setDamage(0.0D);
			player.setVelocity(new Vector());
		}
	}
	
	@EventHandler
	public void on(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (customPlayer.getModerationData().isFreeze() && (
				event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() ||
						event.getFrom().getZ() != event.getTo().getZ()))
		{
			event.setTo(event.getFrom());
			new Message(PrefixType.SERVER,
					"Tu es freeze! Un membre du staff va te contacter (" + ChatColor.RED + "déconnection = kill" + ChatColor.GRAY + ").")
					.send(player);
		}
	}
	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		String label = event.getMessage().substring(1);
		if (label.equalsIgnoreCase("login"))
			return;
		if (label.equalsIgnoreCase("help"))
		{
			player.sendMessage("Unknown command. Type \"/help\" for help.");
			event.setCancelled(true);
		}
		if (customPlayer.getModerationData().isFreeze())
			event.setCancelled(true);
	}
}
