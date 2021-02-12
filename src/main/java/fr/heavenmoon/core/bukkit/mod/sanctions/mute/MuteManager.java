package fr.heavenmoon.core.bukkit.mod.sanctions.mute;

import com.google.gson.Gson;
import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.player.CustomSanction;
import fr.heavenmoon.persistanceapi.customs.player.SanctionType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.core.common.utils.math.MathUtils;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.core.common.utils.time.DateUnity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class MuteManager
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public MuteManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public void muteAdd(CommandSender sender, String name, String reason, String time)
	{
		muteAdd(sender, name, reason, time, new HashMap<>());
	}
	
	public void muteAdd(CommandSender sender, String name, String reason, String time, Map<String, List<String>> evidences)
	{
		if (sender instanceof Player)
		{
			UUID uuid = BUniqueID.get(name);
			CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
			CustomPlayer customModerator = persistanceManager.getPlayerManager().getCustomPlayer(((Player) sender).getUniqueId());
			if (persistanceManager.getSanctionManager().isMuted(customPlayer))
			{
				new Message(PrefixType.ERROR, "Ce joueur est déjà réduit au silence.").send(sender);
				return;
			}
			
			if (persistanceManager.getSanctionManager().isBanned(customPlayer))
			{
				new Message(PrefixType.ERROR, "Ce joueur est banni.").send(sender);
				return;
			}
			
            DateUnity dateUnity = DateUnity.contains(time);
            if (dateUnity == null)
            {
                new Message(PrefixType.ERROR, "Unité de temps invalide").send(sender);
                return;
            }
            String newTime = time.replace(dateUnity.getName(), "");
            if (!MathUtils.isDouble(newTime))
            {
                new Message(PrefixType.ERROR, "Durée invalide").send(sender);
                return;
            }
			
			if (dateUnity == null)
			{
				new Message(PrefixType.ERROR, "Cette unitée de temps n'est pas valide.").send(sender);
				return;
			}
			
			newTime = time.replace(dateUnity.getName(), "");
			
			if (!MathUtils.isDouble(newTime))
			{
				new Message(PrefixType.ERROR, "Cette durée n'est pas valide.").send(sender);
				return;
			}
			
			double finalTime = Double.parseDouble(newTime);
			
			if (!customModerator.hasPermission(customPlayer.getRankData().getPermission()))
			{
				new Message(PrefixType.ERROR, "Vous ne pouvez pas sanctionner ce joueur.").send(sender);
				return;
			}
			
			UUID sanctionid = UUID.randomUUID();
			long apply = System.currentTimeMillis();
			long until = new CustomDate(System.currentTimeMillis()).addTime(dateUnity, finalTime).getMillis();
			
			CustomSanction customSanction = new CustomSanction(sanctionid, customPlayer.getUniqueID(), SanctionType.MUTE, reason,
					new ArrayList<>(), customModerator.getUniqueID(), until, false, apply);
			
			if (this.persistanceManager.getSanctionManager().applySanction(SanctionType.MUTE, customSanction))
            {
	            customPlayer.getModerationData().setCurrentSanctionId(sanctionid.toString());
	            persistanceManager.getPlayerManager().commit(customPlayer);
	            
                new Message(PrefixType.MODO,
                        "Le joueur " + ChatColor.GRAY + customPlayer.getName() + ChatColor.LIGHT_PURPLE + " est réduit au silence.")
                        .send(sender);
                new RedisPublisher(persistanceManager, "Sanction").setArguments("MuteAdd", customPlayer.getUniqueID().toString(),
		                customModerator.getUniqueID().toString(), reason, newTime,
		                String.valueOf(apply), String.valueOf(until)).publish(new RedisTarget(RedisTarget.RedisTargetType.PROXY));
            }
		}
	}
	
	public void muteRemove(CommandSender sender, String name)
	{
		if (sender instanceof Player)
		{
            UUID uuid = BUniqueID.get(name);
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
            CustomPlayer customModerator = persistanceManager.getPlayerManager().getCustomPlayer(((Player) sender).getUniqueId());
            
			if (!persistanceManager.getSanctionManager().isMuted(customPlayer))
			{
				new Message(PrefixType.ERROR, "Ce joueur n'est pas réduit au silence.").send(sender);
				return;
			}
			CustomSanction customSanction = persistanceManager.getSanctionManager().getCurrentCustomSanction(customPlayer);
            CustomPlayer customPunisher = persistanceManager.getPlayerManager().getCustomPlayer(customSanction.getPunisherUuid());
			if (customPunisher.getRankData().getPermission() > customModerator.getRankData().getPermission())
			{
				new Message(PrefixType.ERROR, "Vous ne pouvez pas dé-mute ce joueur.").send(sender);
				return;
			}
			else if (!customPunisher.getUniqueID().equals(customModerator.getUniqueID()))
			{
				new Message(PrefixType.ERROR, "Vous ne pouvez pas dé-mute ce joueur.").send(sender);
				return;
			}
			
			customPlayer.getModerationData().setCurrentSanctionId("null");
			persistanceManager.getPlayerManager().commit(customPlayer);
			
			if (persistanceManager.getSanctionManager().cancelSanction(customSanction, true))
			{
				new Message(PrefixType.MODO,
						"Le joueur " + ChatColor.GRAY + customPlayer.getName() + ChatColor.LIGHT_PURPLE + " n'est plus réduit au silence.")
						.send(sender);
				if (customPlayer.isOnline())
					new Message(PrefixType.SERVER, "Vous n'êtes plus réduit au silence.").send(customPlayer);
			}
		}
	}
	
	public void muteRemove(CustomPlayer customPlayer)
	{
        if (!persistanceManager.getSanctionManager().isMuted(customPlayer)) return;
        
        CustomSanction customSanction = persistanceManager.getSanctionManager().getCurrentCustomSanction(customPlayer);
        
		customPlayer.getModerationData().setCurrentSanctionId("null");
		persistanceManager.getPlayerManager().commit(customPlayer);
		
        if (persistanceManager.getSanctionManager().cancelSanction(customSanction, false)) System.out.println("Sanction type " + customSanction.getType() + " cancelled");
        

	}
	
//	public void muteStatus(CommandSender sender, String name)
//	{
//		if (sender instanceof Player)
//		{
//			String uuid = BUniqueID.get(name);
//			CustomPlayer customPlayer = plugin.getCommons().getPlayerManager().get(name, uuid);
//			if (!customPlayer.getMod().isMuted())
//			{
//				new Message(PrefixType.MODO, "Ce joueur n'est pas réduit au silence.").send(sender);
//				return;
//			}
//
//			CustomMute mute = get(customPlayer.getMod().getSanctions().getId());
//			Arrays.asList(FormatUtils.graySpacer(),
//					FormatUtils.center(ChatColor.RED + "Sanction du joueur " + customPlayer.getName() + "."),
//					FormatUtils.spacesSpacer(),
//					FormatUtils.center(ChatColor.GRAY + "Sanctionnée pour: " + ChatColor.AQUA + mute.getReason()),
//					FormatUtils.center(ChatColor.GRAY + "Sanctionnée par: " + ChatColor.GREEN + mute.getAuthor()),
//					FormatUtils.center(ChatColor.GRAY + "Sanctionnée le: " + ChatColor.YELLOW +
//							new CustomDate(mute.getApply()).getCompleteFormat()),
//					FormatUtils.center(ChatColor.GRAY + "Sanctionnée jusqu'au: " + ChatColor.YELLOW +
//							new CustomDate(mute.getUntil()).getCompleteFormat()),
//					FormatUtils.center(ChatColor.GRAY + "Temps restant: " + ChatColor.LIGHT_PURPLE +
//							new CustomDate(System.currentTimeMillis()).getDurationUntil(mute.getUntil())),
//					FormatUtils.spacesSpacer(),
//					FormatUtils.graySpacer()).forEach(msg -> new Message(msg).send(sender));
//		}
//	}
	
}