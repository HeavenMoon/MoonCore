package fr.heavenmoon.core.bukkit.commands.time;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.SUPERMODO;
	private String syntax = "/day";
	
	public DayCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (sender instanceof Player)
		{
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER,
					((Player) sender).getUniqueId());
			if (!customPlayer.hasOnlyPermission(this.rank))
			{
				new Message(MessageType.PERMISSION).send(sender);
				return false;
			}
		}
		if (args.length == 0)
		{
			TimeUtils.setDay();
		}
		else
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax);
		}
		return false;
	}
}