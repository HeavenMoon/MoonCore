package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.math.MathUtils;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.ADMINISTRATEUR;
	private String syntax = "/stop <temps>";
	
	public StopCommand(MoonBukkitCore plugin)
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
				(new Message(MessageType.PERMISSION, "%rank%", this.rank.getName())).send(sender);
				return false;
			}
		}
		
		if (args.length == 0)
		{
			persistanceManager.shutdown(plugin.getCommons().getServerName());
			plugin.getCommons().shutdown();
			plugin.getServer().shutdown();
		}
		else if (args.length == 1)
		{
			String timeout = args[0];
			if (MathUtils.isInteger(timeout))
			{
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
						{
							persistanceManager.shutdown(plugin.getCommons().getServerName());
							plugin.getCommons().shutdown();
							plugin.getServer().shutdown();
						}, Integer.parseInt(timeout) * 20);
			}
			else
			{
				new Message(PrefixType.ERROR, "La durée de temps choisie est invalide.").send(sender);
			}
		}
		else
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(sender);
		}
		return false;
	}
}