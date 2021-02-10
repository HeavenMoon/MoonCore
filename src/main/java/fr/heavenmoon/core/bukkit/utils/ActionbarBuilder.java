package fr.heavenmoon.core.bukkit.utils;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionbarBuilder {
    private String message;

    private int stay;

    public ActionbarBuilder(String message, int stay) {
        this.message = message;
        this.stay = stay;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStay() {
        return this.stay;
    }

    private IChatBaseComponent buildChatComponent(String msg) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}");
    }

    public void clear(Player player) {
        PlayerConnection connection = (((CraftPlayer) player).getHandle()).playerConnection;
        PacketPlayOutChat clear = new PacketPlayOutChat(buildChatComponent(""), (byte) 2);
        connection.sendPacket(clear);
    }

    public void send(Player player) {
        PlayerConnection connection = (((CraftPlayer) player).getHandle()).playerConnection;
        PacketPlayOutChat packet = new PacketPlayOutChat(buildChatComponent(this.message), (byte) 2);
        connection.sendPacket(packet);
        if (this.stay > 0)
            Bukkit.getScheduler().runTaskLater(MoonBukkitCore.get(), () -> clear(player), this.stay * 20L);
    }
}
