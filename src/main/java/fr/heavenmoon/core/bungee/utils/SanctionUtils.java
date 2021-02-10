package fr.heavenmoon.core.bungee.utils;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.persistanceapi.customs.player.CustomSanction;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

public class SanctionUtils {

    private final MoonBungeeCore plugin;
    private final PersistanceManager persistanceManager;

    public SanctionUtils(MoonBungeeCore plugin)
    {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void banRemove(String name, UUID uuid) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, uuid);
        if (!persistanceManager.getSanctionManager().isBanned(customPlayer)) return;

        customPlayer.getModerationData().setCurrentSanctionId("0");
    
        persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
    }

    public String getBanReason(String author, String reason, long apply, long until) {
        return ChatColor.RED + "Vous êtes banni de ce serveur." +
                "\n\n" + ChatColor.GRAY + "Banni par: " + ChatColor.AQUA + author +
                "\n" + ChatColor.GRAY + "Pour: " + ChatColor.AQUA + reason +
                "\n" + ChatColor.GRAY + "Le: " + ChatColor.YELLOW + new CustomDate(apply).getCompleteFormat() +
                "\n" + ChatColor.GRAY + "Jusqu'au: " + ChatColor.YELLOW + new CustomDate(until).getCompleteFormat();
    }

    public String getKickReason(String reason) {
        return ChatColor.RED + "Vous avez été éjecté du serveur." +
                "\n\n" + ChatColor.GRAY + "Raison: " + ChatColor.AQUA + reason;
    }

    public String getKickReason(String author, String reason) {
        return ChatColor.RED + "Vous avez été éjecté du serveur." +
                "\n\n" + ChatColor.GRAY + "Par: " + ChatColor.AQUA + author +
                "\n\n" + ChatColor.GRAY + "Raison: " + ChatColor.AQUA + reason;
    }

}
