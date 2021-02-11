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
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.core.common.utils.CommandBlocker;
import fr.heavenmoon.persistanceapi.customs.server.CustomServer;
import fr.heavenmoon.persistanceapi.customs.server.ServerStatus;
import fr.heavenmoon.persistanceapi.customs.server.ServerType;
import fr.heavenmoon.persistanceapi.customs.server.ServerWhitelist;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.listener.MessageListener;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MoonBukkitCore extends JavaPlugin implements MoonPlatform {

    private static MoonBukkitCore INSTANCE;

    private final MoonCommons commons = new MoonCommons(this);
    
    private RedisTarget redisTarget;
    
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
        this.redisTarget = new RedisTarget(RedisTarget.RedisTargetType.SERVER);
        commons.init(RedisTarget.RedisTargetType.SERVER);
        CustomServer customServer = null;
        if (!this.commons.getPersistanceManager().getServerManager().exist(this.commons.getConfig().getServerName()))
        {
            this.commons.getLogger().info("Serveur not exist, processing...");
            customServer =
                    new CustomServer(this.commons.getConfig().getServerName(), this.commons.getPlatform().getHost(),
                            ServerType.getByName(this.commons.getConfig().getServerType()), ServerStatus.STARTING,
                            ServerWhitelist.getByName(this.commons.getConfig().getServerWhitelist()), 0);
            this.commons.getPersistanceManager().getServerManager().add(customServer);
            this.commons.getLogger().info("Done");
        }
        else
        {
            customServer = this.commons.getPersistanceManager().getServerManager().getCustomServer(this.commons.getConfig().getServerName());
            customServer.setStatus(ServerStatus.STARTING);
            this.commons.getPersistanceManager().getServerManager()
                        .commit( customServer);
            this.commons.getPersistanceManager().getServerManager().update(customServer);
        }
        
        commons.getLogger().info("Enabling Bukkit Core ...");
        commons.getLogger().info("Loading bukkit core managers...");
        
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

//        for (RankList rank : RankList.values()) {
//            teams.add(new ScoreboardTeam("" + rank.getOrder(), ChatColor.getByChar(rank.getStyleCode()) + rank.getPrefix()));
//        }
        this.commons.getLogger().info("Loading bukkit action !");
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable()
        {
            public void run()
            {
                new Action(MoonBukkitCore.get());
                MoonBukkitCore.this.commons.getLogger().info("Action successfully loaded !");
            }
        }, 40L);
        customServer.setStatus(ServerStatus.STARTED);
        this.commons.getPersistanceManager().getServerManager().commit(customServer);
        this.commons.getPersistanceManager().getServerManager().update(customServer);

        CommandBlocker.removeCommands();

        commons.getLogger().info("Enabling Core successful !");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        commons.getLogger().info("Disabling Bukkit Core...");
        CustomServer server =
                this.commons.getPersistanceManager().getServerManager().getCustomServer(this.commons.getConfig().getServerName());
        server.setStatus(ServerStatus.STOPED);
        server.setOnline(0);
        this.commons.getPersistanceManager().getServerManager().update(server);
        this.commons.getPersistanceManager().getServerManager().remove(server);
        Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer(ChatColor.RED + "Le serveur est éteint !"));

        scoreboardManager.onDisable();
    
        for (Player player : Bukkit.getOnlinePlayers())
        {
            CustomPlayer customPlayer = this.commons.getPersistanceManager().getPlayerManager().getCustomPlayer(player.getUniqueId());
            this.commons.getPersistanceManager().getPlayerManager().update(customPlayer);
        }
        
        this.commons.getLogger().info("Disabling Core successful !");
        this.commons.shutdown();

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
    public String getPlatformName() { return commons.getConfig().getServerName(); }

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
    
    public String getHost()
    {
        try
        {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            return null;
        }
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
