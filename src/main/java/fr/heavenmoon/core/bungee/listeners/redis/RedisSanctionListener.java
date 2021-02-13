package fr.heavenmoon.core.bungee.listeners.redis;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.bungee.utils.SanctionUtils;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.managers.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class RedisSanctionListener
{
	private final String channel;
	
	private final PubSubMessage pubSubMessage;
	private final PersistanceManager persistanceManager;
	
	public RedisSanctionListener(String channel, PubSubMessage pubSubMessage, PersistanceManager persistanceManager)
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
		if (args.get(0).equalsIgnoreCase("BanAdd")) {
			String uniqueId = args.get(1);
			String moderatorUniqueId = args.get(2);
			String reason = args.get(3);
			Long apply = Long.valueOf(Long.parseLong(args.get(4)));
			Long until = Long.valueOf(Long.parseLong(args.get(5)));
			
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(UUID.fromString(uniqueId));
			CustomPlayer customModerator = persistanceManager.getPlayerManager().getCustomPlayer(UUID.fromString(moderatorUniqueId));
			
			if (customPlayer.isOnline()) {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(customPlayer.getUniqueID());
				if (player == null)
					return;
				String banMessage = MoonBungeeCore.get().getSanctionUtils().getBanReason(customModerator.getName(), reason, apply.longValue(),
						until.longValue());
				customPlayer.setOnline(false);
				persistanceManager.getPlayerManager().commit(customPlayer);
				player.disconnect(TextComponent.fromLegacyText(banMessage));
				persistanceManager.getPlayerManager().update(customPlayer);
				persistanceManager.getPlayerManager().remove(customPlayer);
			} else {
				persistanceManager.getPlayerManager().update(customPlayer);
				persistanceManager.getPlayerManager().remove(customPlayer);
			}
		}
		if (args.get(0).equalsIgnoreCase("MuteAdd")) {
			String uniqueId = args.get(1);
			String moderatorUniqueId = args.get(2);
			String reason = args.get(3);
			Double time = Double.valueOf(Double.parseDouble(args.get(4)));
			Long apply = Long.valueOf(Long.parseLong(args.get(5)));
			Long until = Long.valueOf(Long.parseLong(args.get(6)));
			
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(UUID.fromString(uniqueId));
			
			String muteMessage = ChatColor.RED + "Vous avez été réduit au silence pendant " + ChatColor.AQUA + time + " minute(s) " + ChatColor.RED + "pour " + ChatColor.AQUA + reason + ChatColor.RED + ".";
			if (customPlayer.isOnline()) {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(customPlayer.getUniqueID());
				if (player == null)
					return;
				new Message(muteMessage).send(player);
			} else {
				persistanceManager.getPlayerManager().update(customPlayer);
				persistanceManager.getPlayerManager().removeFromCache(customPlayer);
				persistanceManager.getPlayerManager().remove(customPlayer);
			}
			return;
		}
		if (args.get(0).equalsIgnoreCase("KickAdd")) {
			String uniqueId = args.get(1);
			String moderatorUniqueId = args.get(2);
			String reason = args.get(3);
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(UUID.fromString(uniqueId));
			CustomPlayer customModerator = persistanceManager.getPlayerManager().getCustomPlayer(UUID.fromString(moderatorUniqueId));
			if (customPlayer.isOnline()) {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(customPlayer.getUniqueID());
				if (player == null)
					return;
				String kickMessage = MoonBungeeCore.get().getSanctionUtils().getKickReason(customModerator.getName(), reason);
				customPlayer.setOnline(false);
				persistanceManager.getPlayerManager().commit(customPlayer);
				
				player.disconnect(TextComponent.fromLegacyText(kickMessage));
				persistanceManager.getPlayerManager().update(customPlayer);
				persistanceManager.getPlayerManager().removeFromCache(customPlayer);
				persistanceManager.getPlayerManager().remove(customPlayer);
			}
			
		}
	}
}
