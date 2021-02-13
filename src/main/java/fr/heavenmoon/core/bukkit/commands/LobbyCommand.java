package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.server.CustomServer;
import fr.heavenmoon.persistanceapi.customs.server.ServerType;
import fr.heavenmoon.persistanceapi.managers.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.managers.redis.RedisTarget;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

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
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
		if (args.length != 0)
		{
			new Message(MessageType.SYNTAXE.getContent().replace("%syntax%", "/lobby")).send(customPlayer);
			return false;
		}
		if (!customPlayer.canTp() && !customPlayer.hasPermission(RankList.MODERATEUR))
		{
			new Message(PrefixType.ERROR, "Vous ne pouvez pas vous téléporter maintenant.");
			return false;
		}
		List<CustomServer> lobbyList = persistanceManager.getServerManager().getHubs();
		Random random = new Random();
		String serverName = lobbyList.get(random.nextInt(lobbyList.size())).getName();
		CustomServer current = persistanceManager.getServerManager().getCustomServer(customPlayer.getServerName());
		if (current.getType().equals(ServerType.LOBBY))
		{
			player.performCommand("spawn");
			return true;
		}

		if (customPlayer.hasPermission(RankList.MODERATEUR))
		{
			new RedisPublisher(persistanceManager, "Connect").setArguments(customPlayer.getName(), serverName)
			                                                 .publish(new RedisTarget(RedisTarget.RedisTargetType.PROXY));
			return true;
		}
		new Message(PrefixType.SERVER, "Vous allez être téléporté au lobby.").send(player);
		new Message(PrefixType.SERVER, "Veuillez ne pas vous déplacer durant la téléportation").send(player);
		plugin.getTpManager().startCooldownTp(customPlayer, player.getLocation(), 5, serverName);

		return false;
	}
}