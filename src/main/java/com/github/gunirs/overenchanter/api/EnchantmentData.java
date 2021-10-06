package com.github.gunirs.overenchanter.api;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentData {
    private Enchantment enchantment;
    private int level;

    public EnchantmentData(Enchantment enchantment) {
        this(enchantment, 1);
    }

    public EnchantmentData(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return enchantment.getName();
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
