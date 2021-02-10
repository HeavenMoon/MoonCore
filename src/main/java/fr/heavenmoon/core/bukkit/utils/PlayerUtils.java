package fr.heavenmoon.core.bukkit.utils;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.math.MathUtils;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public class PlayerUtils
{
	
	public static void toggleFly(Player sender)
	{
		CustomPlayer customPlayer =
				MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().getCustomPlayer(RedisKey.PLAYER,
						sender.getUniqueId());
		if (customPlayer.getModerationData().isFly())
		{
			customPlayer.getModerationData().setFly(false);
			sender.setAllowFlight(false);
			(new Message(PrefixType.MODO, "Vous ne pouvez plus voler.")).send(sender);
		}
		else
		{
			customPlayer.getModerationData().setFly(true);
			sender.setAllowFlight(true);
			(new Message(PrefixType.MODO, "Vous pouvez maintenant voler.")).send(sender);
		}
		MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
	}
	
	public static void toggleFly(Player sender, String target)
	{
		if (Bukkit.getPlayer(target) == null)
		{
			(new Message(MessageType.NO_PLAYER)).send(sender);
			return;
		}
		Player targetPlayer = Bukkit.getPlayer(target);
		CustomPlayer customTarget =
				MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().getCustomPlayer(targetPlayer.getName(),
						targetPlayer.getUniqueId());
		if (customTarget.getModerationData().isFly())
		{
			customTarget.getModerationData().setFly(false);
			targetPlayer.setAllowFlight(false);
			new Message(PrefixType.MODO, ChatColor.GRAY + target + ChatColor.LIGHT_PURPLE + " ne peut plus voler.").send(sender);
			new Message(PrefixType.MODO, "Vous ne pouvez plus voler.").send(targetPlayer);
		}
		else
		{
			customTarget.getModerationData().setFly(true);
			targetPlayer.setAllowFlight(true);
			new Message(PrefixType.MODO, ChatColor.GRAY + target + ChatColor.LIGHT_PURPLE + " peut maintenant voler.").send(sender);
			new Message(PrefixType.MODO, "Vous pouvez maintenant voler.").send(targetPlayer);
		}
		MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().commit(RedisKey.PLAYER, customTarget);
	}
	
	public static void killPlayer(CommandSender sender, String name)
	{
		if (Bukkit.getPlayer(name) == null)
		{
			(new Message(MessageType.NO_PLAYER)).send(sender);
			return;
		}
		Player target = Bukkit.getPlayer(name);
		target.damage(10000.0D);
		if (sender instanceof Player)
			(new ActionbarBuilder(ChatColor.RED + "Vous avez tué " + name, 2)).send((Player) sender);
		(new Message(ChatColor.RED + "Vous avez tué " + name)).send(sender);
	}
	
	public static void healPlayer(CommandSender sender, String name)
	{
		if (Bukkit.getPlayer(name) == null)
		{
			(new Message(MessageType.NO_PLAYER)).send(sender);
			return;
		}
		Player target = Bukkit.getPlayer(name);
		target.setHealth(target.getMaxHealth());
		target.setExhaustion(0.0F);
		target.setFoodLevel(20);
		if (sender instanceof Player)
		{
			(new ActionbarBuilder(ChatColor.GREEN + "Vous avez heal " + name, 2)).send((Player) sender);
		}
		else
		{
			(new Message(ChatColor.GREEN + "Vous avez heal " + name)).send(sender);
		}
	}
	
	public static void clear(Player player)
	{
		(((CraftPlayer) player).getHandle()).inventory.b(new NBTTagList());
		player.setFallDistance(0.0F);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.updateInventory();
		for (PotionEffect potion : player.getActivePotionEffects())
			player.removePotionEffect(potion.getType());
	}
	
	public static void clearArmor(Player player)
	{
		player.getInventory().setArmorContents(null);
		player.updateInventory();
	}
	
	public static void setGamemode(Player sender, String gamemode)
	{
		CustomPlayer customPlayer =
				MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().getCustomPlayer(RedisKey.PLAYER,
						sender.getUniqueId());
		if (MathUtils.isInteger(gamemode))
		{
			if (Integer.parseInt(gamemode) > 3 || Integer.parseInt(gamemode) < 0)
			{
				new Message(MessageType.SYNTAXE, "%syntax%", "/gamemode <gamemode> <joueur>").send(sender);
				return;
			}
			sender.setGameMode(GameMode.getByValue(Integer.parseInt(gamemode)));
			customPlayer.setGamemode(Integer.parseInt(gamemode));
			MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
			new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + sender.getGameMode().toString().toUpperCase())
					.send(sender);
		}
		else
		{
			for (GameMode gameMode : GameMode.values())
			{
				if (gamemode.equalsIgnoreCase(gameMode.name()))
				{
					sender.setGameMode(gameMode);
					customPlayer.setGamemode(Integer.parseInt(gamemode));
					MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
					(new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + sender.getGameMode().toString().toUpperCase()))
							.send(sender);
					return;
				}
			}
			new Message(MessageType.SYNTAXE, "%syntax%", "/gamemode <gamemode> <joueur>").send(sender);
		}
	}
	
	public static void setGamemode(Player sender, String gamemode, String target)
	{
		if (Bukkit.getPlayer(target) == null)
		{
			(new Message(MessageType.NO_PLAYER)).send(sender);
			return;
		}
		Player targetPlayer = Bukkit.getPlayer(target);
		CustomPlayer customTarget =
				MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().getCustomPlayer(RedisKey.PLAYER
						, targetPlayer.getUniqueId());
		if (MathUtils.isInteger(gamemode))
		{
			if (Integer.parseInt(gamemode) > 3 || Integer.parseInt(gamemode) < 0)
			{
				(new Message(MessageType.SYNTAXE, "%syntax%", "/gamemode <gamemode> <joueur>")).send(sender);
				return;
			}
			customTarget.setGamemode(Integer.parseInt(gamemode));
			MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().commit(RedisKey.PLAYER, customTarget);
			targetPlayer.setGameMode(GameMode.getByValue(Integer.parseInt(gamemode)));
			(new Message(ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + targetPlayer.getGameMode().toString().toUpperCase()))
					.send(targetPlayer);
			(new Message(
					ChatColor.GRAY + "GameMode mis jour pour " + ChatColor.DARK_GRAY + targetPlayer.getName() + ChatColor.DARK_GRAY + ": " +
							ChatColor.GREEN + targetPlayer
							.getGameMode().toString().toUpperCase())).send(sender);
		}
		else
		{
			for (GameMode gameMode : GameMode.values())
			{
				if (gamemode.equalsIgnoreCase(gameMode.name()))
				{
					targetPlayer.setGameMode(gameMode);
					customTarget.setGamemode(gameMode.getValue());
					MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().commit(RedisKey.PLAYER, customTarget);
					new Message(
							ChatColor.GRAY + "GameMode mis jour: " + ChatColor.GREEN + targetPlayer.getGameMode().toString().toUpperCase())
							.send(targetPlayer);
					new Message(ChatColor.GRAY + "GameMode mis jour pour " + ChatColor.DARK_GRAY + targetPlayer.getName() +
							ChatColor.DARK_GRAY + ": " + ChatColor.GREEN + targetPlayer
							.getGameMode().toString().toUpperCase()).send(sender);
					return;
				}
			}
			new Message(MessageType.SYNTAXE, "%syntax%", "/gamemode <gamemode> <joueur>").send(sender);
		}
	}
	
	public static void getLastConnection(CommandSender sender, String name)
	{
		UUID uuid = BUniqueID.get(name);
		CustomPlayer customPlayer =
				MoonBukkitCore.get().getCommons().getPersistanceManager().getPlayerManager().getCustomPlayer(RedisKey.PLAYER, uuid);
		if (customPlayer.isExist())
		{
			new Message(PrefixType.MODO,
					"Dernière connexion de " + ChatColor.GRAY + name + ChatColor.LIGHT_PURPLE + " le " + ChatColor.AQUA +
							new CustomDate(customPlayer
									.getLastLogin()).getCompleteFormat()).send(sender);
		}
		else
		{
			(new Message(PrefixType.MODO, ChatColor.GRAY + name + ChatColor.LIGHT_PURPLE + " ne s'est jamais connecté")).send(sender);
		}
	}
	
}
