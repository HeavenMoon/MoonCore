package fr.heavenmoon.core.bukkit.info;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.gui.AbstractGui;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.guild.GuildRankList;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.builders.items.HeadBuilder;
import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfoGUI extends AbstractGui
{
	
	private String targetName;
	
	public InfoGUI(MoonBukkitCore plugin, String target)
	{
		super(plugin);
		this.targetName = target;
	}
	
	public void display(Player player)
	{
		this.inventory = plugin.getServer().createInventory(null, 54, "Infos : " + targetName);
		UUID uuid = BUniqueID.get(targetName);
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		setSlotData(ChatColor.getByChar(customPlayer.getRankData().getChatStyleCode()) + customPlayer.getName(),
				new HeadBuilder().setOwner(customPlayer.getName()).build(), 13, makePlayerItemsLore(customPlayer), null);
		setSlotData(ChatColor.GOLD + "Caractéristiques", Material.NETHER_STAR, 20, makePlayerStatsLore(customPlayer), null);
		setSlotData(ChatColor.GOLD + "Grade", Material.DIAMOND_CHESTPLATE, 21, makePlayerRankLore(customPlayer), null);
		setSlotData(ChatColor.GOLD + "Modération", Material.WATCH, 22, makePlayerModLore(customPlayer), null);
		setSlotData(ChatColor.GOLD + "Profile", Material.EMERALD, 23, makePlayerProfileLore(customPlayer), null);
		setSlotData(ChatColor.GOLD + "Stats de connexion", Material.IRON_INGOT, 24, makePlayerConnectingStatsLore(customPlayer), null);
		setSlotData(ChatColor.GOLD + "Guild", Material.IRON_SWORD, 31, makePlayerGuildLore(customPlayer), null);
		for (int i = 36; i < 45; i++)
			setSlotData(ChatColor.BOLD + "Deco", (new ItemBuilder(Material.STAINED_GLASS_PANE)).setData((byte) 15).toItemStack(), i, null,
					null);
		setSlotData(ChatColor.LIGHT_PURPLE + "Informations", Material.BOOK, 49,
				new String[]{ChatColor.GRAY + "Vous disposez de toutes les", ChatColor.GRAY + "informations concernant:",
						ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer
						.getRankData().getRank().getPrefix() + this.targetName}, null);
		setSlotData(ChatColor.RED + "Fermer l'inventaire", Material.BARRIER, 53, null, "close");
		plugin.getServer().getScheduler().runTask(plugin, () -> player.openInventory(this.inventory));
	}
	
	public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
	{
		if (action.equalsIgnoreCase("close"))
			plugin.getGuiManager().closeGui(player);
	}
	
	private String[] makePlayerItemsLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Inscrit: " + (customPlayer.isExist() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Connecté: " + (customPlayer.isOnline() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Premium: " + (customPlayer.isPremium() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "AzLauncher: " + (customPlayer.isAzlauncher() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Stars: " + ChatColor.DARK_PURPLE + customPlayer.getStars());
		lore.add(ChatColor.GRAY + "Gemmes: " + ChatColor.DARK_PURPLE + customPlayer.getGemmes());
		lore.add(
				ChatColor.GRAY + "Double comptes: " + ChatColor.AQUA + (plugin.getAltsManager().getAlts(customPlayer).keySet().size() - 1));
		lore.add(ChatColor.GRAY + "Adresse(s) IP: " + ChatColor.AQUA + customPlayer.getAllAddress().size());
		return lore.toArray(loreArray);
	}
	
	private String[] makePlayerStatsLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Speed: " + (customPlayer.getModerationData().isSpeed() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Fly: " + (customPlayer.getModerationData().isFly() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		String gameMode = GameMode.getByValue(customPlayer.getGamemode()).name();
		lore.add(ChatColor.GRAY + "Mode de jeu: " + ChatColor.LIGHT_PURPLE + gameMode.toUpperCase());
		lore.add(ChatColor.GRAY + "Vanish: " + (customPlayer.getModerationData().isVanish() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Freeze: " + (customPlayer.getModerationData().isFreeze() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Proxy: " + ChatColor.BLUE + customPlayer.getProxyName());
		lore.add(ChatColor.GRAY + "Server: " + ChatColor.BLUE + customPlayer.getServerName());
		return lore.toArray(loreArray);
	}
	
	private String[] makePlayerRankLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Grade: " + ChatColor.getByChar(customPlayer.getRankData().getRank().getStyleCode()) +
				customPlayer.getRankData().getRank().getName());
		lore.add(ChatColor.GRAY + "Permission: " + ChatColor.getByChar(RankList.getByPermission(customPlayer.getRankData().getPermission()).getStyleCode()) +
				RankList.getByPermission(customPlayer.getRankData()
				                                     .getPermission()).getName() + ChatColor.GRAY + ", (" + ChatColor.BLUE +
				customPlayer.getRankData().getPermission() + ChatColor.GRAY + ")");
		lore.add(ChatColor.GRAY + "Style: " + ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + "Like that !");
		lore.add(ChatColor.GRAY + "Prefix: " + ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getPrefix());
		lore.add(ChatColor.GRAY + "Suffix: " + ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getSuffix());
		lore.add(ChatColor.GRAY + "Chat (couleur): " + ChatColor.getByChar(customPlayer.getRankData().getChatStyleCode()) + "Like that !");
		return lore.toArray(loreArray);
	}
	
	private String[] makePlayerGuildLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Guild: " + ChatColor.BLUE + customPlayer.getGuild().getGuild().getName());
		lore.add(ChatColor.GRAY + "Grade: " + ChatColor.BLUE + customPlayer.getGuild().getRank().getName());
		if (customPlayer.getGuild().getRank() != GuildRankList.AUCUN)
		{
			lore.add(ChatColor.GRAY + "Style: " + ChatColor.getByChar(customPlayer.getGuild().getRank().getStyle()) + "Like that !");
			lore.add(ChatColor.GRAY + "Title: " + customPlayer.getGuild().getRank().getStyle() + customPlayer.getGuild().getRank().getTitle());
		}
		return lore.toArray(loreArray);
	}
	
	private String[] makePlayerModLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Mode modération: " +
				(customPlayer.getModerationData().isEnable() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Mode bypass: " +
				(customPlayer.getModerationData().isBypass() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Banni: " + (persistanceManager.getSanctionManager().isBanned(customPlayer) ? (ChatColor.GREEN + "Oui") :
				(ChatColor.RED +
				"Non")));
		lore.add(ChatColor.GRAY + "Réduit au silence: " +
				(persistanceManager.getSanctionManager().isMuted(customPlayer) ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		return lore.toArray(loreArray);
	}
	
	private String[] makePlayerProfileLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Faction comming soon !");
		return lore.toArray(loreArray);
	}
	
	private String[] makePlayerConnectingStatsLore(CustomPlayer customPlayer)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Inscription: " + ChatColor.YELLOW +
				new CustomDate(customPlayer.getFirstLogin()).getCompleteFormat());
		lore.add(ChatColor.GRAY + "Dernière connexion: " + ChatColor.GREEN +
				new CustomDate(customPlayer.getLastLogin()).getCompleteFormat());
		long time = customPlayer.getTimePlayed();
		lore.add(ChatColor.GRAY + "Temps de jeu: " + ChatColor.AQUA + new CustomDate(time).getCleanFormat(time));
		return lore.toArray(loreArray);
	}
}