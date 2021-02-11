package fr.heavenmoon.core.bukkit.commands.mod;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor
{
	
	private final MoonBukkitCore plugin;
	private final PersistanceManager persistanceManager;
	private RankList rank = RankList.MODERATEUR;
	private String syntax = "/vanish (show)";
	
	public VanishCommand(MoonBukkitCore plugin)
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
			(new Message(MessageType.PERMISSION, "%rank%", this.rank.getName())).send(sender);
			return false;
		}
		if (args.length == 0)
		{
			if (!customPlayer.getModerationData().isEnable() && !customPlayer.getModerationData().isBypass())
			{
				(new Message(PrefixType.ERROR,
						"Vous devez être en mode modération pour effectuer cette action. Si cette erreur persiste, essayez de changer de serveur."))
						.send(player);
				return false;
			}
			plugin.getVanishManager().toggleVanish(player);
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("show"))
		{
			plugin.getVanishManager().showAll(player);
		}
		else
		{
			(new Message(MessageType.SYNTAXE, "%syntax%", this.syntax)).send(sender);
		}
		return false;
	}
}
