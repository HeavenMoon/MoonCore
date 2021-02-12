package fr.heavenmoon.core.bukkit.commands.gamemode;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmaCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	RankList rank = RankList.MODERATEUR;
	RankList max_rank = RankList.SUPERMODO;
	
	public GmaCommand(MoonBukkitCore plugin)
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
			new Message(MessageType.PERMISSION).send(sender);
			return false;
		}
		if (args.length == 0)
		{
			customPlayer.setGamemode(Integer.valueOf(2));
            persistanceManager.getPlayerManager().commit(customPlayer);
			player.setGameMode(GameMode.ADVENTURE);
			new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + "ADVENTURE").send(player);
			return true;
		}
		if (args.length == 1)
		{
			String name = args[0];
			if (!customPlayer.hasPermission(this.max_rank))
			{
				new Message(MessageType.PERMISSION).send(sender);
				return false;
			}
			if (Bukkit.getPlayer(name) == null)
			{
				(new Message(MessageType.NO_PLAYER)).send(sender);
				return false;
			}
			Player target = Bukkit.getPlayer(name);
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(target.getUniqueId());
			customTarget.setGamemode(Integer.valueOf(2));
            persistanceManager.getPlayerManager().commit(customPlayer);
			target.setGameMode(GameMode.ADVENTURE);
			new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + "ADVENTURE").send(target);
			new Message(ChatColor.GRAY + "GameMode mis jour pour " + ChatColor.DARK_GRAY + target.getName() + ChatColor.DARK_GRAY + ": " +
					ChatColor.GREEN + "ADVENTURE").send(player);
			return true;
		}
		return false;
	}
}
