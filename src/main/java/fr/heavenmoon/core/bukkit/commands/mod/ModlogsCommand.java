package fr.heavenmoon.core.bukkit.commands.mod;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.bukkit.format.Message;
import fr.heavenmoon.core.bukkit.utils.BUniqueID;
import fr.heavenmoon.core.common.format.FormatUtils;
import fr.heavenmoon.core.common.format.message.MessageType;
import fr.heavenmoon.core.common.format.message.PrefixType;
import fr.heavenmoon.core.common.utils.time.CustomDate;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import fr.heavenmoon.persistanceapi.customs.player.CustomSanction;
import fr.heavenmoon.persistanceapi.customs.player.data.RankList;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModlogsCommand implements CommandExecutor {
    private final MoonBukkitCore plugin;

    private PersistanceManager persistanceManager;

    private RankList rank = RankList.GUIDE;

    private String syntax;

    public ModlogsCommand(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CustomPlayer customPlayer = persistanceManager.getPlayerManager().getCustomPlayer(player.getUniqueId());
            CustomPlayer customTarget = persistanceManager.getPlayerManager().getCustomPlayer(BUniqueID.get(args[0]));
            if (!customPlayer.hasPermission(this.rank)) {
                new Message(PrefixType.ERROR, "Vous n'avez pas la permission!").send(customPlayer);
                return true;
            }
            if (args.length == 1) {

                if (persistanceManager.getSanctionManager().isMuted(customTarget) || persistanceManager
                        .getSanctionManager().isBanned(customTarget)) {
                    CustomSanction customSanction = persistanceManager.getSanctionManager().getCurrentCustomSanction(customTarget);

                    if (customSanction.isValid()) {
                        new Message("§d" + FormatUtils.medGraySpacer()).send(player);

                        CustomPlayer customModerator = persistanceManager.getPlayerManager().getCustomPlayer(customSanction.getPunisherUuid());

                        TextComponent lineOne = new TextComponent();

                        TextComponent prefix_1 = new TextComponent("[ModLogs] ");
                        prefix_1.setColor(ChatColor.LIGHT_PURPLE);

                        String type = "null";
                        switch (customSanction.getType()) {
                            case BAN:
                                type = "Banni";
                                break;
                            case MUTE:
                                type = "Réduit au silence";
                                break;
                        }

                        TextComponent sanction_1 = new TextComponent("§b" + customTarget.getName() + " §fest §b" + type + "§f.");
                        lineOne.addExtra(prefix_1);
                        lineOne.addExtra(sanction_1);
                        player.spigot().sendMessage(lineOne);

                        long sTime = (customSanction.getExpirationTime() - customSanction.getCreationTime()) / 1000L;

                        TextComponent lineTwo = new TextComponent();
                        TextComponent prefix_2 = new TextComponent(" ");
                        TextComponent sanction_2 = new TextComponent("§8• §f" + customSanction.getType().getName() + " ");
                        TextComponent time = new TextComponent("§8/ §f" + (new CustomDate(sTime)).getCleanFormat(sTime) + " ");
                        TextComponent info = new TextComponent("§c[i]");

                        info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§7Fin dans: §e"
                                + (new CustomDate(System.currentTimeMillis())).getDurationUntil(customSanction.getExpirationTime())
                                .replace(" jour(s), ", "j").replace(" minute(s), ", "m")
                                .replace(" seconde(s)", "s") + "\n\n§7Par: §e" + customModerator.getName() + "\n\n§7Raison: §e" + customSanction.getReason())).create()));
                        lineTwo.addExtra(prefix_2);
                        lineTwo.addExtra(sanction_2);
                        lineTwo.addExtra(time);
                        lineTwo.addExtra(info);
                        player.spigot().sendMessage(lineTwo);

                        new Message("§d" + FormatUtils.medGraySpacer()).send(player);
                    } else if (persistanceManager.getSanctionManager().cancelSanction(customSanction, false)) {
                        new Message(PrefixType.MODO, customTarget.getName() + " n'est pas sanctionné.")
                                .send(customPlayer);
                    } else {
                        new Message(PrefixType.ERROR, "sanction is invalid but sanctionCancel impossible, please contact an Administrator. (§8SanctionId: §7" + customSanction.getSanctionId() + "§8, time: §7" + System.currentTimeMillis() + "§c)").send(customPlayer);
                    }
                } else {
                    new Message(PrefixType.MODO, customTarget.getName() + " n'est pas sanctionné.")
                            .send(customPlayer);
                }
            } else {
                new Message(MessageType.SYNTAXE.getContent().replace("%syntax%", this.syntax)).send(customPlayer);
            }
        }
        return false;
    }
}