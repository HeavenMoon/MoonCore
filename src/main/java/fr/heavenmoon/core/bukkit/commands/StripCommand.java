package fr.heavenmoon.core.bukkit.commands;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.ActionbarBuilder;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StripCommand implements CommandExecutor {

    private final MoonBukkitCore plugin;
private final PersistanceManager persistanceManager;
    private RankList rank = RankList.MODERATEUR;
    private String syntax = "/strip <player>";

    public StripCommand(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            new Message(MessageType.CONSOLE).send(sender);
            return false;
        }
        Player player = (Player) sender;
        CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
        if (!customPlayer.hasPermission(this.rank)) {
            new Message(MessageType.PERMISSION, "%rank%", this.rank.getName()).send(sender);
            return false;
        }
        if (args.length == 1) {
            String name = args[0];
            Player target = Bukkit.getPlayer(name);
            if (target != null) {
                CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(target.getUniqueId());
                target.getInventory().setArmorContents(null);
                new ActionbarBuilder(ChatColor.GRAY + "Le joueur " + ChatColor.getByChar(customTarget.getRankData().getStyleCode()) + customTarget.getRankData().getPrefix() + customTarget.getName() + ChatColor.GRAY + " n'a plus d'armure.", 4).send(player);
            } else {
                new Message(MessageType.NO_PLAYER).send(sender);
            }
        } else {
            new Message(MessageType.SYNTAXE, "%syntax%", this.syntax).send(sender);
        }
        return false;
    }
}
