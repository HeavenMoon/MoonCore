package fr.heavenmoon.core.bukkit.gui;

import fr.heavenmoon.core.bukkit.MoonBukkitCore;
import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import fr.heavenmoon.persistanceapi.PersistanceManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

public abstract class AbstractGui {

    protected MoonBukkitCore plugin;
    
    protected PersistanceManager persistanceManager;

    protected TreeMap<Integer, String> actions = new TreeMap<>();

    protected Inventory inventory;

    public AbstractGui(MoonBukkitCore plugin) {
        this.plugin = plugin;
        this.persistanceManager = plugin.getCommons().getPersistanceManager();
    }

    public AbstractGui() {
    }

    public abstract void display(Player paramPlayer);

    public void update(Player player) {
    }

    public void onClose(Player player) {
    }

    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        onClick(player, stack, action);
    }

    public void onClick(Player player, ItemStack stack, String action) {
    }

    public void setSlotData(Inventory inv, String name, Material material, int slot, String[] description, String action) {
        setSlotData(inv, name, new ItemStack(material, 1), slot, description, action);
    }

    public void setSlotData(String name, Material material, int slot, String[] description, String action) {
        setSlotData(this.inventory, name, new ItemStack(material, 1), slot, description, action);
    }

    public void setSlotData(String name, ItemStack item, int slot, String[] description, String action) {
        setSlotData(this.inventory, name, item, slot, description, action);
    }

    public void setSlotData(ItemBuilder item, int slot, String[] description, String action) {
        this.inventory.setItem(slot, item.toItemStack());
    }

    public void setSlotData(Inventory inv, String name, ItemStack item, int slot, String[] description, String action) {
        this.actions.put(slot, action);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        if (description != null)
            meta.setLore(Arrays.asList(description));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    public void setSlotData(Inventory inv, ItemStack item, int slot, String action) {
        this.actions.put(slot, action);
        inv.setItem(slot, item);
    }

    public void setSlotData(ItemStack item, int slot, String action) {
        setSlotData(this.inventory, item, slot, action);
    }

    public String getAction(int slot) {
        if (!this.actions.containsKey(slot))
            return null;
        return this.actions.get(slot);
    }

    public int getSlot(String action) {
        for (Iterator<Integer> iterator = this.actions.keySet().iterator(); iterator.hasNext(); ) {
            int slot = iterator.next();
            if (this.actions.get(slot).equals(action))
                return slot;
        }
        return 0;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}