package fr.gamordstrimer.testplugin.commands;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.heads.SkullTextureChanger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Logger;

public class CustomItemsRecipes implements CommandExecutor, Listener {

    private Logger logger;
    private static Inventory mainGUI;
    private static final Map<String, Inventory> itemGUIs = new HashMap<>();
    private static ItemStack BackItem;
    private static ItemStack CraftingTable;

    public CustomItemsRecipes(Main plugin, Logger logger) {
        this.logger = logger;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setupItemGUI();
        // Initialize the main GUI inventory
        initMainGUI();
    }

    private void setupItemGUI() {

        //headback in menu (go back)
        ItemStack headback = new ItemStack(Material.PLAYER_HEAD);
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZkOGVmZjRjNjczZTA2MzY5MDdlYTVjMGI1ZmY0ZjY0ZGMzNWM2YWFkOWI3OTdmMWRmNjYzMzUxYjRjMDgxNCJ9fX0=";
        SkullTextureChanger.setSkullTexture(headback, base64Texture);
        SkullMeta headbackmeta = (SkullMeta) headback.getItemMeta();
        headbackmeta.displayName(Component.text("Retour").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
        headback.setItemMeta(headbackmeta);
        BackItem = headback;

        // crafting table
        ItemStack craftingtable = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta craftingtablemeta = craftingtable.getItemMeta();
        craftingtablemeta.displayName(Component.text("Table de Craft").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
        lore.add(Component.text("Cette Item peut être").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("fabriqué dans une").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("Table de Craft").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
        craftingtablemeta.lore(lore);
        craftingtable.setItemMeta(craftingtablemeta);
        CraftingTable = craftingtable;
    }

    private void initMainGUI() {
        // Retrieve all custom items
        Map<String, ItemStack> customItems = CustomItems.getCustomItems();
        // Calculate the required inventory size
        int inventorySize = (int) Math.ceil((double) customItems.size() / 9) * 9;
        // Create the inventory with the calculated size
        mainGUI = Bukkit.createInventory(null, inventorySize, "§e§lItem Custom de Sendaria");
        // Add items to the inventory
        customItems.forEach((itemName, itemStack) -> {
            mainGUI.addItem(itemStack);
            // Initialize individual item GUI
            ItemGUI(itemName, itemStack);
        });
    }

    private void ItemGUI(String itemName, ItemStack itemStack) {
        String displayName = CustomItems.getItem(itemName).getItemMeta().getDisplayName();
        // Create a new inventory for the item's crafting recipe
        Inventory itemGUI = Bukkit.createInventory(null, 45, displayName);
        // Retrieve the crafting recipe for the item
        Recipe recipe = CustomItems.getRecipe(itemName);

        if (recipe != null) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
                String[] shape = shapedRecipe.getShape();

                int index = 10;
                for (String s : shape) {
                    for (int col = 0; col < s.length(); col++) {
                        char ingredientChar = s.charAt(col);
                        if (ingredientChar != ' ') {
                            ItemStack ingredient = ingredientMap.get(ingredientChar);
                            if (ingredient != null) {
                                itemGUI.setItem(index++, ingredient);
                            }
                        } else {
                            ItemStack air = new ItemStack(Material.AIR);
                            itemGUI.setItem(index++, air);
                        }
                    }
                    index += 6; // Move to the next row in the GUI
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                int index = 10; // Starting position in a 3x3 grid
                for (ItemStack ingredient : shapelessRecipe.getIngredientList()) {
                    itemGUI.setItem(index++, ingredient);
                }
            }
        }

        itemGUI.setItem(23, CraftingTable);
        itemGUI.setItem(25, CustomItems.getItem(itemName));
        itemGUI.setItem(44, BackItem);

        // Fill empty slots with glass panes, except specific slots 10/11/12, 19/20/21, 28/29/30
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = glassPane.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(" "));
            glassPane.setItemMeta(meta);
        }

        // Set of slots to exclude
        Set<Integer> excludedSlots = new HashSet<>(Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30));
        for (int i = 0; i < 45; i++) {
            if (itemGUI.getItem(i) == null && !excludedSlots.contains(i)) {
                itemGUI.setItem(i, glassPane);
            }
        }

        // Store the item GUI in the map
        itemGUIs.put(itemName, itemGUI);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true; // Return true to indicate the command was handled
        }

        if (player.hasPermission("sendaria.cir") || player.hasPermission("*")) {
            player.openInventory(mainGUI);
            return true;
        }
        return false;
    }

    // EventHandler for the GUIs
    @EventHandler
    public void clickEvent(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if(clickedInventory != null && clickedInventory.equals(mainGUI)) {
            // Check if the player is interacting with an item that has custom meta
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                // Check if the clicked item is part of the customItems map by its ItemStack
                if (CustomItems.getCustomItems().containsValue(clickedItem)) {
                    for (Map.Entry<String, ItemStack> entry : CustomItems.getCustomItems().entrySet()) {
                        if (entry.getValue().equals(clickedItem)) {
                            String itemName = entry.getKey();
                            Inventory itemGUI = itemGUIs.get(itemName);
                            if (itemGUI != null) {
                                player.openInventory(itemGUI);
                            }
                            break; // Stop iterating once the item is found
                        }
                    }
                }
            }
        } else if (clickedInventory != null && itemGUIs.containsValue(clickedInventory)) {
            if (clickedItem != null && clickedItem.equals(BackItem)) {
                player.openInventory(mainGUI);
            }
            // Cancel the event to prevent any interaction with the custom GUI
            event.setCancelled(true);
        }
    }
}
