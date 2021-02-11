package fr.heavenmoon.core.bukkit.guild;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.guild.GuildRankList;
import fr.heavenmoon.persistanceapi.customs.guild.GuildUnit;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildManager
{
	
	private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
	
	public GuildManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;

		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public void setGuild(Player sender, String target, String guild)
	{
        UUID uuid = BUniqueID.get(target);
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		GuildUnit guildUnit = GuildUnit.getByName(guild);
		if (guildUnit == null)
		{
			new Message(PrefixType.ERROR, "Cette guild n'existe paas.");
			return;
		}
		customPlayer.getGuild().setGuild(guildUnit);
        persistanceManager.getPlayerManager().commit(customPlayer);
		new Message(PrefixType.SERVER,
				"La guild de " + ChatColor.DARK_GRAY + target + ChatColor.GRAY + " est maintenant " + ChatColor.BLUE + guildUnit.getName())
				.send(sender);
	}
	
	public void setGuildRank(Player sender, String target, String guildRank)
	{
        UUID uuid = BUniqueID.get(target);
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		GuildRankList rank = GuildRankList.getByName(guildRank);
		if (rank == null)
		{
			new Message(PrefixType.ERROR, "Cette rang n'existe paas.");
			return;
		}
		customPlayer.getGuild().setRank(rank);
        persistanceManager.getPlayerManager().commit(customPlayer);
		new Message(PrefixType.SERVER,
				"La rang de guild de " + ChatColor.DARK_GRAY + target + ChatColor.GRAY + " est maintenant " + rank.getStyle() +
						rank.getName())
				.send(sender);
	}
}