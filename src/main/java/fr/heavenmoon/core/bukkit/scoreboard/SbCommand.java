package fr.heavenmoon.core.bukkit.scoreboard;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SbCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private String syntax = "/sb <on|off>";
	
	public SbCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		
		if (!(sender instanceof Player))
		{
			return false;
		}
		
		Player player = (Player) sender;
		if (args.length == 0)
		{
			new Message(MessageType.SYNTAXE, "%syntax%", syntax).send(sender);
			return false;
		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("off"))
			{
				plugin.getScoreboardManager().onLogout(player);
				player.sendMessage(ChatColor.RED + "Scoreboard désactivé");
				return false;
			}
			else if (args[0].equalsIgnoreCase("on"))
			{
				plugin.getScoreboardManager().onLogin(player);
				player.sendMessage(ChatColor.GREEN + "Scoreboard activé");
				return false;
			}
		}
		return false;
	}
}
