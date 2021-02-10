package fr.heavenmoon.core.bukkit.commands.sanctions;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.utils.objects.ArrayUtils;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList minRank = RankList.MODERATEUR;
	private String minSyntax = "/ban remove <pseudo>";
	private RankList maxRank = RankList.SUPERMODO;
	private String maxSyntax = "/ban add <pseudo> <temps> <raison> ou /ban <remove|status> <pseudo>";
	
	public BanCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
			if (customPlayer.hasPermission(this.maxRank))
			{
				if (args.length == 2)
				{
					if (args[0].equalsIgnoreCase("remove"))
					{
						String name = args[1];
						plugin.getBanManager().banRemove(sender, name);
					}
					else
					{
						new Message(MessageType.SYNTAXE, "%syntax%", this.maxSyntax).send(sender);
					}
				}
				else if (args.length >= 4 && args[0].equalsIgnoreCase("add"))
				{
					String name = args[1];
					String time = args[2];
					String reason = ArrayUtils.getArgumentsMin(args, 3);
					plugin.getBanManager().banAdd(sender, name, reason, time);
				}
				else
				{
					new Message(MessageType.SYNTAXE, "%syntax%", this.maxSyntax).send(sender);
				}
			}
			else if (customPlayer.hasPermission(this.minRank))
			{
				if (args.length == 2)
				{
					if (args[0].equalsIgnoreCase("remove"))
					{
						String name = args[1];
						plugin.getBanManager().banRemove(sender, name);
					}
					else
					{
						new Message(MessageType.SYNTAXE, "%syntax%", this.minSyntax).send(sender);
					}
				}
				else
				{
					new Message(MessageType.SYNTAXE, "%syntax%", this.minSyntax).send(sender);
				}
			}
			else
			{
				new Message(MessageType.PERMISSION, "%rank%", this.minRank.getName()).send(sender);
			}
		}
		
		return false;
	}
}