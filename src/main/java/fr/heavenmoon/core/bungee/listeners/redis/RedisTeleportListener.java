package fr.heavenmoon.core.bungee.listeners.redis;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.persistanceapi.customs.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class RedisTeleportListener
{
	private final String channel;
	
	private final PubSubMessage pubSubMessage;
	
	private final PersistanceManager persistanceManager;
	
	public RedisTeleportListener(String channel, PubSubMessage pubSubMessage, PersistanceManager persistanceManager)
	{
		this.channel = channel;
		this.pubSubMessage = pubSubMessage;
		this.persistanceManager = persistanceManager;
		listen();
	}
	
	private void listen()
	{
		List<String> args = this.pubSubMessage.getArguments();
		if (args.get(0).equalsIgnoreCase("GlobalTeleport"))
		{
			RedisBungeeAPI redisBungeeAPI = RedisBungee.getApi();
			String playername = args.get(1);
			String targetname = args.get(2);
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
			
			if (redisBungeeAPI.getUuidFromName(targetname) == null) return;
			
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(redisBungeeAPI.getUuidFromName(targetname));
			if (customPlayer.getServerName() != customTarget.getServerName())
			{
				player.connect(ProxyServer.getInstance().getServerInfo(customTarget.getServerName()));
			}
			new RedisPublisher(persistanceManager, "Teleport")
					.setArguments("Teleport", playername, targetname)
					.publish(new RedisTarget(RedisTarget.RedisTargetType.SERVER));
		}
	}
}