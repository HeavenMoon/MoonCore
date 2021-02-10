package fr.heavenmoon.core.bukkit.commands.time;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class TimeUtils {
    public static void setDay() {
        for (World world : Bukkit.getWorlds())
            world.setTime(0L);
    }

    public static void setNight() {
        for (World world : Bukkit.getWorlds())
            world.setTime(13600L);
    }

    public static void setSun() {
        for (World world : Bukkit.getWorlds()) {
            world.setStorm(false);
            world.setThundering(false);
        }
    }

    public static void setRain() {
        for (World world : Bukkit.getWorlds())
            world.setStorm(true);
    }

    public static void setThunder() {
        for (World world : Bukkit.getWorlds())
            world.setThundering(true);
    }
}
