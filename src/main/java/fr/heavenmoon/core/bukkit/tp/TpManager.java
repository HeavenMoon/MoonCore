package fr.heavenmoon.core.bukkit.tp;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.ActionbarBuilder;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.core.common.utils.wrappers.LambdaWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TpManager {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    private Map<CustomPlayer, Integer> tpCooldowns;

    public TpManager(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
        this.tpCooldowns = new HashMap<>();
    }

    public void startCooldownTp(CustomPlayer customPlayer, Location loc, int seconds) {
        if (!customPlayer.canTp()) {
            new Message(PrefixType.ERROR, "Vous ne pouvez pas vous tmaintenant !");
            return;
        }
        LambdaWrapper<Integer> cooldownTime = new LambdaWrapper(seconds);
        customPlayer.setCanTp(false);
        int scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Player player = Bukkit.getPlayer(customPlayer.getName());
            if (player != null) {
                new ActionbarBuilder( ChatColor.GRAY + "Téléportation dans " + ChatColor.LIGHT_PURPLE + cooldownTime.getData() + ChatColor.GRAY + " seconde(s)...", 2).send(player);
            }
            cooldownTime.setData(cooldownTime.getData() - 1);
            if (cooldownTime.getData() == 0) {
                customPlayer.setCanTp(true);
                Bukkit.getScheduler().cancelTask(tpCooldowns.get(customPlayer));
                new RedisPublisher(persistanceManager, "Connect").setArguments(customPlayer.getName(), "lobby01").publish(new RedisTarget(RedisTarget.RedisTargetType.PROXY));
                return;
            }
            if (player != null) {
                if (player.getLocation().getX() != loc.getX() || player.getPlayer().getLocation().getY() != loc.getY() || player.getLocation().getZ() != loc.getZ()) {
                    new Message(PrefixType.ERROR, "Vous vous êtes déplacé ce qui a annulé votre téléportation.").send(player);
                    customPlayer.setCanTp(true);
                    Bukkit.getScheduler().cancelTask(tpCooldowns.get(customPlayer));
                }
            }

        }, 0L, 20L);
        tpCooldowns.put(customPlayer, scheduler);
    }

    public Map<CustomPlayer, Integer> getTpCooldowns() {
        return tpCooldowns;
    }
}