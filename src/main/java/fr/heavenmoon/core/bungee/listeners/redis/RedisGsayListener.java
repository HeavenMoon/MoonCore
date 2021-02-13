package fr.heavenmoon.core.bungee.listeners.redis;

import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.managers.redis.PubSubMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class RedisGsayListener
{
	private final String channel;
	
	private final PubSubMessage pubSubMessage;
	private final PersistanceManager persistanceManager;
	
	public RedisGsayListener(String channel, PubSubMessage pubSubMessage, PersistanceManager persistanceManager)
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
		String message = args.get(0);
		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
		{
			player.sendMessage(TextComponent.fromLegacyText(message));
		}
	}
}
