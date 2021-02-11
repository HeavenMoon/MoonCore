package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.ActionbarBuilder;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.MODERATEUR;
	private RankList max_rank = RankList.ADMINISTRATEUR;
	private String syntax = "/clear (<clear>)";
	
	public ClearCommand(MoonBukkitCore plugin)
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
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
		if (!customPlayer.hasPermission(this.rank))
		{
			(new Message(MessageType.PERMISSION, "%rank%", this.rank.getName())).send(sender);
			return false;
		}
		if (args.length == 0)
		{
			player.getInventory().clear();
			new ActionbarBuilder(ChatColor.GREEN + "Vous avez été clear.", 4).send(player);
		}
		else if (args.length == 1)
		{
			if (!customPlayer.hasPermission(this.max_rank))
			{
				(new Message(MessageType.PERMISSION, "%rank%", this.rank.getName())).send(sender);
				return false;
			}
			String name = args[0];
			Player target = Bukkit.getPlayer(name);
			if (target != null)
			{
				CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(    target.getUniqueId());
				target.getInventory().clear();
				new ActionbarBuilder(
						ChatColor.GRAY + "Le joueur " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) +
                                customTarget.getRankData().getPrefix() + customTarget.getName() + ChatColor.GRAY + " a été clear.", 4).send(player);
			}
			else
			{
				new Message(MessageType.NO_PLAYER).send(sender);
			}
		}
		else
		{
			(new Message(MessageType.SYNTAXE, "%syntax%", this.syntax)).send(sender);
		}
		return false;
	}
}