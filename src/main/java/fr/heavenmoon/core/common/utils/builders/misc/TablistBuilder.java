package fr.heavenmoon.core.common.utils.builders.misc;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TablistBuilder {

    private Player player;
    private String top;
    private String bottom;

    public TablistBuilder(Player player) {
        super();
        this.player = player;
        this.top = null;
        this.bottom = null;
    }

    public Player getPlayer() {
        return player;
    }

    public TablistBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public String getTop() {
        return top;
    }

    public TablistBuilder setTop(String top) {
        this.top = top;
        return this;
    }

    public String getBottom() {
        return bottom;
    }

    public TablistBuilder setBottom(String bottom) {
        this.bottom = bottom;
        return this;
    }

    public void changeTablistPlayer() {
        if (!this.top.isEmpty() && !this.bottom.isEmpty()) {
            IChatBaseComponent tabHead = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + this.top + "\"}");
            IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + this.bottom + "\"}");
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHead);
            try {
                Field field = packet.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(packet, tabFoot);

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}