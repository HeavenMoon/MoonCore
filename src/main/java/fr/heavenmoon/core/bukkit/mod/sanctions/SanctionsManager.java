package fr.heavenmoon.core.bukkit.mod.sanctions;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SanctionsManager {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public HashMap<Player, String> targets = new HashMap<>();

    public SanctionsManager(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void applySanction(Player player, String target, SanctionList sanctionList) {
        int history;
        double multiplier;
        
        UUID uuid = BUniqueID.get(target);

        CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, uuid);

        switch (sanctionList.getSanction()) {
            case BAN:
                history = persistanceManager.getSanctionManager().getAllBans(customTarget).size();
                multiplier = Math.exp(history);
                plugin.getBanManager().banAdd(player, target, sanctionList.getReason(), sanctionList.getTime(multiplier));
                plugin.getGuiManager().closeGui(player);
                break;
            case KICK:
                plugin.getKickManager().kickAdd(player, target, sanctionList.getReason());
                plugin.getGuiManager().closeGui(player);
                break;
            case MUTE:
                history = persistanceManager.getSanctionManager().getAllMutes(customTarget).size();
                multiplier = Math.exp(history);
                plugin.getMuteManager().muteAdd(player, target, sanctionList.getReason(), sanctionList.getTime(multiplier));
                plugin.getGuiManager().closeGui(player);
                break;
        }
    }

    public String getBanReason(String reason, String author, long apply, long until) {
        return ChatColor.RED + "Vous avez été banni.\n\n" + ChatColor.GRAY + "Banni par: " + ChatColor.AQUA + author + "\n" + ChatColor.GRAY + "Pour: " + ChatColor.AQUA + reason + "\n" + ChatColor.GRAY + "Le: " + ChatColor.YELLOW + new CustomDate(apply)
                .getCompleteFormat() + "\n" + ChatColor.GRAY + "Jusqu'au: " + ChatColor.YELLOW + (new CustomDate(until)).getCompleteFormat();
    }

    public String getKickReason(String reason, String author) {
        return ChatColor.RED + "Vous avez été du serveur.\n\n" + ChatColor.GRAY + "Par: " + ChatColor.AQUA + author + "\n" + ChatColor.GRAY + "Raison: " + ChatColor.AQUA + reason;
    }
    
    public HashMap<Player, String> getTargets()
    {
        return targets;
    }
}