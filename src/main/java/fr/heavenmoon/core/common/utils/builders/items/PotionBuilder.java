package fr.heavenmoon.core.common.utils.builders.items;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Map;

/**
 * @author Fallancy
 * @project Fallancy (Rapini)
 * @date 03.01.2019
 */
public class PotionBuilder {

    private PotionType type;
    private int amount;
    private int level;
    private boolean extended;
    private boolean splash;
    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;

    public PotionBuilder(PotionType type) {
        this.type = null;
        this.amount = Integer.valueOf(1);
        this.level = 1;
        this.extended = false;
        this.splash = false;
        this.name = null;
        this.lore = null;
        this.enchantments = null;
        this.setType(type);
    }

    public PotionType getType() {
        return this.type;
    }

    public PotionBuilder setType(PotionType type) {
        this.type = type;
        return this;
    }

    public int getAmount() {
        return this.amount;
    }

    public PotionBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public int getLevel() {
        return this.level;
    }

    public PotionBuilder setLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public PotionBuilder setExtended(boolean extended) {
        this.extended = extended;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public PotionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public PotionBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public boolean isSplash() {
        return this.splash;
    }

    public PotionBuilder setSplash(boolean splash) {
        this.splash = splash;
        return this;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    public PotionBuilder setEnchantments(
            Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public PotionBuilder addEnchantement(Enchantment enchantment,
                                         Integer level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemStack build() {
        Potion popo = new Potion(this.getType(), this.getLevel(),
                this.isSplash());
        popo.setHasExtendedDuration(this.extended);
        ItemStack item = popo.toItemStack(this.getAmount());
        if (this.name != null || this.lore != null) {
            ItemMeta meta = item.getItemMeta();

            if (this.name != null) {
                meta.setDisplayName(this.name);
            }
            if (this.lore != null) {
                meta.setLore(this.lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

}
