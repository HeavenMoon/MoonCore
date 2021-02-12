package fr.heavenmoon.core.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.heavenmoon.core.common.config.PluginConfig;
import fr.heavenmoon.core.common.logger.LoggerAdapter;
import fr.heavenmoon.persistanceapi.config.DatabaseConfig;
import fr.heavenmoon.persistanceapi.config.RedisConfig;
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
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MoonCommons
{
	private static MoonCommons instance;
	
	private final ScheduledExecutorService scheduler =
			Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().name("moon-scheduler").daemon());
	
	private final Gson gson = new Gson();
	private final Gson gsonPrettyPrint = new GsonBuilder().setPrettyPrinting().create();
	
	private final MoonPlatform platform;
	
	private Path configFile;
	private PluginConfig config = new PluginConfig(null, null, null, null);
	
	private Path databaseConfigFile;
	private DatabaseConfig databaseConfig = new DatabaseConfig(null, null, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	
	private Path redisConfigFile;
	private RedisConfig redisConfig = new RedisConfig(null, 0, null, null, null, null, null, null, null);
	
	private PersistanceManager persistanceManager;
	
	public MoonCommons(MoonPlatform platform)
	{
		instance = this;
		this.platform = platform;
	}
	
	public void init(RedisTarget.RedisTargetType serverType)
	{
		configFile = platform.getDataDirectory().resolve("config.json");
		databaseConfigFile = platform.getDataDirectory().resolve("databaseConfig.json");
		redisConfigFile = platform.getDataDirectory().resolve("redisConfig.json");
		loadConfig();
		loadDatabaseConfig();
		loadRedisConfig();
		
		this.persistanceManager = new PersistanceManager(config.getServerName(), databaseConfig, redisConfig);
	}
	
	private void loadConfig()
	{
		try (BufferedReader reader = Files.newBufferedReader(this.configFile))
		{
			this.config = this.gson.fromJson(reader, PluginConfig.class);
			saveConfig();
			this.platform.getCommons().getLogger().info("Config loaded !");
		}
		catch (IOException | NoSuchFieldError e)
		{
			try
			{
				saveConfig();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			getLogger().error("Error while loading configuration", e);
			return;
		}
		if (!this.config.isValid())
		{
			getLogger().warn("Invalid configuration.");
			return;
		}
	}
	
	public void loadDatabaseConfig()
	{
		try (BufferedReader reader = Files.newBufferedReader(this.databaseConfigFile))
		{
			this.databaseConfig = (DatabaseConfig) this.gson.fromJson(reader, DatabaseConfig.class);
			saveDatabaseConfig();
			this.platform.getCommons().getLogger().info("Database config loaded !");
		}
		catch (IOException | NoSuchFieldError e)
		{
			try
			{
				saveConfig();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			getLogger().error("Error while loading database configuration", e);
			return;
		}
		if (!this.databaseConfig.isValid())
		{
			getLogger().warn("Invalid database configuration.");
			return;
		}
	}
	
	public void loadRedisConfig()
	{
		try (BufferedReader reader = Files.newBufferedReader(this.redisConfigFile))
		{
			this.redisConfig = (RedisConfig) this.gson.fromJson(reader, RedisConfig.class);
			saveRedisConfig();
			this.platform.getCommons().getLogger().info("Redis config loaded !");
		}
		catch (IOException | NoSuchFieldError e)
		{
			try
			{
				saveConfig();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			getLogger().error("Error while loading redis configuration", e);
			return;
		}
		if (!this.redisConfig.isValid())
		{
			getLogger().warn("Invalid redis configuration.");
			return;
		}
		
	}
	
	public void saveConfig() throws IOException
	{
		if (!Files.isDirectory(this.platform.getDataDirectory(), new java.nio.file.LinkOption[0]))
			Files.createDirectories(this.platform.getDataDirectory(), (FileAttribute<?>[]) new FileAttribute[0]);
		try (BufferedWriter writer = Files.newBufferedWriter(this.configFile, new java.nio.file.OpenOption[0]))
		{
			this.gsonPrettyPrint.toJson(this.config, writer);
		}
	}
	
	public void saveDatabaseConfig() throws IOException
	{
		if (!Files.isDirectory(this.platform.getDataDirectory(), new java.nio.file.LinkOption[0]))
			Files.createDirectories(this.platform.getDataDirectory(), (FileAttribute<?>[]) new FileAttribute[0]);
		try (BufferedWriter writer = Files.newBufferedWriter(this.databaseConfigFile, new java.nio.file.OpenOption[0]))
		{
			this.gsonPrettyPrint.toJson(this.databaseConfig, writer);
		}
	}
	
	public void saveRedisConfig() throws IOException
	{
		if (!Files.isDirectory(this.platform.getDataDirectory(), new java.nio.file.LinkOption[0]))
			Files.createDirectories(this.platform.getDataDirectory(), (FileAttribute<?>[]) new FileAttribute[0]);
		try (BufferedWriter writer = Files.newBufferedWriter(this.redisConfigFile, new java.nio.file.OpenOption[0]))
		{
			this.gsonPrettyPrint.toJson(this.redisConfig, writer);
		}
	}
	
	public void shutdown()
	{
		this.platform.getCommons().getLogger().info("Shutting down commons...");
		try
		{
			this.scheduler.awaitTermination(5L, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
			getLogger().warn("Error while shutting down scheduler", e);
		}
		persistanceManager.shutdown();
		this.platform.getCommons().getLogger().info("Success down commons !");
	}
	
	public static MoonCommons get()
	{
		return instance;
	}
	
	public LoggerAdapter getLogger()
	{
		return platform.getMoonLogger();
	}
	
	public ScheduledExecutorService getScheduler()
	{
		return scheduler;
	}
	
	public PluginConfig getConfig()
	{
		return this.config;
	}
	
	public DatabaseConfig getDatabaseConfig()
	{
		return this.databaseConfig;
	}
	
	public RedisConfig getRedisConfig()
	{
		return this.redisConfig;
	}
	
	public MoonPlatform getPlatform()
	{
		return platform;
	}
	
	public Gson getGson()
	{
		return gson;
	}
	
	public Gson getGsonPrettyPrint()
	{
		return gsonPrettyPrint;
	}
	
	public PersistanceManager getPersistanceManager()
	{
		return persistanceManager;
	}
}
