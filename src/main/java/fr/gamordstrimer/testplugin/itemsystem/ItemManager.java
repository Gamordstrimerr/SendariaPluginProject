package fr.gamordstrimer.testplugin.itemsystem;

import fr.gamordstrimer.testplugin.itemsystem.item.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {

    private static final Map<String, Item> customItems = new LinkedHashMap<>();
    private static final Map<String, Recipe> customItemsRecipes = new LinkedHashMap<>();

    public static Map<String, Item> getCustomItems() {
        return customItems;
    }

    public static Recipe getRecipe(String itemName) {
        return customItemsRecipes.get(itemName);
    }

    public static ItemStack getCustomItem(String itemName) {
        Item item = customItems.get(itemName);
        return item != null ? item.getItemStack() : null;
    }

    public void initializeCustomItems() {
        registerItem(new CurseSword(), "curse_sword");
        registerItem(new CurseHelmet(), "curse_helmet");
        registerItem(new CurseChestplate(), "curse_chestplate");
        registerItem(new CurseLeggings(), "curse_leggings");
        registerItem(new CurseBoots(), "curse_boots");
        registerItem(new BlindnessPotion(), "blindness_potion");
        registerItem(new HeadBand(), "headband");
        registerItem(new InvisibleItemFrame(), "invisible_item_frame");
        registerItem(new MagicLantern(), "magic_lantern");
        registerItem(new TeleportationStone(), "teleportation_stone");
    }

    private void registerItem(Item item, String key) {
        customItems.put(key, item);
        item.setRecipe();
        customItemsRecipes.put(key, item.getRecipe());
    }
}
