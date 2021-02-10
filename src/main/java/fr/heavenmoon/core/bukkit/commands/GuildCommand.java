package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private String syntax = "/guild";
	private RankList rank = RankList.ADMINISTRATEUR;
	
	public GuildCommand(MoonBukkitCore plugin)
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
		if (args.length == 0)
		{
			if (customPlayer.getGuild().getGuild() == null)
			{
				new Message(PrefixType.SERVER, "Vous n'avez pas de guild.").send(player);
				return false;
			}
			new Message(PrefixType.SERVER,
					"Votre guild: " + ChatColor.BLUE + customPlayer.getGuild().getGuild().getName() + ChatColor.GRAY + ", votre rang: " +
							customPlayer
									.getGuild().getRank().getStyle() + customPlayer.getGuild().getRank());
		}
		else if (args.length == 3)
		{
			if (!customPlayer.hasOnlyPermission(this.rank))
			{
				(new Message(MessageType.PERMISSION)).send(player);
				return false;
			}
			String name = args[0];
			String arg = args[1];
			String value = args[2];
			if (args[1].equalsIgnoreCase("set"))
			{
				plugin.getGuildManager().setGuild(player, name, value);
			}
			else if (args[1].equalsIgnoreCase("setrank"))
			{
				plugin.getGuildManager().setGuildRank(player, name, value);
			}
		}
		return false;
	}
}