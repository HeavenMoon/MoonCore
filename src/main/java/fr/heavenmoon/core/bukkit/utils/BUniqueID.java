package fr.heavenmoon.core.bukkit.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BUniqueID
{
	private static final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private static final String PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String PLAYERDB = "http://playerdb.co/api/player/minecraft/";
	private static JSONParser JSON_PARSER = new JSONParser();
	
	public static String generate()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String getMojang(String name)
	{
		List<String> APIS = Arrays.asList(PROFILE_URL, PLAYERDB);
		Random random = new Random();
		String api = APIS.get(random.nextInt(APIS.size()));
		switch (api)
		{
			case PLAYERDB:
				HttpGet request = new HttpGet(PLAYERDB + "DAYELA");
				
				// add request headers
				request.addHeader("custom-key", "mkyong");
				request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
				
				try (CloseableHttpResponse response = httpClient.execute(request))
				{
					
					HttpEntity entity = response.getEntity();
					Header headers = entity.getContentType();
					
					if (entity != null) {
						// return it as a String
						String result = EntityUtils.toString(entity);
						JSONParser parser = new JSONParser();
						JSONObject jsonObject = (JSONObject) parser.parse(result);
						JSONObject data = (JSONObject) parser.parse(jsonObject.get("data").toString());
						JSONObject player = (JSONObject) parser.parse(data.get("player").toString());
						return player.get("id").toString();
					}
					
				}
				catch (IOException | ParseException e)
				{
					e.printStackTrace();
				}
				break;
			case PROFILE_URL:
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
						return id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +
								id.substring(20, 32);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			default:
				return null;
		}
		return null;
		
	}
	
	public static String generate(String name)
	{
		return UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)).toString();
	}
	
	public static UUID get(String name)
	{
		if (Bukkit.getPlayer(name) != null) return Bukkit.getPlayer(name).getUniqueId();
		String uuid = getMojang(name);
		if (uuid == null) uuid = generate(name);
		return UUID.fromString(uuid);
	}
	
	
	private static String convertUUID(String uuid)
	{
		return (new UUID(Long.parseUnsignedLong(uuid.substring(0, 16), 16), Long.parseUnsignedLong(uuid.substring(16), 16))).toString()
		                                                                                                                    .replace("-",
				                                                                                                                    "");
	}
}