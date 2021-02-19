package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PermissionCommand implements CommandExecutor
{
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.ADMINISTRATEUR;
	private String syntax = "/permission [add|remove] <player> (permission)";
	
	public PermissionCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
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
			
			if (args.length != 3)
			{
				new Message(MessageType.PERMISSION, "%syntax%", this.syntax).send(sender);
				return false;
			}
			
			String action = args[0];
			String playerName = args[1];
			String permission = args[2];
			
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(BUniqueID.get(playerName));
			List<String> permList = customTarget.getRankData().getPermissionList();
			
			switch (action)
			{
				case "add":
					if (permList.contains(permission))
					{
						new Message(PrefixType.ADMIN, "Ce joueur possède déjà cette permission.").send(customPlayer);
						return false;
					}
					permList.add(permission);
					customTarget.getRankData().setPermissionList(permList);
					persistanceManager.getPlayerManager().commit(customTarget);
					persistanceManager.getPlayerManager().update(customTarget);
					new Message(PrefixType.ADMIN,"Le joueur possède maintenant la permission `" + permission + "`.").send(customPlayer);
					return true;
				case "remove":
					if (!permList.contains(permission))
					{
						new Message(PrefixType.ADMIN, "Ce joueur ne possède pas cette permission.").send(customPlayer);
						return false;
					}
					permList.remove(permission);
					customTarget.getRankData().setPermissionList(permList);
					persistanceManager.getPlayerManager().commit(customTarget);
					persistanceManager.getPlayerManager().update(customTarget);
					new Message(PrefixType.ADMIN,"Le joueur ne possède plus la permission `" + permission + "`.").send(customPlayer);
					break;
				default:
					new Message(MessageType.PERMISSION, "%syntax%", this.syntax).send(sender);
					return false;
			}
		}
		return false;
	}
}
