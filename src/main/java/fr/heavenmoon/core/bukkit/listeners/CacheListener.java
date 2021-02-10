package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.scoreboard.ScoreboardTeam;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CacheListener implements Listener
{
	
	private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern
			.compile("[\u0000\u0002]PAC([0-9A-F]{5})[\u0000\u0002]");
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public CacheListener(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	@EventHandler
	public void on(PlayerLoginEvent event)
	{
		plugin.executeAsync(() ->
		{
			Player player = event.getPlayer();
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
			String hostname = event.getHostname();
			Matcher m = PACTIFY_HOSTNAME_PATTERN.matcher(hostname);
			int launcherProtocolVersion;
			if (m.find())
			{
				launcherProtocolVersion = Math.max(1, Integer.parseInt(m.group(1), 16));
				if (launcherProtocolVersion > 0)
				{
					customPlayer.setAzlauncher(true);
				}
				else
				{
					customPlayer.setAzlauncher(false);
				}
			}
			else
			{
				customPlayer.setAzlauncher(false);
			}
			persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
		});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		persistanceManager.getPlayerManager().cache(customPlayer);
		if (customPlayer.getRankData().getRank() == RankList.ADMINISTRATEUR)
			event.getPlayer().setOp(true);
		
		// Send team to player
		for (ScoreboardTeam team : plugin.getTeams())
			(((CraftPlayer) Bukkit.getPlayer(event.getPlayer().getUniqueId())).getHandle()).playerConnection.sendPacket(team.createTeam());
		plugin.getScoreboardManager().onLogin(event.getPlayer());
		
		if (event.getPlayer().isDead())
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> event.getPlayer().spigot().respawn(), 2L);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		// Remove team from player
		for (ScoreboardTeam team : plugin.getTeams())
			(((CraftPlayer) event.getPlayer()).getHandle()).playerConnection.sendPacket(team.removeTeam());
		plugin.getScoreboardManager().onLogout(event.getPlayer());
		persistanceManager.getPlayerManager().removeFromCache(customPlayer);
		
	}
}