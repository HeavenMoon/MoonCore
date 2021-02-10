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

public class MessageCommand implements CommandExecutor
{
	private final MoonBukkitCore moonBukkitCore;
	
	private String syntax = "/m [player] [message]";
	
	public MessageCommand(MoonBukkitCore moonBukkitCore)
	{
		this.moonBukkitCore = moonBukkitCore;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		if (args.length <= 1)
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(player);
			return false;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null)
		{
			new Message(PrefixType.ERROR, ChatColor.RED + "Le joueur n'existe pas.").send(player);
			return false;
		}
		if (target.getName().equalsIgnoreCase(player.getName()))
		{
			new Message(PrefixType.ERROR, ChatColor.RED + "Vous ne pouvez pas vous envoyer de messages.").send(player);
			return false;
		}
		StringBuilder str = new StringBuilder();
		for (int i = 1; i < args.length; i++)
			str.append(args[i] + " ");
		new Message(ChatColor.RED + "[DM] " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " -> " + ChatColor.YELLOW +
				target.getName() + ChatColor.GRAY + " : " + str.toString()).send(player);
		new Message(ChatColor.RED + "[DM] " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " -> " + ChatColor.YELLOW +
				target.getName() + ChatColor.GRAY + " : " + str.toString()).send(target);
		this.moonBukkitCore.getDmManager().putMessage(player.getUniqueId(), target.getUniqueId());
		this.moonBukkitCore.getDmManager().putMessage(target.getUniqueId(), player.getUniqueId());
		return false;
	}
}