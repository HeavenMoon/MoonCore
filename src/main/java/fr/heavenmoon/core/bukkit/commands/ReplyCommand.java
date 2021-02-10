package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor
{
	private final MoonBukkitCore moonBukkitCore;
	
	private String syntax = "/r [message]";
	
	public ReplyCommand(MoonBukkitCore moonBukkitCore)
	{
		this.moonBukkitCore = moonBukkitCore;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		if (args.length == 0)
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(player);
			return false;
		}
		Player target = Bukkit.getPlayer(this.moonBukkitCore.getDmManager().getLastMessage(player.getUniqueId()));
		if (target == null)
		{
			new Message(PrefixType.ERROR, ChatColor.RED + "Vous n'avez personne qui à qui répondre.").send(player);
			return false;
		}
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < args.length; i++)
			str.append(args[i] + " ");
		new Message(ChatColor.RED + "[DM] " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " -> " + ChatColor.YELLOW +
				target.getName() + ChatColor.GRAY + " : " + str.toString()).send(player);
		new Message(ChatColor.RED + "[DM] " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " -> " + ChatColor.YELLOW +
				target.getName() + ChatColor.GRAY + " : " + str.toString()).send(target);
		return false;
	}
}

