package fr.heavenmoon.core.bukkit.mod;

import fr.heavenmoon.persistanceapi.customs.player.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerModEvent extends Event implements Cancellable
{
    private static final HandlerList handlerList = new HandlerList();
    
    private CustomPlayer customPlayer;
    
    private Player player;
    
    public PlayerModEvent(CustomPlayer customPlayer, Player player) {
        this.customPlayer = customPlayer;
        this.player = player;
    }
    
    public static HandlerList getHandlerList() {
      return handlerList;
    }
    
    public HandlerList getHandlers() {
      return handlerList;
    }
    
    public boolean isCancelled() {
      return false;
    }
    
    public void setCancelled(boolean b) {}
    
    public CustomPlayer getCustomPlayer() {
      return this.customPlayer;
    }
    
    public Player getPlayer() {
      return this.player;
    }
}
