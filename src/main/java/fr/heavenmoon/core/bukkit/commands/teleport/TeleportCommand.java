package fr.heavenmoon.core.bukkit.commands.teleport;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.math.MathUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.MODERATEUR;
	private String syntax = "/teleport <pseudo|x, y, z> (<pseudo>)";
	
	public TeleportCommand(MoonBukkitCore plugin)
	{
		this.plugin = plugin;
		this.persistanceManager = plugin.getCommons().getPersistanceManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
	{
		if (!(sender instanceof Player))
		{
			new Message(MessageType.CONSOLE).send(sender);
			return false;
		}
		Player player = (Player) sender;
		CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
		if (!customPlayer.hasPermission(this.rank))
		{
			new Message(MessageType.PERMISSION, "%rank%", this.rank.getName()).send(sender);
			return false;
		}
		if (args.length == 1)
		{
			String name = args[0];
			plugin.getTeleportManager().teleportPlayer(customPlayer, name);
		}
		else if (args.length == 2)
		{
			String name = args[0];
			String target = args[1];
			plugin.getTeleportManager().teleportPlayerExternal(customPlayer, name, target);
		}
		else if (args.length == 3)
		{
			if (MathUtils.isDouble(args[0]) && MathUtils.isDouble(args[1]) && MathUtils.isDouble(args[2]))
			{
				plugin.getTeleportManager()
				      .teleportCoords(customPlayer, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
			}
			else
			{
				new Message(PrefixType.ERROR, "Veuillez entrer des coordonnées valides.").send(player);
			}
		}
		else if (args.length == 4)
		{
			String name = args[0];
			if (MathUtils.isDouble(args[1]) && MathUtils.isDouble(args[2]) && MathUtils.isDouble(args[3]))
			{
				plugin.getTeleportManager()
				      .teleportCoordsExternal(customPlayer, name, Double.parseDouble(args[1]), Double.parseDouble(args[2]),
						      Double.parseDouble(args[3]));
			}
			else
			{
				(new Message(PrefixType.ERROR, "Veuillez entrer des coordonnée valides.")).send(player);
			}
		}
		else
		{
			(new Message(MessageType.SYNTAXE, "%syntax%", this.syntax)).send(sender);
		}
		return false;
	}
}