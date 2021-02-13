package fr.heavenmoon.core.bukkit.redis;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.listeners.redis.RedisTeleportListener;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.managers.redis.PubSubMessage;
import org.redisson.api.listener.MessageListener;

import java.util.List;

public class RedisMessageEvent implements MessageListener {

    private final MoonBukkitCore plugin;
    private final PersistanceManager persistanceManager;
    public RedisMessageEvent(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public void onMessage(String channel, Object message) {
        PubSubMessage pubSubMessage = PubSubMessage.fromJson((String) message);
        String title = pubSubMessage.getTitle();

        switch (title)
        {
            case "Teleport":
                new RedisTeleportListener(channel, pubSubMessage, persistanceManager);
                break;
        }
    }

}
