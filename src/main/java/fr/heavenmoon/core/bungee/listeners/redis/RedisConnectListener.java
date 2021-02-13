package fr.heavenmoon.core.bungee.listeners.redis;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.common.MoonCommons;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.managers.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.server.CustomServer;
import fr.heavenmoon.persistanceapi.customs.server.ServerStatus;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.Socket;
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
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args.get(0));
		if (player == null)
		{
			System.out.println("wrong proxy, canceling...");
			return;
		}
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
		String server = args.get(1);
		if (!persistanceManager.getServerManager().exist(server))
		{
			new Message(PrefixType.ERROR, "Le serveur est éteint").send(player);
			return;
		}
		CustomServer customServer = persistanceManager.getServerManager().getCustomServer(server);
		if (!customServer.getStatus().equals(ServerStatus.STARTED))
		{
			new Message(PrefixType.ERROR, "Le serveur est éteint").send(player);
			return;
		}
		if (player.getServer().getInfo().getName().equalsIgnoreCase(server)) {
			new Message(PrefixType.ERROR, "Tu es déjà connecté sur ce serveur.").send(player);
			return;
		}
		if (customPlayer.getProxyName().equalsIgnoreCase(MoonBungeeCore.get().getCommons().getConfig().getServerName()) &&
				customPlayer.isOnline())
		{
			customPlayer.setServerName(server);
			this.persistanceManager.getPlayerManager().commit(customPlayer);
			player.connect(ProxyServer.getInstance().getServerInfo(server));
		}
	}
	
}