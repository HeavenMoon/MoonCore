package fr.heavenmoon.core.bukkit.alts;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.gui.AbstractGui;
import fr.heavenmoon.core.bukkit.info.InfoGUI;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.utils.builders.items.HeadBuilder;
import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.core.common.utils.wrappers.LambdaWrapper;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AltsGUI extends AbstractGui
{
	
	Map<String, Integer> alts;
	private String targetName;
	
	public AltsGUI(MoonBukkitCore plugin, String target)
	{
		super(plugin);
		this.targetName = target;
	}
	
	public void display(Player player)
	{
		this.inventory = plugin.getServer().createInventory(null, 54, "Alts > " + targetName);
		UUID uuid = BUniqueID.get(targetName);
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		if (this.targetName.matches("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$"))
		{
			alts = plugin.getAltsManager().getAlts(targetName);
		}
		else
		{
			alts = plugin.getAltsManager().getAlts(customPlayer);
		}
		LambdaWrapper<Integer> slot = new LambdaWrapper(0);
		alts.keySet().forEach(alt ->
		{
			UUID auuid = BUniqueID.get(alt);
			CustomPlayer altPlayer = persistanceManager.getPlayerManager().getCustomPlayer(auuid);
			setSlotData(ChatColor.getByChar(altPlayer.getRankData().getStyleCode()) + altPlayer.getRankData().getRank().getPrefix() +
							altPlayer.getName(),
					new HeadBuilder().setOwner(altPlayer.getName()).build(), slot.getData(), makePlayerAltLore(altPlayer, alt),
					"" + altPlayer.getName());
			slot.setData(slot.getData() + 1);
		});
		for (int i = 36; i < 45; i++)
			setSlotData(ChatColor.BOLD + "Deco", (new ItemBuilder(Material.STAINED_GLASS_PANE)).setData((byte) 15).toItemStack(), i, null,
					null);
		setSlotData(ChatColor.LIGHT_PURPLE + "Informations", Material.BOOK, 49, new String[]{ChatColor.GRAY + "Tous les alts disponibles " +
				"concernant:",
				ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getRank().getPrefix() +
						targetName}, null);
		setSlotData(ChatColor.RED + "Fermer l'inventaire", Material.BARRIER, 53, null, "close");
		plugin.getServer().getScheduler().runTask(plugin, () -> player.openInventory(this.inventory));
	}
	
	public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
	{
		if (action.equalsIgnoreCase("close"))
		{
			plugin.getGuiManager().closeGui(player);
		}
		else
		{
			plugin.getGuiManager().openGui(player, new InfoGUI(plugin, action));
		}
	}
	
	private String[] makePlayerAltLore(CustomPlayer altPlayer, String alt)
	{
		List<String> lore = new ArrayList<>();
		String[] loreArray = new String[0];
		lore.add(ChatColor.GRAY + "Grade: " + ChatColor.getByChar(altPlayer.getRankData().getRank().getStyleCode()) +
				altPlayer.getRankData().getRank().getName());
		lore.add(ChatColor.GRAY + "Permission: " +
				ChatColor.getByChar(RankList.getByPermission(altPlayer.getRankData().getPermission()).getStyleCode()) +
				RankList.getByPermission(altPlayer.getRankData().getPermission()).getName());
		lore.add(ChatColor.GRAY + "Connexion(s): " + ChatColor.RED + this.alts.get(alt));
		lore.add("");
		lore.add(ChatColor.GRAY + "Banni: " + (persistanceManager.getSanctionManager().isBanned(altPlayer) ? (ChatColor.GREEN + "Oui") :
				(ChatColor.RED + "Non")));
		lore.add(ChatColor.GRAY + "Réduit au silence: " + (persistanceManager.getSanctionManager().isMuted(altPlayer) ?
				(ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add("");
		lore.add(ChatColor.GRAY + "Inscription: " + ChatColor.YELLOW + (new CustomDate(altPlayer.getFirstLogin())).getCompleteFormat());
		lore.add(ChatColor.GRAY + "Connection: " + ChatColor.YELLOW + (new CustomDate(altPlayer.getLastLogin())).getCompleteFormat());
		lore.add(ChatColor.GRAY + "En ligne: " + (altPlayer.isOnline() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")));
		lore.add("");
		lore.add(ChatColor.LIGHT_PURPLE + "Cliquez pour obtenir les infos");
		return lore.toArray(loreArray);
	}
}
