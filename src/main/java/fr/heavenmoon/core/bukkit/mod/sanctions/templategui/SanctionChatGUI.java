package fr.heavenmoon.core.bukkit.mod.sanctions.templategui;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.gui.AbstractGui;
import fr.heavenmoon.core.bukkit.mod.sanctions.SanctionList;
import fr.heavenmoon.core.bukkit.mod.sanctions.SanctionStatus;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.builders.items.HeadBuilder;
import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import fr.heavenmoon.core.common.utils.wrappers.LambdaWrapper;
import fr.heavenmoon.persistanceapi.customs.player.SanctionType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SanctionChatGUI extends AbstractGui {

    private String targetName;

    public SanctionChatGUI(MoonBukkitCore plugin, String target) {
        super(plugin);
        this.targetName = target;
    }

    public void display(Player player) {
        this.inventory = plugin.getServer().createInventory(null, 54, "Infraction au chat");
        UUID uuid = BUniqueID.get(targetName);
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, uuid);

        LambdaWrapper<Integer> slot = new LambdaWrapper(18);
        setSlotData(customPlayer.getName(),
                new HeadBuilder().setOwner(customPlayer.getName()).setName(ChatColor.AQUA + customPlayer.getName()).build(), 0,
                new String[]{ChatColor.GRAY + "Grade: " + ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getRank().getPrefix(),
                        ChatColor.GRAY + "Permission: " + ChatColor.BLUE + customPlayer.getRankData().getPermission(),
                        ChatColor.GRAY + "Premium: " + (customPlayer.isPremium() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non")),
                ChatColor.GRAY + "AzLauncher: " + (customPlayer.isAzlauncher() ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non"))}, null);
        setSlotData(ChatColor.RED + "Infraction au chat", Material.BOOK_AND_QUILL, 1, null, "");

        ItemStack deco = new ItemBuilder().setMaterial(Material.STAINED_GLASS_PANE).setData((byte) 15).toItemStack();
        for (int i = 9; i < 18; i++)
            setSlotData(ChatColor.BOLD + "Deco", deco, i, null, null);

        SanctionList.get(SanctionStatus.CHAT).forEach(sanction -> {
            double multiplier;
            if (sanction.getSanction().equals(SanctionType.BAN)) {
                int history = persistanceManager.getSanctionManager().getAllBans(customPlayer).size();
                multiplier = Math.exp(history);
            } else {
                int history = persistanceManager.getSanctionManager().getAllMutes(customPlayer).size();
                multiplier = Math.exp(history);
            }
            setSlotData(ChatColor.GOLD + sanction.getName(), sanction.getMaterial(), slot.getData().intValue(),
                    new String[]{ChatColor.GRAY + "Joueur: " + ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getRank().getPrefix() + customPlayer.getName(),
                            ChatColor.GRAY + "Durée: " + ChatColor.GREEN + sanction.getPublicTime(multiplier), ChatColor.GRAY + "Sanction" +
                            ": " + ChatColor.RED + sanction.getSanction().getDisplayName(), "", ChatColor.GRAY + "Cliquez pour sanctionner"}, "check");
            slot.setData(slot.getData() + 1);
        });
        setSlotData(ChatColor.RED + "Annuler", Material.WOOD_DOOR, 53, null, "comeback");
        plugin.getServer().getScheduler().runTask(plugin, () -> player.openInventory(this.inventory));
    }

    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.equalsIgnoreCase("close")) {
            plugin.getGuiManager().closeGui(player);
        } else if (action.equalsIgnoreCase("comeback")) {
            plugin.getGuiManager().openGui(player, new SanctionGUI(plugin, targetName));
        } else if (action.equalsIgnoreCase("check")) {
            SanctionList sanctionList = SanctionList.get(stack.getItemMeta().getDisplayName().substring(2));
            plugin.getGuiManager().openGui(player, new SanctionConfirmGUI(plugin, targetName, sanctionList));
        }
    }
}