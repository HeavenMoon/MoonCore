package fr.heavenmoon.core.bukkit.scoreboard;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ScoreboardManager
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	
	private final Map<UUID, PersonalScoreboard> scoreboards;
	private final ScheduledFuture glowingTask;
	private final ScheduledFuture reloadingTask;
	private int ipCharIndex;
	private int cooldown;
	
	public ScoreboardManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
		scoreboards = new HashMap<>();
		ipCharIndex = 0;
		cooldown = 0;
		
		glowingTask = plugin.getScheduledExecutorService().scheduleAtFixedRate(() ->
		{
			String ip = colorIpAt();
			for (PersonalScoreboard scoreboard : scoreboards.values())
				plugin.getExecutorMonoThread().execute(() -> scoreboard.setLines(ip));
		}, 80, 80, TimeUnit.MILLISECONDS);
		
		reloadingTask = plugin.getScheduledExecutorService().scheduleAtFixedRate(() ->
		{
			for (PersonalScoreboard scoreboard : scoreboards.values())
				plugin.getExecutorMonoThread().execute(scoreboard::reloadData);
		}, 1, 9, TimeUnit.SECONDS);
	}
	
	public void onDisable()
	{
		this.glowingTask.cancel(true);
		this.reloadingTask.cancel(true);
		scoreboards.values().forEach(PersonalScoreboard::onLogout);
	}
	
	public void onLogin(Player player)
	{
		if (this.scoreboards.containsKey(player.getUniqueId()))
		{
			plugin.getLogger().log(Level.WARNING, "The player '" + player.getUniqueId().toString() + "' already have a scoreboard!");
			return;
		}
		
		this.scoreboards.put(player.getUniqueId(), new PersonalScoreboard(this.plugin, player));
		plugin.getLogger().log(Level.INFO, "Added scoreboard to '" + player.getUniqueId() + "'.");
	}
	
	public void onLogout(Player player)
	{
		if (scoreboards.containsKey(player.getUniqueId()))
		{
			scoreboards.get(player.getUniqueId()).onLogout();
			scoreboards.remove(player.getUniqueId());
			
			plugin.getLogger().log(Level.INFO, "Removed scoreboard to '" + player.getUniqueId() + "'.");
		}
	}
	
	public void update(Player player)
	{
		if (scoreboards.containsKey(player.getUniqueId()))
		{
			scoreboards.get(player.getUniqueId()).reloadData();
			scoreboards.get(player.getUniqueId()).refreshTeams();
		}
	}
	
	private String colorIpAt()
	{
		String ip = "play.heavenmoon.fr";
		
		if (cooldown > 0)
		{
			cooldown--;
			return ip;
		}
		
		StringBuilder formattedIp = new StringBuilder();
		
		if (ipCharIndex > 0)
		{
			formattedIp.append(ip, 0, ipCharIndex - 1);
			formattedIp.append(ChatColor.LIGHT_PURPLE).append(ip, ipCharIndex - 1, ipCharIndex);
		}
		else
		{
			formattedIp.append(ip, 0, ipCharIndex);
		}
		
		formattedIp.append(ChatColor.DARK_PURPLE).append(ip.charAt(ipCharIndex));
		
		if (ipCharIndex + 1 < ip.length())
		{
			formattedIp.append(ChatColor.LIGHT_PURPLE).append(ip.charAt(ipCharIndex + 1));
			
			if (ipCharIndex + 2 < ip.length())
				formattedIp.append(ChatColor.AQUA).append(ip.substring(ipCharIndex + 2));
			
			ipCharIndex++;
		}
		else
		{
			ipCharIndex = 0;
			cooldown = 50;
		}
		
		return ChatColor.AQUA + formattedIp.toString();
	}
	
}