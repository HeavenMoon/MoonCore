package fr.heavenmoon.core.bukkit.mod.sanctions;

import fr.heavenmoon.core.common.utils.math.MathUtils;
import fr.heavenmoon.core.common.utils.time.DateUnity;
import fr.heavenmoon.persistanceapi.customs.player.SanctionType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SanctionList {
    FLOOD("Flood", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 3.0D, DateUnity.MINUTE, Material.SULPHUR),
    RACISME_DIFFAMATION("Propos racistes ou diffamatoires", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.HOUR, Material.FLINT_AND_STEEL),
    MESSAGE_INUTILE("Message Inutile", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.MINUTE, Material.BONE),
    FAUSSE_INFORMATION("Fausse information", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 5.0D, DateUnity.MINUTE, Material.FERMENTED_SPIDER_EYE),
    FORMATTAGE_INCORRECT("Formatage incorrect", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.MINUTE, Material.BLAZE_POWDER),
    MAUVAIS_LANGAGE("Mauvais langage", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 30.0D, DateUnity.MINUTE, Material.COOKED_FISH),
    INSULTE("Insulte(s)", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 10.0D, DateUnity.MINUTE, Material.GOLDEN_CARROT),
    PROVOCATION("Provocation", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 2.0D, DateUnity.MINUTE, Material.NETHER_STAR),
    CONTOURNEMENT_PROTECTION_CHAT("Contournement de la protection du chat", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 30.0D, DateUnity.MINUTE, Material.SUGAR_CANE),
    PUBLICITE_LIEN("Publicit(Lien)", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.DAY, Material.PAPER),
    PUBLICITE_NOM("Publicit(Nom)", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.HOUR, Material.NAME_TAG),
    MENACE_NON_MESUREE("Menace (Non mesurée)", "Infraction au chat: ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.HOUR, Material.BONE),
    HACK("Hack, DDoS, phishing, ...", "Infraction (Autre): ", SanctionStatus.CHAT, SanctionType.BAN, 1.0D, DateUnity.WEEK, Material.STRING),
    ARNAQUE("Arnaques, liens interdits, ...", "Infraction (Autre): ", SanctionStatus.CHAT, SanctionType.MUTE, 1.0D, DateUnity.WEEK, Material.FLINT_AND_STEEL),
    MENACE_MESUREE("Menace (mesurée)", "Infraction (Autre): ", SanctionStatus.CHAT, SanctionType.BAN, 1.0D, DateUnity.WEEK, Material.DEAD_BUSH),
    AUTOCLICK("Autoclick/CPS élevés", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.DAY, Material.SHEARS),
    FORCEFIELD("Forcefield", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.DIAMOND_SWORD),
    REACH("Reach", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.STICK),
    REGENERATION("Regénération", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.GOLDEN_APPLE),
    KNOCKBACK_MODIFIER("Knockback Modifier", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.ANVIL),
    FLY("Fly", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.FEATHER),
    GLIDE("Glide", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.MAGMA_CREAM),
    WATER_WALK("Water Walk (Jesus)", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.GOLD_BOOTS),
    NOFALL("NoFall", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.DIAMOND_BOOTS),
    SPEED("Speed", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 2.0D, DateUnity.WEEK, Material.SUGAR),
    SPIDER("Spider", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.WEB),
    XRAY("X-Ray", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.GLASS),
    OTHER("Autre", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.RAW_FISH),
    CONSTRUCTION_INAPROPRIEE("Construction inapropriée", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 7.0D, DateUnity.DAY, Material.GRASS),
    ANTIJEU("Antijeu", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 1.0D, DateUnity.DAY, Material.TNT),
    UTILISATION_BUG_MAJEUR("Utilisation d'un bug (Majeur)", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 1.0D, DateUnity.HOUR, Material.IRON_INGOT),
    PSEUDO_INCORRECT("Pseudo incorrect", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 1.0D, DateUnity.MONTH, Material.NAME_TAG),
    SKIN_INCORRECT("Skin incorrect", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 1.0D, DateUnity.HOUR, Material.LEATHER),
    INCITATION_INFRACTION("Incitation l'infraction", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 1.0D, DateUnity.HOUR, Material.EMERALD),
    DOUBLE_COMPTE("Double compte (Abusif)", "Infraction (Autre): ", SanctionStatus.AUTRE, SanctionType.BAN, 2.0D, DateUnity.MONTH, Material.BOOK_AND_QUILL),
    OTHER_LIFE(".", "Infraction (Triche): ", SanctionStatus.TRICHE, SanctionType.BAN, 9999.0D, DateUnity.DAY, Material.BARRIER);

    public String name;
    public String prefix;
    public SanctionStatus status;
    public SanctionType sanction;
    public double time;
    public DateUnity unity;
    public Material material;

    SanctionList(String name, String prefix, SanctionStatus status, SanctionType sanction, double time, DateUnity unity, Material material) {
        this.name = name;
        this.prefix = prefix;
        this.status = status;
        this.sanction = sanction;
        this.time = time;
        this.unity = unity;
        this.material = material;
    }

    public static List<SanctionList> get(SanctionStatus status) {
        List<SanctionList> sanctions = new ArrayList<>();
        Arrays.asList(values()).forEach(value -> {
            if (value.getStatus().equals(status))
                sanctions.add(value);
        });
        return sanctions;
    }

    public static SanctionList get(String name) {
        for (SanctionList sanctionList : values()) {
            if (name.contains(sanctionList.getName()))
                return sanctionList;
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public SanctionStatus getStatus() {
        return this.status;
    }

    public SanctionType getSanction() {
        return this.sanction;
    }

    public String getTime() {
        return this.time + this.unity.getName();
    }

    public String getTime(double multiplier) {
        return (multiplier * this.time) + this.unity.getName();
    }

    public String getPublicTime() {
        return this.time + " " + this.unity.getDisplayName();
    }

    public String getPublicTime(double multiplier) {
        return MathUtils.round(multiplier * this.time, 2) + " " + this.unity.getDisplayName();
    }

    public DateUnity getUnity() {
        return this.unity;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getReason() {
        return this.prefix + this.name;
    }
}
