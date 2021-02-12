package fr.heavenmoon.core.common.utils;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class CommandBlocker {
    private static final String MINECRAFT_PREFIX = "minecraft";
    private static final String BUKKIT_PREFIX = "bukkit";
    private static final String SPIGOT_PREFIX = "spigot";
    private static final String PROTOCOLLIB_PREFIX = "protocollib";
    private static final String SONARPET_PREFIX = "sonarpet";
    private static final String LIBSDISGUISES_PREFIX = "libsdisguises";
    private static final String PROTOCOLSUPPORT_PREFIX = "protocolsupport";
    private static final String VIAVERSION_PREFIX = "viaversion";
    private static final String WORLDEDIT_PREFIX = "worldedit";

    public static void removeCommands() {
        try {
            // Minecraft
            removeCommand(MINECRAFT_PREFIX, "help");
            removeCommand(MINECRAFT_PREFIX, "tell");
            removeCommand(MINECRAFT_PREFIX, "me");
            removeCommand(MINECRAFT_PREFIX, "trigger");
            removeCommand(MINECRAFT_PREFIX, "ban");
            removeCommand(MINECRAFT_PREFIX, "ban-ip");
            removeCommand(MINECRAFT_PREFIX, "banlist");

            // Bukkit
            removeCommand(BUKKIT_PREFIX, "about", "version", "ver", "icanhasbukkit");
            removeCommand(BUKKIT_PREFIX, "plugins", "pl");
            removeCommand(BUKKIT_PREFIX, "help", "?");
            removeCommand(BUKKIT_PREFIX, "me");
            removeCommand(BUKKIT_PREFIX, "save-all", "save-off", "save-on");
            removeCommand(BUKKIT_PREFIX, "trigger");

            
            // Spigot
            removeCommand(SPIGOT_PREFIX, "restart");

            // ProtocolLib
            removeCommand(PROTOCOLLIB_PREFIX, "*");

            // SonarPet
            removeCommand(SONARPET_PREFIX, "*");

            // LibsDisguises
            removeCommand(LIBSDISGUISES_PREFIX, "*");
            // ProtocolSupport
            removeCommand(PROTOCOLSUPPORT_PREFIX, "*");

            // ViaVersion
            removeCommand(VIAVERSION_PREFIX, "*");

            // WorldEdit
            removeCommand(WORLDEDIT_PREFIX, "*");
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void removeCommand(String prefix, String... str) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SimpleCommandMap scm = getCommandMap();
        Map knownCommands = (Map) Reflection.getValue(scm, true, "knownCommands");

        for (String cmd : str) {
            if (cmd.equals("*")) {
                for (String knownCommand : new HashSet<String>(knownCommands.keySet())) {
                    if (knownCommand.startsWith(prefix)) {
                        knownCommands.remove(knownCommand);

                        if (knownCommands.containsKey(":"))
                            knownCommands.remove(knownCommand.split(":")[1]);
                    }
                }
            } else {
                knownCommands.remove(cmd);

                knownCommands.remove(prefix + ":" + cmd);
            }
        }
    }

    private static SimpleCommandMap getCommandMap() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> craftServerClass = Reflection.getOBCClass("CraftServer");
        Method getCommandMapMethod = craftServerClass.getMethod("getCommandMap");

        return (SimpleCommandMap) getCommandMapMethod.invoke(craftServerClass.cast(Bukkit.getServer()));
    }
}