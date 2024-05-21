package fr.gamordstrimer.testplugin.customitems;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class CustomItems {
    private static Plugin plugin;
    private static final Map<String, ItemStack> customItems = new LinkedHashMap<>();
    private static final Map<String, Recipe> customItemRecipes = new LinkedHashMap<>();
    private static final Map<String, ItemStack> customItemsForCraft = new LinkedHashMap<>();

    public static void init(Plugin main) {
        plugin = main;
        //curse set
        CurseSword();
        CurseHelmet();
        CurseChestplate();
        CurseLeggings();
        CurseBoots();
        //Normal Item
        BlindnessPotion();
        HeadBand();
        InvisibleItemFrame();
    }

    //Curse set of item
    private static void CurseSword() {
        // Creating a properly configured Potion ItemStack for Instant Damage II
        ItemStack potionItem = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, true)); // Level 2 Potion
            potionItem.setItemMeta(potionMeta);
            customItemsForCraft.put("instant_damage_ii_potion", potionItem);
        }

        // Creating the Cursed Sword
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName("§4ᛍᚢᚱᛌᛂᛌᚥᚮᚱᛑ");
            itemMeta.setLore(Arrays.asList(ChatColor.of("#E96767") + "≈ Épée Maudite", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 15.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            item.setItemMeta(itemMeta);
        }

        // Creating the Craft for the Cursed Sword
        ShapedRecipe cs = new ShapedRecipe(new NamespacedKey(plugin, "cursesword"), item);
        cs.shape(
                " N ",
                " H ",
                " S "
        );
        cs.setIngredient('N', Material.NETHERITE_INGOT);
        cs.setIngredient('H', new RecipeChoice.ExactChoice(customItemsForCraft.get("instant_damage_ii_potion")));
        cs.setIngredient('S', Material.STICK);

        // Adding the CurseSword recipe to the server and the CurseSword Item inside the Hashmap
        Bukkit.getServer().addRecipe(cs);
        customItems.put("curse_sword", item);
        customItemRecipes.put("curse_sword", cs);
    }

    private static void CurseHelmet() {
        ItemStack item = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.of("#D00000") + "ᛍᚢᚱᛌᛂ ᚼᛂᛚᛘᛂᛐ");
            itemMeta.setLore(Arrays.asList(ChatColor.of("#E96767") + "≈ Casque Maudit", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            item.setItemMeta(itemMeta);
        }

        ShapedRecipe ch = new ShapedRecipe(new NamespacedKey(plugin, "cursehelmet"), item);
        ch.shape(
                "DND",
                "DHD",
                "   "
        );
        ch.setIngredient('D', Material.DIAMOND);
        ch.setIngredient('N', Material.NETHERITE_INGOT);
        ch.setIngredient('H', new RecipeChoice.ExactChoice(customItemsForCraft.get("instant_damage_ii_potion")));

        Bukkit.getServer().addRecipe(ch);
        customItems.put("curse_helmet", item);
        customItemRecipes.put("curse_helmet", ch);
    }

    private static void CurseChestplate() {
        ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.of("#D00000") + "ᚿᛂᛐᚼᛂᚱᛁᛐᛂ ᛍᚼᛂᛌᛐᛔᛚᛆᛐᛂ");
            itemMeta.setLore(Arrays.asList(ChatColor.of("#E96767") + "≈ Plastron Maudit", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            item.setItemMeta(itemMeta);
        }

        ShapedRecipe cc = new ShapedRecipe(new NamespacedKey(plugin, "cursechestplate"), item);
        cc.shape(
                "D D",
                "DHD",
                "DND"
        );
        cc.setIngredient('D', Material.DIAMOND);
        cc.setIngredient('H', new RecipeChoice.ExactChoice(customItemsForCraft.get("instant_damage_ii_potion")));
        cc.setIngredient('N', Material.NETHERITE_INGOT);

        Bukkit.getServer().addRecipe(cc);
        customItems.put("curse_chestplate", item);
        customItemRecipes.put("curse_chestplate", cc);
    }

    private static void CurseLeggings() {
        ItemStack item = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.of("#D00000") + "ᚿᛂÞᛂᚱᛁᛐᛂ ᛚᛂᚵᚵᛁᛜᛍ");
            itemMeta.setLore(Arrays.asList(ChatColor.of("#E96767") + "≈ Jambières Maudites", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            item.setItemMeta(itemMeta);
        }

        ShapedRecipe cl = new ShapedRecipe(new NamespacedKey(plugin, "curseleggings"), item);
        cl.shape(
                "DND",
                "DHD",
                "D D"
        );
        cl.setIngredient('D', Material.DIAMOND);
        cl.setIngredient('N', Material.NETHERITE_INGOT);
        cl.setIngredient('H', new RecipeChoice.ExactChoice(customItemsForCraft.get("instant_damage_ii_potion")));

        Bukkit.getServer().addRecipe(cl);
        customItems.put("curse_leggings", item);
        customItemRecipes.put("curse_leggings", cl);
    }

    private static void CurseBoots() {
        ItemStack item = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.of("#D00000") + "ᚿᛂÞᛂᚱᛁᛐᛂ ᛒᚮᚮᛐᛍ");
            itemMeta.setLore(Arrays.asList(ChatColor.of("#E96767") + "≈ Bottes Maudites", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            item.setItemMeta(itemMeta);
        }

        ShapedRecipe cb = new ShapedRecipe(new NamespacedKey(plugin, "curseboots"), item);
        cb.shape(
                "DHD",
                "DND",
                "   "
        );
        cb.setIngredient('D', Material.DIAMOND);
        cb.setIngredient('N', Material.NETHERITE_INGOT);
        cb.setIngredient('H', new RecipeChoice.ExactChoice(customItemsForCraft.get("instant_damage_ii_potion")));

        Bukkit.getServer().addRecipe(cb);
        customItems.put("curse_boots", item);
        customItemRecipes.put("curse_boots", cb);
    }

    //Normal Item
    private static void BlindnessPotion() {
        ItemStack potionItem = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setColor(Color.BLACK);
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3600, 0), true);
            potionMeta.setDisplayName(ChatColor.of("#FFFFFF") + "Potion de Cécité");
            potionMeta.setLore(Arrays.asList("§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            potionItem.setItemMeta(potionMeta);
            customItemsForCraft.put("blindness_potion", potionItem);
        }
        customItems.put("blindness_potion", potionItem);
    }

    private static void HeadBand() {

        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.of("#8800ED") + "Bandeau");
            itemMeta.setLore(Arrays.asList("§7Cette Item permet de rendre", "§7aveugle un joueur.", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
            leatherArmorMeta.setColor(Color.fromRGB(0, 0, 0));
            leatherArmorMeta.addItemFlags(ItemFlag.HIDE_DYE);
            item.setItemMeta(leatherArmorMeta);
        }

        ShapedRecipe hb = new ShapedRecipe(new NamespacedKey(plugin, "headband"), item);
        hb.shape(
                "LLL",
                "LBL",
                "   "
        );
        hb.setIngredient('L', Material.LEATHER);
        hb.setIngredient('B', new RecipeChoice.ExactChoice(customItemsForCraft.get("blindness_potion")));

        Bukkit.getServer().addRecipe(hb);
        customItems.put("headband", item);
        customItemRecipes.put("headband", hb);
    }

    private static void InvisibleItemFrame() {
        ItemStack item = new ItemStack(Material.ITEM_FRAME);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.AQUA + "Cadre Invisible");
            itemMeta.setLore(Arrays.asList("§8Ce Cadre est Invisible lorsque vous le placez.", "§7", ChatColor.of("#FF5D00") + "§oSendaria"));
            item.setItemMeta(itemMeta);
        }

        ShapelessRecipe iif = new ShapelessRecipe(new NamespacedKey(plugin, "invisibleitemframe"), item);
        iif.addIngredient(1, Material.ITEM_FRAME);
        iif.addIngredient(1, Material.GLASS_PANE);
        Bukkit.getServer().addRecipe(iif);
        customItems.put("invisible_item_frame", item);
        customItemRecipes.put("invisible_item_frame", iif);
    }

    public static ItemStack getItem(String itemName) {
        return customItems.get(itemName); // Retrieve item from the map
    }

    public static Recipe getRecipe(String itemName) {
        return customItemRecipes.get(itemName);
    }

    // Method to log the contents of customItemRecipes map
    public static void logCustomItemRecipes() {
        for (Map.Entry<String, Recipe> entry : customItemRecipes.entrySet()) {
            String itemName = entry.getKey();
            Recipe recipe = entry.getValue();
        }
    }

    public static Map<String, ItemStack> getCustomItems() {
        return customItems; // Retrieve the entire custom items map
    }

    public static Map<String, Recipe> getCustomRecipes() {
        return customItemRecipes; // Retrieve the entire custom recipes map
    }

    public static Map<String, ItemStack> getCustomItemsForCraft() {
        return customItemsForCraft;
    }

}
