package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.info.InfoGUI;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccountCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.GUIDE;
	private String syntax = "/account <player>";
	
	public AccountCommand(MoonBukkitCore plugin)
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
			new Message(MessageType.PERMISSION, "%syntax%", this.syntax).send(sender);
			return false;
		}
		if (args.length == 1)
		{
			String name = args[0];
			plugin.getGuiManager().openGui(player, new InfoGUI(plugin, name));
		}
		else
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(player);
		}
		return false;
	}
}