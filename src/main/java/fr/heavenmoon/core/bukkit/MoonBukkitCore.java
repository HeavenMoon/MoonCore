package fr.heavenmoon.core.bukkit;

import fr.heavenmoon.core.bukkit.alts.AltsManager;
import fr.heavenmoon.core.bukkit.commands.teleport.TeleportManager;
import fr.heavenmoon.core.bukkit.dm.DmManager;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.gui.GuiManager;
import fr.heavenmoon.core.bukkit.guild.GuildManager;
import fr.heavenmoon.core.bukkit.mod.ModManager;
import fr.heavenmoon.core.bukkit.mod.bypass.BypassManager;
import fr.heavenmoon.core.bukkit.mod.freeze.FreezeManager;
import fr.heavenmoon.core.bukkit.mod.sanctions.SanctionsManager;
import fr.heavenmoon.core.bukkit.mod.sanctions.ban.BanManager;
import fr.heavenmoon.core.bukkit.mod.sanctions.kick.KickManager;
import fr.heavenmoon.core.bukkit.mod.sanctions.mute.MuteManager;
import fr.heavenmoon.core.bukkit.mod.speed.SpeedManager;
import fr.heavenmoon.core.bukkit.mod.vanish.VanishManager;
import fr.heavenmoon.core.bukkit.rank.RankManager;
import fr.heavenmoon.core.bukkit.redis.RedisMessageEvent;
import fr.heavenmoon.core.bukkit.scoreboard.ScoreboardManager;
import fr.heavenmoon.core.bukkit.scoreboard.ScoreboardTeam;
import fr.heavenmoon.core.bukkit.tp.TpManager;
import fr.heavenmoon.core.common.MoonCommons;
import fr.heavenmoon.core.common.MoonPlatform;
import fr.heavenmoon.core.common.PlatformType;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.logger.LoggerAdapter;
import fr.heavenmoon.core.common.logger.MoonLogger;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.core.common.utils.CommandBlocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.listener.MessageListener;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MoonBukkitCore extends JavaPlugin implements MoonPlatform {

    private static MoonBukkitCore INSTANCE;

    private final MoonCommons commons = new MoonCommons(this);
    private MoonLogger moonLogger;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    private ScoreboardManager scoreboardManager;
    private GuiManager guiManager;
    private DmManager dmManager;
    private AltsManager altsManager;
    private ModManager modManager;
    private VanishManager vanishManager;
    private SpeedManager speedManager;
    private RankManager rankManager;
    private BypassManager bypassManager;
    private TeleportManager teleportManager;
    private SanctionsManager sanctionsManager;
    private BanManager banManager;
    private KickManager kickManager;
    private MuteManager muteManager;
    private TpManager tpManager;
    private FreezeManager freezeManager;
    private GuildManager guildManager;

    private List<ScoreboardTeam> teams = new ArrayList<>();

    @Override
    public void onLoad() {
        moonLogger = new MoonLogger(getLogger());
        super.onLoad();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        commons.init(RedisTarget.RedisTargetType.SERVER);

        commons.getLogger().info("Enabling Bukkit Core ...");

        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);

        commons.getLogger().info("Loading bukkit core managers...");
        scoreboardManager = new ScoreboardManager(this);
        guiManager = new GuiManager(this);
        dmManager = new DmManager(this);
        altsManager = new AltsManager(this);
        modManager = new ModManager(this);
        vanishManager = new VanishManager(this);
        speedManager = new SpeedManager(this);
        rankManager = new RankManager(this);
        bypassManager = new BypassManager(this);
        teleportManager = new TeleportManager(this);
        sanctionsManager = new SanctionsManager(this);
        banManager = new BanManager(this);
        kickManager = new KickManager(this);
        muteManager = new MuteManager(this);
        tpManager = new TpManager(this);
        freezeManager = new FreezeManager(this);
        guildManager = new GuildManager(this);
        commons.getLogger().info("Bukkit core managers successfully loaded !");

        for (RankList rank : RankList.values()) {
            teams.add(new ScoreboardTeam("" + rank.getOrder(), ChatColor.getByChar(rank.getStyleCode()) + rank.getPrefix()));
        }

        commons.getLogger().info("Loading bukkit action !");
        new Action(this);
        commons.getLogger().info("Action successfully loaded !");

        CommandBlocker.removeCommands();

        commons.getLogger().info("Enabling Core successful !");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        commons.getLogger().info("Disabling Bukkit Core...");

        Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer(ChatColor.RED + "Le serveur est éteint !"));

        scoreboardManager.onDisable();

        commons.getLogger().info("Disabling Core successful !");
        commons.shutdown();

        super.onDisable();
    }

    public static MoonBukkitCore get() {
        return INSTANCE;
    }

    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public AltsManager getAltsManager() {
        return altsManager;
    }

    public ModManager getModManager() {
        return modManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public SpeedManager getSpeedManager() {
        return speedManager;
    }

    public DmManager getDmManager() {
        return dmManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public BypassManager getBypassManager() {
        return bypassManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public SanctionsManager getSanctionsManager() {
        return sanctionsManager;
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public KickManager getKickManager() {
        return kickManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public TpManager getTpManager() {
        return tpManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public List<ScoreboardTeam> getTeams() {
        return teams;
    }

    public ScoreboardTeam getSbTeam(String name) {
        return teams.stream().filter(t -> t.getName().equals(name)).findFirst().get();
    }

    @Override
    public MoonCommons getCommons() {
        return commons;
    }

    @Override
    public LoggerAdapter getMoonLogger() {
        return moonLogger;
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.BUKKIT;
    }

    @Override
    public String getPlatformName() { return commons.getServerName(); }

    @Override
    public String getPlatformVersion() {
        return Bukkit.getVersion();
    }

    @Override
    public String getPluginVersion() {
        return getDescription().getVersion();
    }

    @Override
    public Path getDataDirectory() {
        return getDataFolder().toPath();
    }

    @Override
    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    @Override
    public MessageListener<String> getMessageEvent() {
        return new RedisMessageEvent(this);
    }

    @Override
    public void executeAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
    }

    @Override
    public void sendMessage(String name, String message) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public void shutdown() {
        new Message(PrefixType.SERVER, "Le serveur va " + ChatColor.RED + "s'éteindre" + ChatColor.GRAY + ".").broadcast();
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(MessageType.SERVER_DOWN.getContent()));
        Bukkit.shutdown();
    }

    @Override
    public void reboot() {
        new Message(ChatColor.GRAY + "Le serveur va " + ChatColor.RED + "redémarrer" + ChatColor.GRAY + ".").broadcast();
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(MessageType.SERVER_RESTART.getContent()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }
}
