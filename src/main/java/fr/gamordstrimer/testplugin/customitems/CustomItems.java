package fr.gamordstrimer.testplugin.customitems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        MagicLantern();
    }

    //Curse set of item
    private static void CurseSword() {
        // Creating a properly configured Potion ItemStack for Instant Damage II
        ItemStack potionItem = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, true)); // false for extended, true for upgraded
            potionItem.setItemMeta(potionMeta);
        }
        customItemsForCraft.put("instant_damage_ii_potion", potionItem);

        // Creating the Cursed Sword
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text("ᛍᚢᚱᛌᛂᛌᚥᚮᚱᛑ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Épée Maudite").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
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
            itemMeta.displayName(Component.text("ᛍᚢᚱᛌᛂ ᚼᛂᛚᛘᛂᛐ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Casque Maudit").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
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
            itemMeta.displayName(Component.text("ᚿᛂᛐᚼᛂᚱᛁᛐᛂ ᛍᚼᛂᛌᛐᛔᛚᛆᛐᛂ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Plastron Maudit").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
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
            itemMeta.displayName(Component.text("ᚿᛂÞᛂᚱᛁᛐᛂ ᛚᛂᚵᚵᛁᛜᛍ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Jambières Maudites").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
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
            itemMeta.displayName(Component.text("ᚿᛂÞᛂᚱᛁᛐᛂ ᛒᚮᚮᛐᛍ").color(TextColor.fromHexString("#D00000")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("≈ Bottes Maudites").color(TextColor.fromHexString("#FF5D00")).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
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
            potionMeta.displayName(Component.text("Potion de Cécité").color(TextColor.fromHexString("#FFFFFF")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            potionMeta.lore(lore);
            potionItem.setItemMeta(potionMeta);
            customItemsForCraft.put("blindness_potion", potionItem);
        }
        customItems.put("blindness_potion", potionItem);
    }

    private static void HeadBand() {

        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text("Bandeau").color(TextColor.fromHexString("#8800ED")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Cette Item permet de rendre").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("aveugle un joueur.").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
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
            itemMeta.displayName(Component.text("Cadre Invisible").color(TextColor.fromHexString("#00c6ff")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Ce Cadre est Invisible lorsque vois le placez").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
            item.setItemMeta(itemMeta);
        }

        ShapelessRecipe iif = new ShapelessRecipe(new NamespacedKey(plugin, "invisibleitemframe"), item);
        iif.addIngredient(1, Material.ITEM_FRAME);
        iif.addIngredient(1, Material.GLASS_PANE);
        Bukkit.getServer().addRecipe(iif);
        customItems.put("invisible_item_frame", item);
        customItemRecipes.put("invisible_item_frame", iif);
    }

    private static void MagicLantern() {
        ItemStack item = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta != null) {
            itemMeta.displayName(Component.text("Lanterne Magique").color(TextColor.fromHexString("#A89CF0")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Cette Lanterne vous procure une vision").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("dans le noir en appuyant sur Clique droit").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            lore.add(Component.text("Sendaria").color(TextColor.fromHexString("#FF5D00")).decorate(TextDecoration.ITALIC));
            itemMeta.lore(lore);
            item.setItemMeta(itemMeta);
        }

        ShapedRecipe ml = new ShapedRecipe(new NamespacedKey(plugin, "magiclantern"), item);
        ml.shape(
                " S ",
                "GDG",
                " S "
        );
        ml.setIngredient('S', Material.SOUL_LANTERN);
        ml.setIngredient('D', Material.DIAMOND);
        ml.setIngredient('G', Material.GLASS);
        Bukkit.getServer().addRecipe(ml);
        customItems.put("magic_lantern", item);
        customItemRecipes.put("magic_lantern", ml);
    }

    public static ItemStack getItem(String itemName) {
        return customItems.get(itemName); // Retrieve item from the map
    }

    public static Recipe getRecipe(String itemName) {
        return customItemRecipes.get(itemName);
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
