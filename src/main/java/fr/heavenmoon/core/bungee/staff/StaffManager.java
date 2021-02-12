package fr.heavenmoon.core.bungee.staff;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class StaffManager {

    private final MoonBungeeCore plugin;
    private final PersistanceManager persistanceManager;

    public StaffManager(MoonBungeeCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void getStaffList(CommandSender sender) {
        new Message(ChatColor.GREEN + "" + ChatColor.BOLD + "[STAFF] " + ChatColor.GRAY + "Liste du staff en ligne:").send(sender);

        List<ProxiedPlayer> staffs = plugin.getProxy()
                                           .getPlayers()
                                           .stream()
                                           .filter(p -> persistanceManager.getPlayerManager().getCustomPlayer(p.getUniqueId()).getRankData().getPermission() >= 60).collect(Collectors.toList());

        staffs.forEach(staffPlayer -> {
            CustomPlayer customStaff = persistanceManager.getPlayerManager().getCustomPlayer(staffPlayer.getUniqueId());
            new Message(ChatColor.getByChar(customStaff.getRankData().getStyleCode()) + customStaff.getRankData().getPrefix()
                    + staffPlayer.getName() + ChatColor.GRAY + " - " + customStaff.getServerName()).send(sender);
        });
    }

    public void sendStaffChatMessage(CustomPlayer customPlayer, String message) {
        plugin.getProxy().getPlayers().forEach(p -> {
            if (persistanceManager.getPlayerManager().getCustomPlayer(p.getUniqueId()).getRankData().getPermission() >= 60) {
                p.sendMessage(TextComponent.fromLegacyText(ChatColor.GREEN + "" + ChatColor.BOLD + "[STAFF] "
                        + ChatColor.getByChar(customPlayer.getRankData().getStyleCode()) + customPlayer.getRankData().getPrefix()
                        + customPlayer.getName() + ChatColor.GRAY + ": " + ChatColor.GREEN + message));
            }
        });
    }


}
