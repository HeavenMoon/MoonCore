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
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.redisson.api.listener.MessageListener;

import java.nio.file.Path;

public class MoonBungeeCore extends Plugin implements MoonPlatform {

    private static MoonBungeeCore INSTANCE;

    private MoonCommons commons;
    private MoonLogger moonLogger;

    private StaffManager staffManager;
    private SanctionUtils sanctionUtils;

    @Override
    public void onLoad() {
        moonLogger = new MoonLogger(getLogger());
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.commons = new MoonCommons(this);
        commons.init(RedisTarget.RedisTargetType.PROXY);

        staffManager = new StaffManager(this);
        sanctionUtils = new SanctionUtils(this);

        new Action(this);
    }

    public static MoonBungeeCore get() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        commons.shutdown();
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
        return commons.getJoConfig().get("server-name").toString();
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
