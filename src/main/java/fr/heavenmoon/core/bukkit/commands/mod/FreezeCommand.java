package fr.heavenmoon.core.bukkit.commands.mod;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FreezeCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.MODERATEUR;
	private String syntax = "/freeze <pseudo>";
	
	public FreezeCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
			if (!customPlayer.hasPermission(this.rank))
			{
				new Message(MessageType.PERMISSION, "%rank%", this.rank.getName()).send(sender);
				return false;
			}
		}
		if (args.length == 1)
		{
			String name = args[0];
			UUID uuid = BUniqueID.get(name);
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
			plugin.getFreezeManager().toggleFreeze(sender, customTarget);
		}
		else
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(sender);
		}
		return false;
	}
	
	
}
