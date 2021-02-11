package fr.heavenmoon.core.bukkit;

import fr.heavenmoon.core.bukkit.commands.*;
import fr.heavenmoon.core.bukkit.commands.gamemode.*;
import fr.heavenmoon.core.bukkit.commands.mod.*;
import fr.heavenmoon.core.bukkit.commands.sanctions.BanCommand;
import fr.heavenmoon.core.bukkit.commands.sanctions.KickCommand;
import fr.heavenmoon.core.bukkit.commands.sanctions.MuteCommand;
import fr.heavenmoon.core.bukkit.commands.sanctions.SanctionCommand;
import fr.heavenmoon.core.bukkit.commands.teleport.GlobalteleportCommand;
import fr.heavenmoon.core.bukkit.commands.teleport.TeleportCommand;
import fr.heavenmoon.core.bukkit.commands.teleport.TeleporthereCommand;
import fr.heavenmoon.core.bukkit.commands.time.*;
import fr.heavenmoon.core.bukkit.listeners.*;
import fr.heavenmoon.core.bukkit.scoreboard.SbCommand;
import fr.heavenmoon.core.common.utils.DCommand;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Action {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;

    public Action(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
        
        registerEvents();
        loadCommands();
    }

    private void register(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void registerEvents() {
        register(new ModListener(plugin));
        register(new CacheListener(plugin));
        register(new FreezeListener(plugin));

        register(new PlayerDeathListener(plugin));
        register(new PlayerJoinListener(plugin));
        register(new PlayerQuitListener(plugin));
        register(new AsyncPlayerChatListener(plugin));
    }

    public void loadCommands() {
        new DCommand("gemmes", "/gemmes", "gemmes command", null, Collections.singletonList(""), new GemmesComand(plugin), plugin);
        new DCommand("stars", "/stars", "stars command", null, Collections.singletonList(""), new StarsCommand(plugin), plugin);
        new DCommand("guild", "/guild", "show/set guild", null, Collections.singletonList("g"), new GuildCommand(plugin), plugin);
        new DCommand("day", "/day", "set time to day", null, Collections.singletonList(""), new DayCommand(plugin), plugin);
        new DCommand("night", "/night", "set time to night", null, Collections.singletonList(""), new NightCommand(plugin), plugin);
        new DCommand("sun", "/sun", "set time to sun", null, Collections.singletonList(""), new SunCommand(plugin), plugin);
        new DCommand("rain", "/rain", "set time to rain", null, Collections.singletonList(""), new RainCommand(plugin), plugin);
        new DCommand("thunder", "/thunder", "set time to thunder", null, Collections.singletonList(""), new ThunderCommand(plugin), plugin);
        new DCommand("freeze", "/freeze <player>", "freeze a player", null, Collections.singletonList(""), new FreezeCommand(plugin), plugin);
        new DCommand("seen", "/seen <player>", "see last connection of a player", null, Collections.singletonList(""), new SeenCommand(plugin), plugin);
        new DCommand("fly", "/fly <player>", "fly a player", null, Collections.singletonList(""), new FlyCommand(plugin), plugin);
        new DCommand("gamemode", "/gamemode <player>", "gamemode survival a player", null, Collections.singletonList("gm"), new GamemodeCommand(plugin), plugin);
        new DCommand("gms", "/gms <player>", "gm survival a player", null, Collections.singletonList(""), new GmsCommand(plugin), plugin);
        new DCommand("gmc", "/gmc <player>", "gm creative a player", null, Collections.singletonList(""), new GmcCommand(plugin), plugin);
        new DCommand("gma", "/gma <player>", "gm adventure a player", null, Collections.singletonList(""), new GmaCommand(plugin), plugin);
        new DCommand("gmsp", "/gmsp <player>", "gm spectator a player", null, Collections.singletonList(""), new GmspCommand(plugin), plugin);
        new DCommand("strip", "/strip <player>", "strip a player", null, Collections.singletonList(""), new StripCommand(plugin), plugin);
        new DCommand("clear", "/clear <player>", "clear a player", null, Collections.singletonList(""), new ClearCommand(plugin), plugin);
        new DCommand("heal", "/heal <player>", "heal a player", null, Collections.singletonList(""), new HealCommand(plugin), plugin);
        new DCommand("kill", "/kill <player>", "kill a player", null, Collections.singletonList(""), new KillCommand(plugin), plugin);
        new DCommand("scoreboard", "/sb", "Toggle scoreboard", null, Collections.singletonList("sb"), new SbCommand(plugin), plugin);
        new DCommand("teleporthere", "/teleporthere", "Teleport player at your location", null, new ArrayList<>(Arrays.asList("tphere", "tph", "s")), new TeleporthereCommand(plugin), plugin);
        new DCommand("teleport", "/teleport", "Teleport to player", null, Collections.singletonList("tp"), new TeleportCommand(plugin), plugin);
        new DCommand("gtp", "/globalteleport", "Teleport to player (without warning server)", null, Collections.singletonList("ptp"), new GlobalteleportCommand(plugin), plugin);
        new DCommand("dev", "/dev", "dev command to display thing we want", null, Collections.singletonList(""), new DevCommand(plugin), plugin);

        new DCommand("info", "/info", "Info player", null, Collections.singletonList("whois"), new InfoCommand(plugin), plugin);
        new DCommand("bypass", "/bypass", "Bypass player", null, Collections.singletonList(""), new BypassCommand(plugin), plugin);
        new DCommand("rank", "/rank", "Rank player", null, Collections.singletonList(""), new RankCommand(plugin), plugin);
        new DCommand("reboot", "/reboot", "Reboot server", null, Collections.singletonList(""), new RebootCommand(plugin), plugin);
        new DCommand("stop", "/stop", "Stop server", null, Collections.singletonList(""), new StopCommand(plugin), plugin);
        new DCommand("speed", "/speed", "Speed command", null, Collections.singletonList(""), new SpeedCommand(plugin), plugin);
        new DCommand("vanish", "/vanish", "Vanish command", null, Collections.singletonList(""), new VanishCommand(plugin), plugin);
        new DCommand("vanishshow", "/vanishshow", "Vanishshow command", null, Collections.singletonList(""), new VsCommand(plugin), plugin);
        new DCommand("modo", "/modo", "Mod command", null, Collections.singletonList(""), new ModoCommand(plugin), plugin);
        new DCommand("sanction", "/sanction", "Sanction command", null, new ArrayList<>(Arrays.asList("sanction", "sa")), new SanctionCommand(plugin), plugin);
        new DCommand("ban", "/ban", "Ban command", null, Collections.singletonList(""), new BanCommand(plugin), plugin);
        new DCommand("kick", "/kick", "Kick command", null, Collections.singletonList(""), new KickCommand(plugin), plugin);
        new DCommand("mute", "/mute", "Mute command", null, Collections.singletonList(""), new MuteCommand(plugin), plugin);
        new DCommand("alts", "/alts", "Alts command", null, Collections.singletonList(""), new AltsCommand(plugin), plugin);
        new DCommand("lobby", "/lobby", "Return to lobby", null, Collections.singletonList("hub"), new LobbyCommand(plugin), plugin);
        new DCommand("modlogs", "/modlogs", "See current sanction of player", null, Collections.singletonList(""), new ModlogsCommand(plugin), plugin);
    }

}
