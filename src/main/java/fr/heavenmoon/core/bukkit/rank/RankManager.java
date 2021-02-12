package fr.heavenmoon.core.bukkit.rank;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.format.style.StyleList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class RankManager {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public RankManager(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void getList(CommandSender sender) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.GRAY + "Liste des grades: ");
        Arrays.asList(RankList.values()).forEach(rank -> stringBuilder.append(
                net.md_5.bungee.api.ChatColor.getByChar(rank.getStyleCode()) + rank.getName() + ChatColor.GRAY + ", "));

        String message = stringBuilder.toString();
        new Message(message.substring(0, message.length() - 2)).send(sender);
    }

    public void setRank(CommandSender sender, String name, String rank, String permission) {
        UUID uuid = BUniqueID.get(name);
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
        RankList newRank = RankList.getByName(rank);
        RankList newRankPermission = RankList.getByName(permission);
        if (newRank == null || newRankPermission == null) {
            new Message(PrefixType.ERROR, "Ce grade n'existe pas.").send(sender);
            return;
        }
        if (sender instanceof Player) {
            CustomPlayer customSender = persistanceManager.getPlayerManager().getCustomPlayer(((Player) sender).getUniqueId());
            if (!customSender.hasPermission(newRank) || !customSender.hasPermission(newRankPermission)) {
                new Message(PrefixType.ERROR, "Vous n'avez pas la permission de donner ce grade.").send(sender);
                return;
            }
            if (!customSender.hasPermission(customPlayer.getRankData().getRank())) {
                new Message(PrefixType.ERROR, "Vous n'avez pas la permission de changer le grade de ce joueur.").send(sender);
                return;
            }
        }

        if (customPlayer.getRankData().getRank().equals(newRank) && customPlayer.getRankData().getRank().equals(newRankPermission)) {
            new Message(PrefixType.ERROR, "Le joueur possède déjà ce grade.").send(sender);
            return;
        }
        setRank(name, uuid, newRank, newRankPermission);
        new Message(PrefixType.SERVER, "Le joueur " + ChatColor.LIGHT_PURPLE + customPlayer.getName() + ChatColor.GRAY + " possède " +
                "maintenant le grade " + ChatColor.getByChar(newRank.getStyleCode()) + newRank.getName() + ChatColor.GRAY + ", avec les permissions du grade " + ChatColor.getByChar(newRankPermission.getStyleCode()) + newRankPermission.getName() + ChatColor.GRAY +
                ".").send(sender);
    }

    public void setRank(String name, String rank, String permission) {
        UUID uuid = BUniqueID.get(name);
        RankList newRank = RankList.getByName(rank);
        RankList newRankPermission = RankList.getByName(permission);

        setRank(name, uuid, newRank, newRankPermission);
    }

    public void setRank(CustomPlayer customPlayer, String rank, String permission) {
        RankList newRank = RankList.getByName(rank);
        RankList newRankPermission = RankList.getByName(permission);

        setRank(customPlayer, newRank, newRankPermission);
    }

    public void setRank(CustomPlayer customPlayer, RankList rank, RankList permission) {
        customPlayer.getRankData().setRank(rank);
        customPlayer.getRankData().setPermission(permission.getPermission());
        customPlayer.getRankData().setStyleCode(rank.getStyleCode());
        customPlayer.getRankData().setPrefix(rank.getPrefix());
        customPlayer.getRankData().setSuffix(rank.getSuffix());
        customPlayer.getRankData().setChatStyleCode(rank.getChatStyleCode());
        customPlayer.getRankData().setOrder(permission.getOrder());
        if (customPlayer.isOnline())
            persistanceManager.getPlayerManager().commit(customPlayer);
        else persistanceManager.getPlayerManager().update(customPlayer);
        Player player = Bukkit.getPlayer(customPlayer.getName());
        if (customPlayer.isOnline() && player != null)
            new Message(PrefixType.SERVER,
                    "Vous possédez maintenant le grade " + ChatColor.getByChar(rank.getStyleCode()) + rank.getName() + ChatColor.GRAY + ".").send(customPlayer);
    }

    public void setRank(String name, UUID uuid, RankList rank, RankList permission) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(uuid);
        customPlayer.getRankData().setRank(rank);
        customPlayer.getRankData().setPermission(permission.getPermission());
        customPlayer.getRankData().setStyleCode(rank.getStyleCode());
        customPlayer.getRankData().setPrefix(rank.getPrefix());
        customPlayer.getRankData().setSuffix(rank.getSuffix());
        customPlayer.getRankData().setChatStyleCode(rank.getChatStyleCode());
        customPlayer.getRankData().setOrder(permission.getOrder());
        if (customPlayer.isOnline())
            persistanceManager.getPlayerManager().commit(customPlayer);
        else persistanceManager.getPlayerManager().update(customPlayer);
        Player player = Bukkit.getPlayer(name);
        if (customPlayer.isOnline() && player != null)
            new Message(PrefixType.SERVER,
                    "Vous possédez maintenant le grade " + ChatColor.getByChar(rank.getStyleCode()) + rank.getName() + ChatColor.GRAY + ".").send(customPlayer);
    }
    

    public boolean isOrder(String order) {
        for (RankList rank : RankList.values()) {
            if (rank.getOrder() == order)
                return true;
        }
        return false;
    }
}