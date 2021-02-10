package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.ADMINISTRATEUR;
	private String syntax = "/rank <player> (rank) (permission)";
	
	public RankCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
			if (!customPlayer.hasPermission(this.rank))
			{
				new Message(MessageType.PERMISSION, "%rank%", this.rank.getName()).send(sender);
				return false;
			}
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("list"))
		{
			plugin.getRankManager().getList(sender);
		}
		else if (args.length == 2)
		{
			String name = args[0];
			String rank = args[1];
			plugin.getRankManager().setRank(sender, name, rank, rank);
			Player player = Bukkit.getPlayerExact(name);
			if (player != null) plugin.getScoreboardManager().update(player);
		}
		else if (args.length == 3)
		{
			String name = args[0];
			String rank = args[1];
			String permission = args[2];
			plugin.getRankManager().setRank(sender, name, rank, permission);
			Player player = Bukkit.getPlayerExact(name);
			if (player != null) plugin.getScoreboardManager().update(player);
		}
		else
		{
			(new Message(MessageType.SYNTAXE, "%syntax%", this.syntax)).send(sender);
		}
		return false;
	}
}