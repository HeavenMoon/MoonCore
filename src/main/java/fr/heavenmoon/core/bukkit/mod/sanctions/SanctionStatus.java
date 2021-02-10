package fr.heavenmoon.core.bukkit.mod.sanctions;

public enum SanctionStatus {
    CHAT("CHAT"),
    TRICHE("TRICHE"),
    AUTRE("AUTRE");

    public String name;

    SanctionStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
