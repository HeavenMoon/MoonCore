package fr.heavenmoon.core.bukkit.alts;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.address.CustomAddress;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;

import java.util.Map;

public class AltsManager
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	
	public AltsManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public Map<String, Integer> getAlts(CustomPlayer customPlayer)
	{
		String address = customPlayer.getLastIP();
		CustomAddress customAddress = persistanceManager.getAddressManager().get(address);
		return customAddress.getAccounts();
	}
	
	public Map<String, Integer> getAlts(String address)
	{
		CustomAddress customAddress = persistanceManager.getAddressManager().get(address);
		return customAddress.getAccounts();
	}
}
