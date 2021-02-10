package fr.heavenmoon.core.bukkit.commands.mod;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModoCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.MODERATEUR;
	private String syntax = "/modo <tools|message>";
	
	public ModoCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
	{
		if (!(sender instanceof Player))
		{
			new Message(MessageType.CONSOLE).send(sender);
			return false;
		}
		Player player = (Player) sender;
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (!customPlayer.hasOnlyPermission(this.rank))
		{
			(new Message(MessageType.PERMISSION, "%rank%", this.rank.getName())).send(sender);
			return false;
		}
		if (args.length == 0)
		{
			plugin.getModManager().toggleMod(customPlayer);
		}
		else if ((args.length == 1 && args[0].equalsIgnoreCase("tools")) || (args.length == 1 && args[0].equalsIgnoreCase("t")))
		{
			if (customPlayer.getModerationData().isTools())
			{
				player.getInventory().clear();
				customPlayer.getModerationData().setTools(false);
				persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
				return true;
			}
			plugin.getModManager().getItems(player);
			customPlayer.getModerationData().setTools(true);
			persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
		}
		else
		{
			(new Message(MessageType.SYNTAXE, "%syntax%", this.syntax)).send(sender);
		}
		return false;
	}
}