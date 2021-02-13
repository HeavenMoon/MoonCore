package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.managers.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.managers.redis.RedisTarget;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GsayCommand implements CommandExecutor
{
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.SUPERMODO;
	
	public GsayCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
			
			String prefix = ChatColor.DARK_RED + "[" + ChatColor.RED + "Annonce" + ChatColor.DARK_RED + "] " +
					ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getPrefix() + customPlayer.getName() + " §7» ";
			
			StringBuilder sb = new StringBuilder();
			for (String arg : args)
			{
				sb.append(arg).append(" ");
			}
			String message = sb.toString();
			String formated_message = prefix + message.substring(0, sb.toString().length() - 1).replace("&", "§");
			
			new RedisPublisher(persistanceManager, "gSay").addArgument(formated_message).publish(new RedisTarget(
					RedisTarget.RedisTargetType.PROXY));
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			for (String arg : args)
			{
				sb.append(arg).append(" ");
			}
			String message = sb.toString();
			String formated_message = message.substring(0, sb.toString().length() - 1).replace("&", "§");
			
			new RedisPublisher(persistanceManager, "gSay").addArgument(formated_message).publish(new RedisTarget(
					RedisTarget.RedisTargetType.PROXY));
		}
		return false;
	}
	
}
