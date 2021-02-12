package fr.heavenmoon.core.common.utils;

import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class UniqueID
{
	
	private static String PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";
	private static String PLAYERDB = "http://playerdb.co/api/player/minecraft/";
	private static JSONParser JSON_PARSER = new JSONParser();
	
	public static String generate()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String generate(String name)
	{
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
		return offlinePlayer.getUniqueId().toString();
	}
	
	// public static String get(Player player) { return get(player.getName()); }

//    public static String get(String name) {
//        if (CustomUniqueId.exists(name)) return CustomUniqueId.get(name).getUuid();
//
//        String uuid = getMojang(name);
//        if (uuid == null) {
//            uuid = generate(name);
//        } else {
//            uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
//        }
//        new CustomUniqueId(name, uuid).commit();
//
//        return uuid;
//    }
	
	public static String getMojang(String name)
	{
		List<String> APIS = Arrays.asList(PROFILE_URL, PLAYERDB);
		Random random = new Random();
		String api = APIS.get(random.nextInt(APIS.size()));
		try
		{
			URL url = new URL(api + name);
			
			JSONObject json;
			
			try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())))
			{
				json = (JSONObject) JSON_PARSER.parse(in);
			}
			catch (ParseException e)
			{
				return null;
			}
			
			String id = (String) json.get("id");
			
			if (id != null)
			{
				ProxyServer.getInstance().getLogger().info(name + " is premium");
				return convertUUID(id);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ProxyServer.getInstance().getLogger().info(name + " is cracked");
		
		return null;
	}
	
	public static boolean isPremium(String name)
	{
		try
		{
			URL url = new URL(PROFILE_URL + name);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				return true;
			}
			else if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT)
			{
				return false;
			}
			else
			{
				return false;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
		
	}
	
	private static String convertUUID(String uuid)
	{
		return new UUID(Long.parseUnsignedLong(uuid.substring(0, 16), 16), Long.parseUnsignedLong(uuid.substring(16), 16)).toString()
		                                                                                                                  .replace("-", "");
	}
	
}
