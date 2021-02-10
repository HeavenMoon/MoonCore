package fr.heavenmoon.core.bukkit.mod.bypass;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BypassManager
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public BypassManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public void toggleBypass(Player player)
	{
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (customPlayer.getModerationData().isBypass())
		{
			disableBypass(player);
			player.sendMessage(ChatColor.RED + "Bypass off");
		}
		else
		{
			enableBypass(player);
			player.sendMessage(ChatColor.GREEN + "Bypass on");
		}
	}
	
	public void enableBypass(Player player)
	{
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		customPlayer.getModerationData().setBypass(true);
		persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
	
	public void disableBypass(Player player)
	{
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		customPlayer.getModerationData().setBypass(false);
		persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
}