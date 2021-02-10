package fr.heavenmoon.core.bukkit.mod.freeze;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeManager
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public FreezeManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public void toggleFreeze(CommandSender sender, CustomPlayer customPlayer)
	{
		if (customPlayer.getModerationData().isFreeze())
		{
			customPlayer.getModerationData().setFreeze(false);
			new Message(PrefixType.MODO, ChatColor.GRAY + customPlayer.getName()
					+ ChatColor.LIGHT_PURPLE + " n'est plus freeze").send(sender);
			if (customPlayer.isOnline()) new Message(ChatColor.GRAY + "Vous n'êtes plus freeze.").send(Bukkit.getPlayer(customPlayer.getName()));
		}
		else
		{
			customPlayer.getModerationData().setFreeze(true);
			new Message(PrefixType.MODO, ChatColor.GRAY + customPlayer.getName()
					+ ChatColor.LIGHT_PURPLE + " est maintenant freeze.").send(sender);
			if (customPlayer.isOnline()) new Message(ChatColor.GRAY + "Vous êtes maintenant freeze.").send(Bukkit.getPlayer(customPlayer.getName()));
		}
		persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
	
	public void toggleFreeze(CommandSender sender, Player target)
	{
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, target.getUniqueId());
		if (customPlayer.getModerationData().isFreeze())
		{
			customPlayer.getModerationData().setFreeze(false);
			new Message(PrefixType.MODO, ChatColor.GRAY + customPlayer.getName()
					+ ChatColor.LIGHT_PURPLE + " n'est plus freeze.").send(sender);
			if (customPlayer.isOnline()) new Message(ChatColor.GRAY + "Vous n'êtes plus freeze.").send(Bukkit.getPlayer(customPlayer.getName()));
		}
		else
		{
			customPlayer.getModerationData().setFreeze(true);
			new Message(PrefixType.MODO, ChatColor.GRAY + customPlayer.getName()
					+ ChatColor.LIGHT_PURPLE + " est maintenant freeze.").send(sender);
			if (customPlayer.isOnline()) new Message(ChatColor.GRAY + "Vous êtes maintenant freeze.").send(Bukkit.getPlayer(customPlayer.getName()));
		}
		persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
	
	public void toggleFreeze(CustomPlayer customPlayer)
	{
		customPlayer.getModerationData().setFreeze(!customPlayer.getModerationData().isFreeze());
		persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
}
