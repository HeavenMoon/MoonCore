package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	
	public LobbyCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
		if (args.length == 0)
		{
			if (customPlayer.getServerName().equals("lobby01"))
			{
				Location spawn = new Location(Bukkit.getWorld("lobby"), 44.5D, 88.0D, 11.5D, 0.0F, 0.0F);
				player.teleport(spawn);
				return false;
			}
			if (customPlayer.hasOnlyPermission(RankList.MODERATEUR))
			{
				new RedisPublisher(persistanceManager, "Connect").setArguments(customPlayer.getName(), "lobby01")
				                                                  .publish(new RedisTarget(RedisTarget.RedisTargetType.PROXY));
				return true;
			}
			new Message(PrefixType.SERVER, "Vous allez être téléporté au lobby.").send(player);
			new Message(PrefixType.SERVER, "Veillez ne pas vous déplacer durant la téléportation").send(player);
			plugin.getTpManager().startCooldownTp(customPlayer, player.getLocation(), 5);
		}
		return false;
	}
}