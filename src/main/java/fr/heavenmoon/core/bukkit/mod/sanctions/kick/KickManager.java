package fr.heavenmoon.core.bukkit.mod.sanctions.kick;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickManager
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public KickManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public void kickAdd(CommandSender sender, String name, String reason)
	{
		if (sender instanceof Player)
		{
			UUID uuid = BUniqueID.get(name);
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
			CustomPlayer customModerator = persistanceManager.getPlayerManager().getCustomPlayer(((Player) sender).getUniqueId());
			if (!customPlayer.isOnline())
			{
				new Message(PrefixType.ERROR, "Ce joueur n'est pas en ligne.").send(sender);
				return;
			}
			if (!customModerator.hasPermission(customPlayer.getRankData().getPermission()))
			{
				new Message(PrefixType.ERROR, "Vous ne pouvez pas sanctionner ce joueur.").send(sender);
				return;
			}
			String author = ((Player) sender).getUniqueId().toString();
			
			
			if (customPlayer.isOnline())
				new RedisPublisher(persistanceManager, "Sanction").setArguments("KickAdd", customPlayer.getUniqueID().toString(), author, reason)
				                                                   .publish(new RedisTarget(RedisTarget.RedisTargetType.PROXY));
            
            new Message(PrefixType.MODO,
                    "Le joueur " + ChatColor.GRAY + customPlayer.getName() + ChatColor.LIGHT_PURPLE + " a été éjecté du serveur.")
                    .send(sender);
		}
	}
}
