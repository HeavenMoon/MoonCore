package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.guild.GuildUnit;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.style.StyleList;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.persistanceapi.customs.player.CustomSanction;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public AsyncPlayerChatListener(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	@EventHandler
	public void on(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
		String rankPrefix = ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getPrefix();
		net.md_5.bungee.api.ChatColor chat = net.md_5.bungee.api.ChatColor.getByChar(customPlayer.getRankData().getChatStyleCode());
		String message = event.getMessage().replace("<3", "❤");
		if (persistanceManager.getSanctionManager().isMuted(customPlayer))
		{
            CustomSanction customSanction = persistanceManager.getSanctionManager().getCurrentCustomSanction(customPlayer);
			long time = ((customSanction.getExpirationTime() - customSanction.getCreationTime()) / 60000);
			
			if (customSanction.isValid())
			{
				new Message(ChatColor.GRAY + "Tu a été réduit au silence pendant " + ChatColor.LIGHT_PURPLE + time + " minute(s)" +
						ChatColor.GRAY + ", raison: " + ChatColor.LIGHT_PURPLE + customSanction.getReason() + ChatColor.GRAY +
						", temps restant: " + ChatColor.LIGHT_PURPLE + new CustomDate(
						System.currentTimeMillis()).getDurationUntil(customSanction.getExpirationTime())).send(player);
				event.setCancelled(true);
				return;
			}
			else
			{
				plugin.getMuteManager().muteRemove(customPlayer.getName());
			}
		}
		if (customPlayer.hasPermission(RankList.ADMINISTRATEUR))
			message = ChatColor.translateAlternateColorCodes('&', message);
		
		//ChatFormat
		TextComponent chatFormat = new TextComponent("");
		TextComponent report = new TextComponent(ChatColor.RED + "⚠");
		report.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "Signaler " + player.getName()).create()));
		report.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message));
		TextComponent space = new TextComponent(" ");
		TextComponent prefix = new TextComponent(rankPrefix + player.getName() + ChatColor.GRAY + " » ");
		if (customPlayer.getGuild().getGuild() == GuildUnit.Aucune)
			prefix.setHoverEvent(
					new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GOLD + "Aucune Guild").create()));
		else
			prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
					customPlayer.getGuild().getGuild().getPrefix() + ChatColor.GRAY + ": " + customPlayer.getGuild().getRank().getTitle())
					.create()));
		TextComponent chatMessage = new TextComponent(message);
		chatMessage.setColor(chat);
		
		if (!customPlayer.hasPermission(RankList.ADMINISTRATEUR))
		{
			chatFormat.addExtra(report);
			chatFormat.addExtra(space);
			
		}
		chatFormat.addExtra(prefix);
		chatFormat.addExtra(chatMessage);
		
		Bukkit.getOnlinePlayers().forEach(p ->
		{
			p.spigot().sendMessage(chatFormat);
			System.out.println(chatFormat.toString());
		});
		
		event.setCancelled(true);
		
	}
}
