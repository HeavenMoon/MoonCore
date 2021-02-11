package fr.heavenmoon.core.bukkit.scoreboard;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.NumberUtils;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PersonalScoreboard
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private final ObjectiveSign objectiveSign;
	private CustomPlayer customPlayer;
	private Player player;
	
	private RankList rank;
	private long stars;
	private long gemmes;
	
	public PersonalScoreboard(MoonBukkitCore plugin, Player player)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
		this.player = player;
		objectiveSign = new ObjectiveSign("heavenmoon", "Heavenmoon");
		
		reloadData();
		objectiveSign.addReceiver(player);
		refreshTeams();
	}
	
	public void reloadData()
	{
		this.customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(this.player.getUniqueId());
		this.rank = customPlayer.getRankData().getRank();
		this.stars = customPlayer.getStars();
		this.gemmes = customPlayer.getGemmes();
	}
	
	public void setLines(String ip)
	{
		objectiveSign.setDisplayName(
				ChatColor.BOLD + "" + ChatColor.AQUA + "❃" + ChatColor.LIGHT_PURPLE + " Heaven" + ChatColor.DARK_PURPLE + "Moon " +
						ChatColor.AQUA + "❃");
		
		objectiveSign.setLine(0, ChatColor.GRAY + "");
		objectiveSign.setLine(1,
				ChatColor.GRAY + "Compte: " + ChatColor.getByChar(this.rank.getStyleCode()) + customPlayer.getName());
		objectiveSign.setLine(2, ChatColor.GRAY + "Rank: " + ChatColor.getByChar(this.rank.getStyleCode()) + this.rank.getPrefix());
		objectiveSign.setLine(3, ChatColor.GRAY + "Etoiles: " + ChatColor.LIGHT_PURPLE + NumberUtils.format(this.stars) + " ✯");
		objectiveSign.setLine(4, ChatColor.GRAY + "Gemmes: " + ChatColor.DARK_PURPLE + NumberUtils.format(this.gemmes) + " ◈");
		objectiveSign.setLine(5, ChatColor.AQUA + "");
//        objectiveSign.setLine(5, ChatColor.AQUA + " ⚔ " + ChatColor.DARK_GRAY + "⊱ " + ChatColor.DARK_PURPLE + "Faction");
//        objectiveSign.setLine(6, ChatColor.LIGHT_PURPLE + "");

//        objectiveSign.setLine(10, ChatColor.WHITE + " ➤ " + ChatColor.AQUA + "Nom " + ChatColor.DARK_GRAY + "↠ " + ChatColor.LIGHT_PURPLE + faction.getName());
//        objectiveSign.setLine(11, ChatColor.WHITE + " ➤ " + ChatColor.AQUA + "Membres " + ChatColor.DARK_GRAY + "↠ " + ChatColor.LIGHT_PURPLE + faction.getOnlinePlayers().size() + "/" + faction.getMPlayers().size());
//        objectiveSign.setLine(12, ChatColor.WHITE + " ➤ " + ChatColor.AQUA + "Power " + ChatColor.DARK_GRAY + "↠ " + ChatColor.LIGHT_PURPLE + faction.getPower() + "/" + faction.getPowerMax());
//        objectiveSign.setLine(13, ChatColor.DARK_PURPLE + "");
//        objectiveSign.setLine(14, ChatColor.AQUA + ip);
//        objectiveSign.setLine(7, ChatColor.WHITE + " ➤ " + ChatColor.AQUA + "Nom " + ChatColor.DARK_GRAY + "↠ " + ChatColor.YELLOW + "Aucune");
//        objectiveSign.setLine(8, ChatColor.DARK_PURPLE + "");
		objectiveSign.setLine(6, ChatColor.AQUA + ip);
		
		objectiveSign.updateLines();
	}
	
	public void onLogout()
	{
		objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(player.getUniqueId()));
	}
	
	public void refreshTeams()
	{
		for (Player p1 : Bukkit.getOnlinePlayers())
		{
			for (Player p2 : Bukkit.getOnlinePlayers())
			{
				ScoreboardTeam team =
						plugin.getSbTeam("" + Arrays.stream(RankList.values())
						                            .filter(r -> persistanceManager.getPlayerManager().getCustomPlayer(		                            p2.getUniqueId()).getRankData().getRank() == r).findAny()
						                            .orElse(RankList.EXPLORATEUR).getOrder());
				
				if (team != null)
				{
					((CraftPlayer) p1).getHandle().playerConnection.sendPacket(team.addOrRemovePlayer(3, p2.getName()));
				}
			}
		}
	}
}