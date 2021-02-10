package fr.heavenmoon.core.bukkit.mod;

import fr.heavenmoon.core.common.utils.builders.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum ModItems {
    KNOCKBACK(new ItemBuilder()
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE).setMaterial(Material.WOOD_SWORD)
            .addEnchantment(Enchantment.KNOCKBACK, 1)
            .setDisplayName(ChatColor.GRAY + "Knockback").setUnbreakable(true).toItemStack()),

    KNOCKBACK2(new ItemBuilder()
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE).setMaterial(Material.WOOD_SWORD)
            .addEnchantment(Enchantment.KNOCKBACK, 2)
            .setDisplayName(ChatColor.GRAY + "Knockback II").setUnbreakable(true).toItemStack()),

    STRIP(new ItemBuilder()
            .setItemFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).setMaterial(Material.GOLD_HOE)
            .setDisplayName(ChatColor.YELLOW + "Strip").setUnbreakable(true).toItemStack()),

    FREEZE(new ItemBuilder()
            .setItemFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).setMaterial(Material.DIAMOND_HOE)
            .setDisplayName(ChatColor.BLUE + "Freeze").setUnbreakable(true).toItemStack()),

    VANISH(new ItemBuilder()
            .setMaterial(Material.INK_SACK)
            .setData((byte) 10)
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE).setDisplayName(ChatColor.GRAY + "Invisibilitée " + ChatColor.GREEN + "(Activée)").setUnbreakable(true)
            .toItemStack()),

    VANISHON(new ItemBuilder()
            .setMaterial(Material.INK_SACK)
            .setData((byte) 10)
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE).setDisplayName(ChatColor.GRAY + "Invisibilitée " + ChatColor.GREEN + "(Activée)").setUnbreakable(true)
            .toItemStack()),

    VANISHOFF(new ItemBuilder()
            .setMaterial(Material.INK_SACK)
            .setData((byte) 8)
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE).setDisplayName(ChatColor.GRAY + "Invisibilitée " + ChatColor.RED + "(Désactivée)")
            .setUnbreakable(true).toItemStack()),

    SPEEDOFF(new ItemBuilder()
            .setMaterial(Material.FEATHER)
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES).setDisplayName(ChatColor.GRAY + "Vitesse " + ChatColor.RED + "(Désactivée)").setUnbreakable(true)
            .toItemStack()),

    SPEEDON(new ItemBuilder()
            .setMaterial(Material.FEATHER)
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES).setDisplayName(ChatColor.GRAY + "Vitesse " + ChatColor.GREEN + "(Activée)").setUnbreakable(true)
            .toItemStack()),

    SANCTION(new ItemBuilder()
            .setItemFlag(ItemFlag.HIDE_UNBREAKABLE).setMaterial(Material.IRON_AXE).setDisplayName(ChatColor.GRAY + "Sanctions")
            .setUnbreakable(true).toItemStack()),

    INFO(new ItemBuilder()
            .setMaterial(Material.NAME_TAG).setDisplayName(ChatColor.GRAY + "Informations")
            .setUnbreakable(true).toItemStack());

    private ItemStack content;

    ModItems(ItemStack content) {
        this.content = content;
    }

    public ItemStack getContent() {
        return content;
    }

    public void setContent(ItemStack content) {
        this.content = content;
    }
}