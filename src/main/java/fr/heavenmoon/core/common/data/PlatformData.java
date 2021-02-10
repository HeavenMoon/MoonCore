package fr.heavenmoon.core.common.data;


import fr.heavenmoon.core.common.PlatformType;

public class PlatformData {

    private final PlatformType type;
    private final String name;
    private final String version;

    public PlatformData(PlatformType type, String name, String version) {
        this.type = type;
        this.name = name;
        this.version = version;
    }

    public PlatformType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
