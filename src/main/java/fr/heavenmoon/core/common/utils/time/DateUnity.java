package fr.heavenmoon.core.common.utils.time;

public enum DateUnity {
    YEAR("years", "année(s)", Long.valueOf("31536000000")),
    MONTH("months", "mois", Long.valueOf("2592000000")),
    WEEK("weeks", "semaine(s)", 604800000),
    DAY("days", "jour(s)", 86400000),
    HOUR("hours", "heure(s)", 3600000),
    MINUTE("minutes", "minute(s)", 60000),
    SECONDS("seconds", "seconde(s)", 1000);

    public String name;
    public String displayName;
    public long millis;

    DateUnity(String name, String displayName, long millis) {
        this.name = name;
        this.displayName = displayName;
        this.millis = millis;
    }

    public static DateUnity get(String name) {
        for (DateUnity dateUnity : DateUnity.values()) {
            if (dateUnity.getName().equalsIgnoreCase(name))
                return dateUnity;
        }
        return null;
    }

    public static DateUnity contains(String string) {
        for (DateUnity dateUnity : DateUnity.values()) {
            if (string.contains(dateUnity.getName().toLowerCase()))
                return dateUnity;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getMillis() {
        return millis;
    }
}
