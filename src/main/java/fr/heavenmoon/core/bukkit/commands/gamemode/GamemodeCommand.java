package fr.heavenmoon.core.bukkit.commands.gamemode;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.PlayerUtils;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	RankList rank = RankList.ADMINISTRATEUR;
	
	public GamemodeCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		if (!(sender instanceof Player))
		{
			(new Message(MessageType.CONSOLE)).send(sender);
			return false;
		}
		Player player = (Player) sender;
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (!customPlayer.hasOnlyPermission(this.rank))
		{
			(new Message(MessageType.PERMISSION)).send(sender);
			return false;
		}
		if (args.length == 1)
		{
			String gamemode = args[0];
			PlayerUtils.setGamemode(player, gamemode);
			return true;
		}
		if (args.length == 2)
		{
			String gamemode = args[0];
			String target = args[1];
			PlayerUtils.setGamemode(player, gamemode, target);
			return true;
		}
		return false;
	}
}
