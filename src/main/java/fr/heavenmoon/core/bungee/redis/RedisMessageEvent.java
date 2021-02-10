package fr.heavenmoon.core.bungee.redis;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.bungee.listeners.redis.RedisConnectListener;
import fr.heavenmoon.core.bungee.listeners.redis.RedisSanctionListener;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.persistanceapi.customs.redis.PubSubMessage;
import fr.heavenmoon.persistanceapi.customs.redis.RedisPublisher;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.ChatColor;
import org.redisson.api.listener.MessageListener;

import java.net.Socket;
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
        List<String> args = pubSubMessage.getArguments();

        ProxiedPlayer player;
        CustomPlayer customPlayer;

        switch (title) {
            case "Connect":
                new RedisConnectListener(channel, pubSubMessage, persistanceManager);
                break;
            case "Sanction":
                new RedisSanctionListener(channel, pubSubMessage, persistanceManager);
                break;
            case "Teleport":
                if (args.get(0).equalsIgnoreCase("GlobalTeleport")) {
                    String playername = args.get(1);
                    String targetname = args.get(2);

                    player = ProxyServer.getInstance().getPlayer(playername);
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetname);
                    if (player.getServer() != target.getServer()) {
                        player.connect(target.getServer().getInfo());
                    }

                    new RedisPublisher(persistanceManager, "Teleport").setArguments("Teleport", playername, targetname).publish(new RedisTarget(RedisTarget.RedisTargetType.SERVER));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + title);
        }
    }
}
