package fr.heavenmoon.core.bungee.redis;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.bungee.listeners.redis.RedisConnectListener;
import fr.heavenmoon.core.bungee.listeners.redis.RedisSanctionListener;
import fr.heavenmoon.core.bungee.listeners.redis.RedisTeleportListener;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.UniqueID;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.managers.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.managers.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.managers.redis.RedisTarget;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.listener.MessageListener;

import java.util.List;

public class RedisMessageEvent implements MessageListener {

    private final MoonBungeeCore plugin;
    private final PersistanceManager persistanceManager;

    public RedisMessageEvent(MoonBungeeCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void onMessage(String channel, Object message) {
        PubSubMessage pubSubMessage = PubSubMessage.fromJson((String) message);
        String title = pubSubMessage.getTitle();
        
        switch (title) {
            case "Connect":
                new RedisConnectListener(channel, pubSubMessage, persistanceManager);
                break;
            case "Sanction":
                new RedisSanctionListener(channel, pubSubMessage, persistanceManager);
                break;
            case "Teleport":
                new RedisTeleportListener(channel, pubSubMessage, persistanceManager);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + title);
        }
    }
}
