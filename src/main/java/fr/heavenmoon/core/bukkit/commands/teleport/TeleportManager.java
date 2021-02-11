package fr.heavenmoon.core.bukkit.commands.teleport;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportManager
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	
	public TeleportManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public void globalTeleport(CustomPlayer customPlayer, String name)
	{
		UUID uuid = BUniqueID.get(name);
		CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		if (customTarget.isOnline())
		{
			Player target = Bukkit.getPlayer(customTarget.getName());
			if (target != null)
			{
				teleportPlayer(customPlayer, customTarget.getName());
			}
			else
			{
				new RedisPublisher(persistanceManager, "Teleport")
						.setArguments("GlobalTeleport", customPlayer.getName(), customTarget.getName())
						.publish(new RedisTarget(RedisTarget.RedisTargetType.PROXY));
			}
		}
		else
		{
			new Message(PrefixType.ERROR, "Ce joueur n'est pas en ligne.").send(customPlayer);
		}
	}
	
	public void teleportPlayer(CustomPlayer customPlayer, String name)
	{
		Player player = Bukkit.getPlayer(customPlayer.getName());
		if (player == null) return;
        UUID uuid = BUniqueID.get(name);
		CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		Player target = Bukkit.getPlayer(customTarget.getName());
		if (target != null)
		{
			player.teleport(target);
			new Message(PrefixType.SERVER,
					"Vous avez été téléporté sur " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget.getRankData().getPrefix() + customTarget.getName() + ChatColor.GRAY + ".")
					.send(player);
		}
		else if (customTarget.isOnline())
		{
			new Message(ChatColor.RED + "Ce joueur n'est pas sur le même serveur.").send(player);
		}
		else
		{
			new Message(PrefixType.ERROR, "Ce joueur n'est pas en ligne.").send(player);
		}
	}
	
	public void teleportPlayerExternal(CustomPlayer sender, String name, String targetName)
	{
		UUID puuid = BUniqueID.get(name);
        UUID tuuid = BUniqueID.get(targetName);
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(puuid);
		CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(tuuid);
		Player player = Bukkit.getPlayer(name);
		Player target = Bukkit.getPlayer(targetName);
		if (player != null && target != null)
		{
			player.teleport(target);
			(new Message(PrefixType.SERVER,
					"Vous avez été téléporté sur " + ChatColor.getByChar(customPlayer.getRankData().getStyleCode())+ customPlayer.getRankData().getPrefix() + customPlayer.getName() + ChatColor.GRAY +
							", sur " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget
							.getRankData().getPrefix() + customTarget.getName() + ChatColor.GRAY + ".")).send(customPlayer);
		}
		else
		{
			(new Message(PrefixType.ERROR, "Un des deux joueur n'est pas sur le même serveur.")).send(sender);
		}
	}
	
	public void teleportCoords(CustomPlayer customPlayer, double x, double y, double z)
	{
		Player player = Bukkit.getPlayer(customPlayer.getName());
		Location location = new Location(player.getWorld(), x, y, z);
		player.teleport(location);
		new Message(PrefixType.SERVER, ChatColor.GREEN + "Vous avez été téléporté en " + x + " " + y + " " + z + ".").send(player);
	}
	
	public void teleportCoordsExternal(CustomPlayer customPlayer, String name, double x, double y, double z)
	{
		Player player = Bukkit.getPlayer(name);
		if (player != null)
		{
			CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
			Location location = new Location(player.getWorld(), x, y, z);
			player.teleport(location);
			(new Message("avez ten " + x + " " + y + " " + z + ".")).send(player);
			(new Message(PrefixType.SERVER,
					"Le joueur " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget.getRankData().getPrefix() + customTarget.getName() + ChatColor.GRAY + " a été téléporté en " +
							x + " " + y + " " + z + "."))
					.send(customPlayer);
		}
		else
		{
			(new Message(PrefixType.ERROR, "Ce joueur n'est pas en ligne.")).send(customPlayer);
		}
	}
	
	public void teleportHere(CustomPlayer customPlayer, String targetName)
	{
        UUID uuid = BUniqueID.get(targetName);
		CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		Player player = Bukkit.getPlayer(customPlayer.getName());
		Player target = Bukkit.getPlayer(targetName);
		if (target != null)
		{
			target.teleport(player);
			new Message(PrefixType.SERVER,
					"Vous avez téléporté " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget.getRankData().getPrefix() + customTarget.getName() + ChatColor.GRAY + " sur vous.")
					.send(player);
		}
		else if (customTarget.isOnline())
		{
			new Message(PrefixType.ERROR, "Ce joueur n'est pas sur le même serveur.").send(player);
		}
		else
		{
			new Message(PrefixType.ERROR, "Ce joueur n'est pas en ligne.").send(player);
		}
	}
}
