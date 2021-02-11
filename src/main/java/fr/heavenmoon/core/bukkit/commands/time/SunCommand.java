package fr.heavenmoon.core.bukkit.commands.time;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SunCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.SUPERMODO;
	private String syntax = "/sun";
	
	public SunCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (sender instanceof Player)
		{
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(((Player) sender).getUniqueId());
			if (!customPlayer.hasPermission(this.rank))
			{
				new Message(MessageType.PERMISSION).send(sender);
				return false;
			}
		}
		if (args.length == 0)
		{
			TimeUtils.setSun();
		}
		else
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax);
		}
		return false;
	}
}
