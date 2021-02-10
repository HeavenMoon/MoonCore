package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.math.MathUtils;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GemmesComand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.ADMINISTRATEUR;
	
	private String syntax = "/stars <add|remove> <pseudo> <montant>";
	
	public GemmesComand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (args.length == 3)
		{
			String targetName = args[1];
			String amount = args[2];
			
			UUID uuid = BUniqueID.get(targetName);
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, uuid);
			if (customTarget == null)
			{
				new Message(PrefixType.ERROR, "Le joueur n'existe pas.").send(sender);
				return false;
			}
			if (!MathUtils.isInteger(amount))
			{
				new Message(PrefixType.ERROR, "Montant invalide.").send(sender);
				return false;
			}
			
			long gemmes = Long.parseLong(amount);
			
			if (args[0].equalsIgnoreCase("add"))
			{
				
				customTarget.setGemmes(customTarget.getGemmes() + (int)gemmes);
				persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customTarget);
				Player targetPlayer = Bukkit.getPlayer(customTarget.getName());
				if (targetPlayer != null)
				{
					plugin.getScoreboardManager().update(targetPlayer);
					new Message(PrefixType.SERVER,
							"Vous avez reçu " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_PURPLE + " Gemmes " + ChatColor.GRAY +
									" !").send(targetPlayer);
				}
				new Message(PrefixType.ADMIN,
						ChatColor.LIGHT_PURPLE + amount + ChatColor.LIGHT_PURPLE + " Gemmes " + ChatColor.RED + "on été ajouté à " +
								ChatColor.DARK_RED + customTarget.getName()).send(sender);
				return false;
				
			}
			else if (args[0].equalsIgnoreCase("remove"))
			{
				
				customTarget.setGemmes(customTarget.getGemmes() - (int)gemmes);
				persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customTarget);
				Player targetPlayer = Bukkit.getPlayer(customTarget.getName());
				if (targetPlayer != null)
				{
					plugin.getScoreboardManager().update(targetPlayer);
					new Message(PrefixType.SERVER,
							"Vous avez perdu " + ChatColor.LIGHT_PURPLE + amount + ChatColor.DARK_PURPLE + " Gemmes " + ChatColor.GRAY +
									" !").send(targetPlayer);
				}
				new Message(PrefixType.ADMIN,
						ChatColor.LIGHT_PURPLE + amount + ChatColor.LIGHT_PURPLE + " Gemmes " + ChatColor.RED + "on été retiré à " +
								ChatColor.DARK_RED + customTarget.getName()).send(sender);
				return false;
				
			}
			
		}
		else
		{
			new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(sender);
		}
		
		return false;
	}
}
