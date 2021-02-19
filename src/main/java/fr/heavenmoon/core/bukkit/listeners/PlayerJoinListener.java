package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.scoreboard.ScoreboardTeam;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.builders.misc.TablistBuilder;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.BUILDER;
	
	public PlayerJoinListener(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	@EventHandler
	public void on(PlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
		
		if (customPlayer.getRankData().getPermission() < persistanceManager.getServerManager().getCustomServer(plugin.getCommons().getConfig().getServerName()).getWhitelist().getRank().getPermission())
		{
			player.kickPlayer(persistanceManager.getServerManager().getCustomServer(plugin.getCommons().getConfig().getServerName()).getWhitelist().getDescription());
			return;
		}
		
		initPlayer(player, customPlayer);
		
		new TablistBuilder(player).setTop(ChatColor.BLUE + "Vous êtes connecté sur " + ChatColor.LIGHT_PURPLE + "play.heavenmoon.fr")
		                          .setBottom(
				                          ChatColor.BLUE + "Grades, Kits, Clés disponibles sur " + ChatColor.WHITE + "" + ChatColor.BOLD +
						                          "shop.heavenmoon.fr").changeTablistPlayer();
		// Send team to player
		for (ScoreboardTeam team : plugin.getTeams())
			(((CraftPlayer) Bukkit.getPlayer(event.getPlayer().getUniqueId())).getHandle()).playerConnection.sendPacket(team.createTeam());
		plugin.getScoreboardManager().onLogin(event.getPlayer());
	}
	
	private void initPlayer(Player player, CustomPlayer customPlayer)
	{
		System.out.println("Init " + player.getName() + " " + customPlayer.getUniqueID());
		plugin.getVanishManager().refreshVanish(player);
		plugin.getSpeedManager().refreshSpeed(player);
		
		player.setAllowFlight(customPlayer.getModerationData().isFly());
		player.setGameMode(GameMode.getByValue(customPlayer.getGamemode()));
		customPlayer.setServerName(plugin.getCommons().getConfig().getServerName());
		customPlayer.setOnline(true);
		
		if (customPlayer.getModerationData().isEnable())
			if (customPlayer.getModerationData().isVanish()) plugin.getVanishManager().enableVanish(player);
			else plugin.getVanishManager().disableVanish(player);
		
		persistanceManager.getPlayerManager().commit(customPlayer);
	}
}