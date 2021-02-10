package fr.heavenmoon.core.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.heavenmoon.core.common.logger.LoggerAdapter;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import fr.heavenmoon.persistanceapi.customs.redis.RedisTarget;
import fr.heavenmoon.core.common.scheduler.ThreadFactoryBuilder;
import fr.heavenmoon.persistanceapi.customs.server.CustomServer;
import fr.heavenmoon.persistanceapi.customs.server.ServerStatus;
import fr.heavenmoon.persistanceapi.customs.server.ServerType;
import fr.heavenmoon.persistanceapi.customs.server.ServerWhitelist;
import fr.heavenmoon.persistanceapi.managers.AddressManager;
import fr.heavenmoon.persistanceapi.managers.PlayerManager;
import fr.heavenmoon.persistanceapi.managers.ServerManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import fr.heavenmoon.persistanceapi.PersistanceManager;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MoonCommons {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().name("moon-scheduler").daemon());

    private final Gson gson = new Gson();
    private final Gson gsonPrettyPrint = new GsonBuilder().setPrettyPrinting().create();

    private final MoonPlatform platform;

    private Path configFile;
    private JSONObject joConfig;
    private String config;
    
    private Path redisConfigFile;
    private JSONObject joRedisConfig;
    private String redisConfig;
    
    private String serverName;
    
    private PersistanceManager persistanceManager;

    public MoonCommons(MoonPlatform platform) {
        this.platform = platform;
    }

    public void init(RedisTarget.RedisTargetType serverType) {
        configFile = platform.getDataDirectory().resolve("config.json");
        redisConfigFile = platform.getDataDirectory().resolve("redisConfig.json");
        
        FileReader fileReader = null;
        try
        {
            JSONParser jsonParser = new JSONParser();
    
            fileReader = new FileReader(configFile.toFile());
            Object oConfig = jsonParser.parse(fileReader);
            this.joConfig = (JSONObject) oConfig;
            this.config = joConfig.toJSONString();
            
            fileReader = new FileReader(redisConfigFile.toFile());
            Object oRedisConfig = jsonParser.parse(fileReader);
            this.joRedisConfig = (JSONObject) oRedisConfig;
            this.redisConfig = joRedisConfig.toJSONString();
            
            this.serverName = joConfig.get("server-name").toString();
            this.persistanceManager = new PersistanceManager(config, redisConfig);
            
            if (!persistanceManager.getServerManager().exist(serverName))
            {
                System.out.println("No CustomServer found");
                CustomServer customServer = new CustomServer(serverName, InetAddress.getLocalHost().getHostAddress(),
                        ServerType.getByName((String) joConfig.get("type"))
                        , ServerStatus.STARTING, ServerWhitelist.getByName((String) joConfig.get("whitelist")), 0);
                persistanceManager.getServerManager().add(customServer);
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        RedissonClient redissonClient = persistanceManager.getRedisManager().getRedissonClient();
        RTopic<String> rTopic = redissonClient.getTopic(serverType.getName());
        rTopic.addListener(platform.getMessageEvent());
        platform.getCommons().getLogger().info("PubSub registered !");
    
        CustomServer customServer = persistanceManager.getServerManager().getCustomServer(RedisKey.SERVER, serverName);
        customServer.setStatus(ServerStatus.STARTED);
        persistanceManager.getServerManager().commit(RedisKey.SERVER, customServer);
        persistanceManager.getServerManager().update(customServer);
    }

    public void shutdown() {
        platform.getCommons().getLogger().info("Shutting down commons...");
        CustomServer customServer = persistanceManager.getServerManager().getCustomServer(RedisKey.SERVER, serverName);
        customServer.setStatus(ServerStatus.STOPED);
        persistanceManager.getServerManager().commit(RedisKey.SERVER, customServer);
        persistanceManager.getServerManager().update(customServer);
        persistanceManager.getServerManager().remove(RedisKey.SERVER, customServer);
        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            getLogger().warn("Error while shutting down scheduler", e);
        }

        platform.getCommons().getLogger().info("Success down commons !");
    }

    public void saveConfig() throws IOException {
        if (!Files.isDirectory(platform.getDataDirectory())) {
            Files.createDirectories(platform.getDataDirectory());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
            gsonPrettyPrint.toJson(config, writer);
        }
    }
    
    public String getServerName()
    {
        return serverName;
    }
    
    public PersistanceManager getPersistanceManager()
    {
        return persistanceManager;
    }
    
    public JSONObject getJoConfig()
    {
        return joConfig;
    }
    
    public JSONObject getJoRedisConfig()
    {
        return joRedisConfig;
    }
    
    public LoggerAdapter getLogger() {
        return platform.getMoonLogger();
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public MoonPlatform getPlatform() {
        return platform;
    }

    public Gson getGson() {
        return gson;
    }

    public Gson getGsonPrettyPrint() {
        return gsonPrettyPrint;
    }

}
