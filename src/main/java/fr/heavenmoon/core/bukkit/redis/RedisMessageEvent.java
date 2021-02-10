package fr.heavenmoon.core.bukkit.redis;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.listeners.redis.RedisConnectListener;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.redis.PubSubMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        List<String> args = pubSubMessage.getArguments();

        if (title.equalsIgnoreCase("Teleport")) {
            new RedisConnectListener(channel, pubSubMessage, persistanceManager);
        }
    }

}
