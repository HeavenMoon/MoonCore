package fr.heavenmoon.core.common.utils;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationsUtils {
    public static Location stringToLocation(String location) {
        List<String> locList = new ArrayList<>(Arrays.asList(location.split(",")));
        Location loc = new Location(Bukkit.getWorld(locList.get(0)), Double.parseDouble(locList.get(1)), Double.parseDouble(locList.get(2)),
                Double.parseDouble(locList.get(3)), Float.parseFloat(locList.get(4)), Float.parseFloat(locList.get(5)));
        return loc;
    }

    public static String locationToString(Location location) {
        String loc_string = "";
        loc_string = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + ","
                + location.getYaw() + "," + location.getPitch();
        return loc_string;
    }
}

