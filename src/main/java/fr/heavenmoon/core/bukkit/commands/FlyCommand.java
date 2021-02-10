package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor
{
	
	private static MoonBukkitCore plugin;
	private static PersistanceManager persistanceManager;
	
	private RankList rank = RankList.MODERATEUR;
	private RankList max_rank = RankList.ADMINISTRATEUR;
	private String syntax = "/fly <pseudo>";
	
	public FlyCommand(MoonBukkitCore plugin)
	{
		FlyCommand.plugin = plugin;
		persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public static void toggleFly(Player player)
	{
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		
		if (player.getAllowFlight())
		{
			player.setAllowFlight(false);
			customPlayer.getModerationData().setFly(false);
			new Message(PrefixType.MODO, "Vous ne pouvez plus voler").send(player);
		}
		else
		{
			player.setAllowFlight(true);
			customPlayer.getModerationData().setFly(true);
			new Message(PrefixType.MODO, "Vous pouvez maintenant voler").send(player);
		}
		persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
	
	public static void toggleFly(Player player, String name)
	{
		Player target = Bukkit.getPlayer(name);
        if (target == null) return;
        CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, target.getUniqueId());
        if (target.getAllowFlight())
		{
			target.setAllowFlight(false);
			customTarget.getModerationData().setFly(false);
			new Message(PrefixType.MODO, ChatColor.GRAY + name + ChatColor.LIGHT_PURPLE + " ne peut plus voler.").send(player);
			new Message(PrefixType.SERVER, "Vous ne pouvez plus voler").send(target);
		}
		else
		{
			target.setAllowFlight(true);
			customTarget.getModerationData().setFly(true);
			new Message(PrefixType.MODO, ChatColor.GRAY + name + ChatColor.LIGHT_PURPLE + " peut maintenant voler.").send(player);
			new Message(PrefixType.SERVER, "Vous pouvez maintenant voler").send(target);
		}
        persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customTarget);
	}
	
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
			if (!customPlayer.hasOnlyPermission(this.rank))
			{
				new Message(MessageType.PERMISSION, "%rank%", this.rank.getName()).send(sender);
				return false;
			}
			if (args.length == 0)
			{
				toggleFly(player);
			}
			else if (args.length == 1)
			{
				if (!customPlayer.hasOnlyPermission(this.max_rank))
				{
					(new Message(MessageType.PERMISSION)).send(player);
					return false;
				}
				String name = args[0];
				toggleFly(player, name);
			}
			else
			{
				new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(sender);
			}
		}
		return false;
	}
}
