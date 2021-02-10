package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DevCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	
	public DevCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		
		if (sender instanceof Player)
		{
			
			if (args.length == 1)
			{
				String target = args[0];
				String off_uuid = Bukkit.getOfflinePlayer(target).getUniqueId().toString();
				String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + target).getBytes()).toString();
				new Message("Offline UUID: " + off_uuid).send(sender);
				new Message("FromBytes UUID: " + uuid).send(sender);
			}
		}
		return false;
	}
}
