package fr.heavenmoon.core.bungee;

import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.bungee.redis.RedisMessageEvent;
import fr.heavenmoon.core.bungee.staff.StaffManager;
import fr.heavenmoon.core.bungee.utils.SanctionUtils;
import fr.heavenmoon.core.common.MoonCommons;
import fr.heavenmoon.core.common.MoonPlatform;
import fr.heavenmoon.core.common.PlatformType;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.logger.LoggerAdapter;
import fr.heavenmoon.core.common.logger.MoonLogger;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.persistanceapi.customs.server.CustomServer;
import fr.heavenmoon.persistanceapi.customs.server.ServerStatus;
import fr.heavenmoon.persistanceapi.customs.server.ServerType;
import fr.heavenmoon.persistanceapi.customs.server.ServerWhitelist;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class MoonBungeeCore extends Plugin implements MoonPlatform {

    private static MoonBungeeCore INSTANCE;

    private MoonCommons commons = new MoonCommons(this);
    
    private RedisTarget redisTarget;
    
    private MoonLogger moonLogger;

    private StaffManager staffManager;
    private SanctionUtils sanctionUtils;
    
    private RedisMessageEvent redisMessageEvent;

    @Override
    public void onLoad() {
        super.onLoad();
        moonLogger = new MoonLogger(getLogger());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        INSTANCE = this;
        
        this.redisTarget = new RedisTarget(RedisTarget.RedisTargetType.PROXY);
        commons.init(RedisTarget.RedisTargetType.PROXY);
        this.redisMessageEvent = new RedisMessageEvent(this);
    
        RedissonClient redissonClient = this.commons.getPersistanceManager().getRedisManager().getRedissonClient();
        RTopic<String> rTopic = redissonClient.getTopic(RedisTarget.RedisTargetType.PROXY.getName());
        rTopic.addListener(this.commons.getPlatform().getMessageEvent());
    
        this.commons.getLogger().info("PubSub registered !");
        CustomServer customServer = null;
        System.out.println(this.commons.getConfig().getServerName());
        if (!this.commons.getPersistanceManager().getServerManager().exist(this.commons.getConfig().getServerName()))
        {
            this.commons.getLogger().info("Serveur not exist, processing...");
            customServer = new CustomServer(this.commons.getConfig().getServerName(), this.commons.getPlatform().getHost(),
                            ServerType.getByName(this.commons.getConfig().getServerType()), ServerStatus.STARTING,
                            ServerWhitelist.getByName(this.commons.getConfig().getServerWhitelist()), 0);
            this.commons.getPersistanceManager().getServerManager().add(customServer);
            this.commons.getLogger().info("Done");
        }
        else
        {
            customServer =
                    this.commons.getPersistanceManager().getServerManager().getCustomServer(this.commons.getConfig().getServerName());
        }
        customServer.setStatus(ServerStatus.STARTED);
        this.commons.getPersistanceManager().getServerManager().commit(customServer);
        this.commons.getPersistanceManager().getServerManager().update(customServer);
        
        staffManager = new StaffManager(this);
        sanctionUtils = new SanctionUtils(this);

        new Action(this);
    }
    
    @Override
    public void onDisable()
    {
        super.onDisable();
        CustomServer customServer = this.commons.getPersistanceManager().getServerManager().getCustomServer(this.commons.getConfig().getServerName());
        customServer.setStatus(ServerStatus.STOPED);
        customServer.setOnline(0);
        this.commons.getPersistanceManager().getServerManager().update(customServer);
        this.commons.getPersistanceManager().getServerManager().remove(customServer);
        for (ProxiedPlayer player : getProxy().getPlayers())
        {
            CustomPlayer customPlayer = this.commons.getPersistanceManager().getPlayerManager().getCustomPlayer(player.getUniqueId());
            this.commons.getPersistanceManager().getPlayerManager().update(customPlayer);
            this.commons.getPersistanceManager().getPlayerManager().remove(customPlayer);
        }
        this.commons.shutdown();
    }

    public static MoonBungeeCore get() {
        return INSTANCE;
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
        return PlatformType.BUNGEE;
    }

    @Override
    public String getPlatformName() {
        return commons.getConfig().getServerName();
    }

    @Override
    public String getPlatformVersion() {
        return getProxy().getVersion();
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
        return getProxy().getConfig().getPlayerLimit();
    }

    @Override
    public MessageListener<String> getMessageEvent() {
        return new RedisMessageEvent(this);
    }

    @Override
    public void executeAsync(Runnable runnable) {
        getProxy().getScheduler().runAsync(this, runnable);
    }

    public SanctionUtils getSanctionUtils() {
        return sanctionUtils;
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    @Override
    public void sendMessage(String name, String message) {
        ProxiedPlayer proxiedPlayer = getProxy().getPlayer(name);
        if (proxiedPlayer != null && proxiedPlayer.isConnected()) {
            proxiedPlayer.sendMessage(TextComponent.fromLegacyText(message));
        }
    }

    @Override
    public void shutdown() {
        new Message(MessageType.SERVER_DOWN.getContent()).broadcast();
        getProxy().getPlayers().forEach(player -> player.disconnect(TextComponent.fromLegacyText(MessageType.SERVER_DOWN.getContent())));
        getProxy().stop();
    }

    @Override
    public void reboot() {

    }
}
