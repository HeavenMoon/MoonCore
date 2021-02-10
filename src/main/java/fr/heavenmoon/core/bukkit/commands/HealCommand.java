package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.PlayerUtils;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.MODERATEUR;
	private RankList max_rank = RankList.MODERATEUR;
	private String syntax = "/heal <pseudo>";
	
	public HealCommand(MoonBukkitCore plugin)
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
			if (!customPlayer.hasOnlyPermission(this.rank))
			{
				new Message(MessageType.PERMISSION, "%rank%", this.rank.getName()).send(sender);
				return false;
			}
			if (args.length == 0 && sender instanceof Player)
			{
				PlayerUtils.healPlayer(sender, sender.getName());
			}
			else if (args.length == 1)
			{
				if (!customPlayer.hasOnlyPermission(this.max_rank))
				{
					(new Message(MessageType.PERMISSION)).send(player);
					return false;
				}
				String name = args[0];
				PlayerUtils.healPlayer(sender, name);
			}
			else
			{
				(new Message(MessageType.SYNTAXE, "%syntax%", this.syntax)).send(sender);
			}
		}
		return false;
	}
}
