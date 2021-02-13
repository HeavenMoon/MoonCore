package fr.heavenmoon.core.bukkit.mod.sanctions.templategui;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.gui.AbstractGui;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.builders.items.HeadBuilder;
import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SanctionGUI extends AbstractGui {

    private String targetName;

    public SanctionGUI(MoonBukkitCore plugin, String target) {
        super(plugin);
        this.targetName = target;
    }

    public void display(Player player) {
        this.inventory = plugin.getServer().createInventory(null, 54, "Sanction : " + targetName);

        UUID uuid = BUniqueID.get(targetName);
        CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(uuid);

        setSlotData(customTarget.getName(),
                new HeadBuilder().setOwner(customTarget.getName()).setName(ChatColor.AQUA + customTarget.getName()).build(), 0,
                new String[]{ChatColor.GRAY + "Grade: " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget.getRankData().getRank().getPrefix(),
                        ChatColor.GRAY + "Permission: " + ChatColor.BLUE + customTarget.getRankData().getPermission(),
                        ChatColor.GRAY + "Premium: " + (customTarget.isPremium() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")),
                ChatColor.GRAY + "AzLauncher: " + (customTarget.isAzlauncher() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non"))},
                null);
        ItemStack deco = new ItemBuilder().setMaterial(Material.STAINED_GLASS_PANE).setData(Byte.valueOf((byte) 15)).toItemStack();
        for (int i = 9; i < 18; i++)
            setSlotData(ChatColor.BOLD + "Deco", deco, i, null, null);

        if (persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId()).hasPermission(RankList.MODERATEUR)) {
            setSlotData(ChatColor.GOLD + "Infraction au chat", Material.BOOK_AND_QUILL, 29, new String[]{"", ChatColor.GRAY + "Cliquez pour sanctionner"}, "chat");
            setSlotData(ChatColor.GOLD + "Infraction au jeu (triche)", Material.DIAMOND_AXE, 31, new String[]{"", ChatColor.GRAY + "Cliquez pour sanctionner"}, "cheat");
            setSlotData(ChatColor.GOLD + "Infraction Autre", Material.QUARTZ, 33, new String[]{"", ChatColor.GRAY + "Cliquez pour sanctionner"}, "other");
        } else {
            setSlotData(ChatColor.GOLD + "Infraction au chat", Material.BOOK_AND_QUILL, 31, new String[]{"", ChatColor.GRAY + "Cliquez pour sanctionner"}, "chat");
        }

        setSlotData(ChatColor.RED + "Quitter", Material.WOOD_DOOR, 53, null, "close");

        plugin.getSanctionsManager().getTargets().put(player, targetName);

        plugin.getServer().getScheduler().runTask(plugin, () -> player.openInventory(this.inventory));
    }

    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.equalsIgnoreCase("close")) {
            plugin.getGuiManager().closeGui(player);
        } else if (action.equalsIgnoreCase("chat")) {
            plugin.getGuiManager().openGui(player, new SanctionChatGUI(plugin, targetName));
        } else if (action.equalsIgnoreCase("cheat")) {
            plugin.getGuiManager().openGui(player, new SanctionCheatGUI(plugin, targetName));
        } else if (action.equalsIgnoreCase("other")) {
            plugin.getGuiManager().openGui(player, new SanctionOtherGUI(plugin, targetName));
        }
    }
}