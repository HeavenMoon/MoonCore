package fr.heavenmoon.core.bukkit.listeners.redis;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.customs.server.CustomServer;
import fr.heavenmoon.persistanceapi.customs.server.ServerStatus;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class RedisConnectListener
{
	private final String channel;
	
	private final PubSubMessage pubSubMessage;
	private final PersistanceManager persistanceManager;
	
	public RedisConnectListener(String channel, PubSubMessage pubSubMessage, PersistanceManager persistanceManager)
	{
		this.channel = channel;
		this.pubSubMessage = pubSubMessage;
		this.persistanceManager = persistanceManager;
		listen();
	}
	
	private void listen()
	{
		System.out.println("received packet");
		System.out.println(channel + " " + pubSubMessage.getTitle() + " " + pubSubMessage.getArguments());
		List<String> args = this.pubSubMessage.getArguments();
		if (args.get(0).equalsIgnoreCase("Teleport")) {
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MoonBukkitCore.get(), () -> {
				Player player = Bukkit.getPlayer(args.get(1));
				Player target = Bukkit.getPlayer(args.get(2));
				if (player == null || target == null) return;
				player.teleport(target);
			}, 4L);
		}
	}
	
}