package fr.heavenmoon.core.bukkit.dm;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.commands.MessageCommand;
import fr.heavenmoon.core.bukkit.commands.ReplyCommand;
import fr.heavenmoon.core.common.utils.DCommand;
import fr.heavenmoon.persistanceapi.PersistanceManager;

import java.util.*;

public class DmManager
{
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	
	private Map<UUID, UUID> lastMsg;
	
	public DmManager(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
		this.lastMsg = new HashMap<>();
		new DCommand("message", "/message", "Direct Message", null, Arrays.asList("msg", "m"), new MessageCommand(plugin), plugin);
		new DCommand("reply", "/r", "Reply Direct Message", null, Collections.singletonList("r"), new ReplyCommand(plugin), plugin);
	}
	
	public void putMessage(UUID player, UUID target)
	{
		this.lastMsg.put(player, target);
	}
	
	public UUID getLastMessage(UUID uuid)
	{
		if (!this.lastMsg.containsKey(uuid))
			return null;
		return this.lastMsg.get(uuid);
	}
}