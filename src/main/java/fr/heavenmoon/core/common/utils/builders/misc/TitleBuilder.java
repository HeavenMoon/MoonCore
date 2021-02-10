package fr.heavenmoon.core.common.utils.builders.misc;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleBuilder {

    private Player player;
    private String title;
    private String subtitle;
    private int fadeIn;
    private int fadeOut;
    private int duration;

    public TitleBuilder(Player player) {
        super();
        this.player = player;
        this.title = null;
        this.subtitle = null;
        this.fadeIn = 20;
        this.fadeOut = 20;
        this.duration = 40;
    }

    public Player getPlayer() {
        return player;
    }

    public TitleBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TitleBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public TitleBuilder setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public TitleBuilder setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public TitleBuilder setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public TitleBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public void sendTitle() {
        if (this.title != null) {
            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\""
                    + this.title + "\"}"), this.fadeIn, this.duration, this.fadeOut);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        }
        if (this.subtitle != null) {
            PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\""
                    + this.subtitle + "\"}"), this.fadeIn, this.duration, this.fadeOut);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
        }
    }

}
