package fr.heavenmoon.core.bukkit.mod;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ModManager {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;    public List<Player> modList;

    public ModManager(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
        this.modList = new ArrayList<>();
    }

    public void enableMod(CustomPlayer customPlayer) {
        if (customPlayer.getModerationData().isEnable()) {
            new Message(PrefixType.ERROR, "Vous êtes déjà en mode modération").send(customPlayer);
            return;
        }

        customPlayer.getModerationData().setEnable(true);
        customPlayer.getModerationData().setTools(true);
        customPlayer.getModerationData().setVanish(true);
        customPlayer.getModerationData().setSpeed(false);
        customPlayer.getModerationData().setFly(true);
        persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
        Player player = Bukkit.getPlayer(customPlayer.getName());
        getItems(player);
        plugin.getVanishManager().enableVanish(player);
        plugin.getSpeedManager().enableSpeed(player);
        player.setAllowFlight(true);
        if (!modList.contains(player)) modList.add(player);
        Bukkit.getServer().getPluginManager().callEvent(new PlayerModEvent(customPlayer, player));
        new Message(PrefixType.MODO, "Vous êtes maintenant en mode modération.").send(customPlayer);
    }

    public void disableMod(CustomPlayer customPlayer) {
        if (!customPlayer.getModerationData().isEnable()) {
            new Message(PrefixType.ERROR, "Vous êtes déjà en mode modération.").send(customPlayer);
            return;
        }

        customPlayer.getModerationData().setEnable(false);
        customPlayer.getModerationData().setTools(false);
        customPlayer.getModerationData().setVanish(false);
        customPlayer.getModerationData().setSpeed(false);
        persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
        Player player = Bukkit.getPlayer(customPlayer.getName());
        player.getInventory().clear();
        plugin.getVanishManager().disableVanish(player);
        plugin.getSpeedManager().disableSpeed(player);
        if(modList.contains(player)) modList.remove(player);
        Bukkit.getServer().getPluginManager().callEvent(new PlayerModEvent(customPlayer, player));
        new Message(PrefixType.MODO, "Vous n'êtes plus en mode modération.").send(customPlayer);
    }

    public void toggleMod(CustomPlayer customPlayer) {
        if (customPlayer.getModerationData().isEnable()) {
            disableMod(customPlayer);
        } else {
            enableMod(customPlayer);
        }
    }

    public void getItems(Player player) {
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        if (!customPlayer.getModerationData().isEnable()) {
            new Message(PrefixType.ERROR, "Vous devez être en mode mod pour effectuer cette action.").send(player);
            return;
        }
        player.getInventory().clear();
        player.getInventory().setItem(0, ModItems.KNOCKBACK.getContent());
        player.getInventory().setItem(1, ModItems.KNOCKBACK2.getContent());
        player.getInventory().setItem(2, ModItems.STRIP.getContent());
        player.getInventory().setItem(3, ModItems.FREEZE.getContent());
        if (customPlayer.getModerationData().isSpeed()) {
            player.getInventory().setItem(5, ModItems.SPEEDON.getContent());
        } else {
            player.getInventory().setItem(5, ModItems.SPEEDOFF.getContent());
        }
        if (customPlayer.getModerationData().isVanish()) {
            player.getInventory().setItem(6, ModItems.VANISHON.getContent());
        } else {
            player.getInventory().setItem(6, ModItems.VANISHOFF.getContent());
        }
        player.getInventory().setItem(7, ModItems.SANCTION.getContent());
        player.getInventory().setItem(8, ModItems.INFO.getContent());
        
    }

    public List<Player> getModList() {
        return modList;
    }
}