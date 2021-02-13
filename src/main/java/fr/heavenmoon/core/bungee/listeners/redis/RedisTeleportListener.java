package fr.heavenmoon.core.bungee.listeners.redis;


import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.UniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.managers.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.managers.redis.RedisTarget;
import fr.heavenmoon.persistanceapi.managers.redis.RedisPublisher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
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
			String playername = args.get(1);
			String targetname = args.get(2);
			
			if (!ProxyServer.getInstance().getPlayer(playername).isConnected())
			{
				System.out.println("wrong proxy canceling...");
				return;
			}
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(UniqueID.get(targetname));
			if (customTarget.isOnline() && customTarget.getServerName() != "null")
			{
				ServerInfo info = ProxyServer.getInstance().getServerInfo(customTarget.getServerName());
				player.connect(info);
				new RedisPublisher(persistanceManager, "Teleport")
						.setArguments("SimpleTeleport", playername, targetname)
						.publish(new RedisTarget(RedisTarget.RedisTargetType.SERVER));
			}
			else
			{
				new Message(PrefixType.ERROR, "Le joueur est sur un serveur injoignabl.");
			}
		}
	}
}