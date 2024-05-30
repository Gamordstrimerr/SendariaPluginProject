package fr.gamordstrimer.testplugin.itemsystem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class Item {

    protected ItemStack itemStack;

    protected Recipe recipe;

    public Item() {
        this.itemStack = new ItemStack(setMaterial(), setQuantity());
        this.itemStack.setItemMeta(setItemMeta());
    }

    public abstract @NotNull Material setMaterial();

    public abstract int setQuantity();

    public abstract ItemMeta setItemMeta();

    public abstract void setRecipe();

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Recipe getRecipe() {
        return recipe;
    }

}