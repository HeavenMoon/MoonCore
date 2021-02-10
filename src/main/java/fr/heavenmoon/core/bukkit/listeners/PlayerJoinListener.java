package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.builders.misc.TablistBuilder;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;    private RankList rank = RankList.BUILDER;

    public PlayerJoinListener(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER, player.getUniqueId());
        plugin.getVanishManager().refreshVanish(player);
        plugin.getSpeedManager().refreshSpeed(player);

        if (customPlayer.getModerationData().isEnable()) {
            customPlayer.getModerationData().setFly(true);
            
            if (!plugin.getModManager().getModList().contains(player)) plugin.getModManager().getModList().add(player);
            if (customPlayer.getModerationData().isTools())
                plugin.getModManager().getItems(player);

            event.setJoinMessage(null);

            new Message(PrefixType.MODO, "Vous rejoignez le serveur en mode modération.").send(player);
        }

        initCustomPlayer(customPlayer);

        new TablistBuilder(player).setTop(ChatColor.BLUE + "Vous êtes connecté sur " + ChatColor.LIGHT_PURPLE + "play.heavenmoon.fr")
                .setBottom(ChatColor.BLUE + "Grades, Kits, Clés disponibles sur " + ChatColor.WHITE + "" + ChatColor.BOLD + "shop.heavenmoon.fr").changeTablistPlayer();
    }

    private void initCustomPlayer(CustomPlayer customPlayer) {
        Player player = Bukkit.getPlayer(customPlayer.getName());
        plugin.getSpeedManager().resetSpeed(player);
        player.setAllowFlight(customPlayer.getModerationData().isFly());

        customPlayer.setServerName(plugin.getCommons().getServerName());
        customPlayer.setOnline(true);

        if (customPlayer.hasPermission(RankList.MODERATEUR) && customPlayer.getModerationData().isEnable())
            if (customPlayer.getModerationData().isVanish()) plugin.getVanishManager().enableVanish(player);
            else plugin.getVanishManager().disableVanish(player);

        player.setGameMode(GameMode.getByValue(customPlayer.getGamemode()));
       persistanceManager.getPlayerManager().commit(RedisKey.PLAYER, customPlayer);
    }
}