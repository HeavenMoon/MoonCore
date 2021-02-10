package fr.heavenmoon.core.bukkit.listeners;

import fr.heavenmoon.core.bukkit.gui.AbstractGui;
import fr.heavenmoon.core.bukkit.gui.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;

public class GuiListener implements Listener
{
	private final GuiManager manager;
	
	public GuiListener(GuiManager manager)
	{
		this.manager = manager;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getWhoClicked() instanceof Player)
		{
			Player player = (Player) event.getWhoClicked();
			if (event.getCurrentItem() == null)
				return;
			AbstractGui gui = this.manager.getPlayerGui(player);
			if (gui != null)
			{
				if (event.getClickedInventory() instanceof PlayerInventory)
					return;
				String action = gui.getAction(event.getSlot());
				if (action != null)
					gui.onClick(player, event.getCurrentItem(), action, event.getClick());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if (this.manager.getPlayerGui(event.getPlayer()) != null)
			this.manager.removeClosedGui((Player) event.getPlayer());
	}
}
