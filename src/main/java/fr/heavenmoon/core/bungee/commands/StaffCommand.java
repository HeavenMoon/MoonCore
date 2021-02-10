package fr.heavenmoon.core.bungee.commands;

import fr.heavenmoon.core.bungee.MoonBungeeCore;
import fr.heavenmoon.core.bungee.format.Message;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.utils.objects.ArrayUtils;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.redis.RedisKey;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffCommand extends Command {

    private RankList rank = RankList.BUILDER;
    private final MoonBungeeCore plugin;
    private final PersistanceManager persistanceManager;

    public StaffCommand(MoonBungeeCore plugin, String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER,
                    ((ProxiedPlayer) sender).getUniqueId());
            if (!customPlayer.hasPermission(rank)) {
                new Message(MessageType.PERMISSION, "%rank%", rank.getName()).send(sender);
                return;
            }
        }

        if (args.length == 0) {
            plugin.getStaffManager().getStaffList(sender);
        } else {
            if (!(sender instanceof ProxiedPlayer)) {
                new Message(MessageType.CONSOLE).send(sender);
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;

            String message = ArrayUtils.getArgumentsMin(args, 0);
            plugin.getStaffManager().sendStaffChatMessage(persistanceManager.getPlayerManager().getCustomPlayer(RedisKey.PLAYER,
                    player.getUniqueId()),
                    message);
        }
    }
}