package fr.gamordstrimer.testplugin.itemsystem;

import fr.gamordstrimer.testplugin.itemsystem.item.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManager {

    private static final Map<String, ItemStack> customItems = new LinkedHashMap<>();
    private static final Map<String, Recipe> customItemsRecipes = new LinkedHashMap<>();


    public void initializeCustomItems() {

        // Initialize CurseSword and add it to the HashMap

        /////////////////////////////////////////////////////////////////////

        //CURSE_SWORD
        CurseSword curseSword = new CurseSword();
        curseSword.setRecipe();
        customItems.put("curse_sword", curseSword.getItemStack());
        customItemsRecipes.put("curse_sword", curseSword.getRecipe());

        //CURSE_HELMET
        CurseHelmet curseHelmet = new CurseHelmet();
        curseHelmet.setRecipe();
        customItems.put("curse_helmet", curseHelmet.getItemStack());
        customItemsRecipes.put("curse_helmet", curseHelmet.getRecipe());

        //CURSE_CHESTPLATE
        CurseChestplate curseChestplate = new CurseChestplate();
        curseChestplate.setRecipe();
        customItems.put("curse_chestplate", curseChestplate.getItemStack());
        customItemsRecipes.put("curse_chestplate", curseChestplate.getRecipe());

        //CURSE_LEGGINGS
        CurseLeggings curseLeggings = new CurseLeggings();
        curseLeggings.setRecipe();
        customItems.put("curse_leggings", curseLeggings.getItemStack());
        customItemsRecipes.put("curse_leggings", curseLeggings.getRecipe());

        //CURSE_BOOTS
        CurseBoots curseBoots = new CurseBoots();
        curseBoots.setRecipe();
        customItems.put("curse_boots", curseBoots.getItemStack());
        customItemsRecipes.put("curse_boots", curseBoots.getRecipe());

        /////////////////////////////////////////////////////////////////////

        //BLINDNESS_POTION
        BlindnessPotion blindnessPotion = new BlindnessPotion();
        customItems.put("blindness_potion", blindnessPotion.getItemStack());

        //HEADBAND
        HeadBand headBand = new HeadBand();
        headBand.setRecipe();
        customItems.put("headband", headBand.getItemStack());
        customItemsRecipes.put("headband", headBand.getRecipe());

        //INVISIBLE_ITEM_FRAME
        InvisibleItemFrame invisibleItemFrame = new InvisibleItemFrame();
        invisibleItemFrame.setRecipe();
        customItems.put("invisible_item_frame", invisibleItemFrame.getItemStack());
        customItemsRecipes.put("invisible_item_frame", invisibleItemFrame.getRecipe());

        //MAGIC_LANTERN
        MagicLantern magicLantern = new MagicLantern();
        magicLantern.setRecipe();
        customItems.put("magic_lantern", magicLantern.getItemStack());
        customItemsRecipes.put("magic_lantern", magicLantern.getRecipe());

        //HEAL_STONE
        HealStone healStone = new HealStone();
        healStone.setRecipe();
        customItems.put("heal_stone", healStone.getItemStack());
        customItemsRecipes.put("heal_stone", healStone.getRecipe());

        /////////////////////////////////////////////////////////////////////

    }

    public static ItemStack getCustomItem(String itemName) {
        return customItems.get(itemName);
    }

    public static Map<String, ItemStack> getCustomItems() {
        return customItems;
    }

    public static Recipe getRecipe(String itemName) {
        return customItemsRecipes.get(itemName);
    }
}