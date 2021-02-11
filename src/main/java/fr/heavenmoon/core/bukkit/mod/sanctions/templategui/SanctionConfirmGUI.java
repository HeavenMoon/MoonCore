package fr.heavenmoon.core.bukkit.mod.sanctions.templategui;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.gui.AbstractGui;
import fr.heavenmoon.core.bukkit.mod.sanctions.SanctionList;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.builders.items.HeadBuilder;
import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import fr.heavenmoon.persistanceapi.customs.player.SanctionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SanctionConfirmGUI extends AbstractGui
{
	
	private String targetName;
	private SanctionList sanctionList;
	
	public SanctionConfirmGUI(MoonBukkitCore plugin, String target, SanctionList sanctionList)
	{
		super(plugin);
		this.targetName = target;
		this.sanctionList = sanctionList;
	}
	
	public void display(Player player)
	{
		double multiplier;
		
		this.inventory = plugin.getServer().createInventory(null, 9 * 5, "Confirmation");
		UUID uuid = BUniqueID.get(targetName);
		CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
		
		setSlotData(customTarget.getName(),
				new HeadBuilder().setOwner(customTarget.getName()).setName(ChatColor.AQUA + customTarget.getName()).build(), 0,
				new String[]{ChatColor.GRAY + "Grade: " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget.getRankData().getRank().getPrefix(),
						ChatColor.GRAY + "Permission: " + ChatColor.BLUE + customTarget.getRankData().getPermission(),
						ChatColor.GRAY + "Premium: " + (customTarget.isPremium() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")),
						ChatColor.GRAY + "AzLauncher: " +
								(customTarget.isAzlauncher() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non"))}, null);
		
		ItemStack deco = new ItemBuilder().setMaterial(Material.STAINED_GLASS_PANE).setData((byte) 15).toItemStack();
		for (int i = 9; i < 18; i++)
			setSlotData(ChatColor.BOLD + "Deco", deco, i, null, null);
		
		if (sanctionList.getSanction().equals(SanctionType.BAN))
		{
			int history = persistanceManager.getSanctionManager().getAllBans(customTarget).size();
			multiplier = Math.exp(history);
		}
		else
		{
			int history = persistanceManager.getSanctionManager().getAllMutes(customTarget).size();
			multiplier = Math.exp(history);
		}
		setSlotData(ChatColor.GOLD + sanctionList.getName(), sanctionList.getMaterial(), 31,
				new String[]{ChatColor.GRAY + "Joueur: " + customTarget
						.getRankData().getRank().getPrefix() + customTarget.getName(),
						ChatColor.GRAY + "Durée: " + ChatColor.GREEN + sanctionList
								.getPublicTime(multiplier), ChatColor.GRAY + "Sanction: " + ChatColor.RED + sanctionList
						.getSanction().getDisplayName()}, null);
		ItemStack validate = new ItemBuilder().setMaterial(Material.INK_SACK).setData(Byte.valueOf((byte) 10)).toItemStack();
		setSlotData(ChatColor.GREEN + "Sanctionner", validate, 29, null, "sanction");
		ItemStack cancel = new ItemBuilder().setMaterial(Material.INK_SACK).setData(Byte.valueOf((byte) 8)).toItemStack();
		setSlotData(ChatColor.RED + "Annuler", cancel, 33, null, "close");
		plugin.getServer().getScheduler().runTask(plugin, () -> player.openInventory(this.inventory));
	}
	
	public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
	{
		if (action.equalsIgnoreCase("close"))
		{
			plugin.getGuiManager().closeGui(player);
		}
		else if (action.equalsIgnoreCase("comeback"))
		{
			plugin.getGuiManager().openGui(player, new SanctionGUI(plugin, targetName));
		}
		else if (action.contains("sanction"))
		{
			plugin.getSanctionsManager().applySanction(player, targetName, sanctionList);
		}
	}
}