package fr.heavenmoon.core.bukkit.commands.gamemode;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmcCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	RankList rank = RankList.ADMINISTRATEUR;
	RankList max_rank = RankList.ADMINISTRATEUR;
	
	public GmcCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (!(sender instanceof Player))
		{
			new Message(MessageType.CONSOLE).send(sender);
			return false;
		}
		Player player = (Player) sender;
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (!customPlayer.hasOnlyPermission(this.rank))
		{
			new Message(MessageType.PERMISSION).send(sender);
			return false;
		}
		if (args.length == 0)
		{
			customPlayer.setGamemode(Integer.valueOf(1));
			persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
			player.setGameMode(GameMode.CREATIVE);
			(new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + "CREATIVE")).send(player);
			return true;
		}
		if (args.length == 1)
		{
			String name = args[0];
			if (!customPlayer.hasOnlyPermission(this.max_rank))
			{
				(new Message(MessageType.PERMISSION)).send(sender);
				return false;
			}
			if (Bukkit.getPlayer(name) == null)
			{
				(new Message(MessageType.NO_PLAYER)).send(sender);
				return false;
			}
			Player target = Bukkit.getPlayer(name);
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, target.getUniqueId());
			customTarget.setGamemode(Integer.valueOf(1));
			persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
			target.setGameMode(GameMode.CREATIVE);
			(new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + "CREATIVE")).send(target);
			(new Message(ChatColor.GRAY + "GameMode mis jour pour " + ChatColor.DARK_GRAY + target.getName() + ChatColor.DARK_GRAY + ": " +
					ChatColor.GREEN + "CREATIVE")).send(player);
			return true;
		}
		return false;
	}
}
