package fr.heavenmoon.core.common.utils.builders.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class HeadBuilder {
    private int amount;
    private String name;
    private String owner;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> flags;
    private boolean unbreakable;

    public HeadBuilder() {
        this.amount = 1;
        this.lore = new ArrayList<String>();
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.flags = new ArrayList<ItemFlag>();
    }

    public HeadBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public HeadBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public HeadBuilder setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public HeadBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public HeadBuilder setLore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public HeadBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public HeadBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public HeadBuilder setFlags(List<ItemFlag> flags) {
        this.flags = flags;
        return this;
    }

    public HeadBuilder setFlags(ItemFlag... flags) {
        this.flags = Arrays.asList(flags);
        return this;
    }

    public HeadBuilder addFlag(ItemFlag flag) {
        this.flags.add(flag);
        return this;
    }

    public HeadBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, this.amount);
        item.setDurability((short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(this.name);
        meta.setOwner(this.owner);
        meta.setLore(this.lore);
        this.enchantments.entrySet().forEach(entry -> meta.addEnchant(entry.getKey(), entry.getValue(), true));
        this.flags.forEach(entry -> meta.addItemFlags(new ItemFlag[]{entry}));
        meta.spigot().setUnbreakable(this.unbreakable);
        item.setItemMeta(meta);
        return item;
    }

}