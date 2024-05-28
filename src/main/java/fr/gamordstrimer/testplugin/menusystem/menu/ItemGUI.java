package fr.gamordstrimer.testplugin.menusystem.menu;

import fr.gamordstrimer.testplugin.Main;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.heads.SkullTextureChanger;
import fr.gamordstrimer.testplugin.menusystem.Menu;
import fr.gamordstrimer.testplugin.menusystem.PlayerMenuUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemGUI extends Menu {

    private ItemStack headback;
    private ItemStack craftingTable;

    public ItemGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        initializeItems();
    }


    @Override
    public @NotNull TextComponent getMenuName() {
        String itemName = MainGUI.getItemName();
        Component displayNameComponent = CustomItems.getItem(itemName).getItemMeta().displayName();
        String displayName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(displayNameComponent));
        return Component.text(displayName).color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true);
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        if (clickedItem != null && clickedItem.isSimilar(headback)) {
            // Go back to the main GUI
            new MainGUI(Main.getPlayerMenuUtility(player)).open();
        }
    }

    @Override
    public void setMenuItems() {

        String itemName = MainGUI.getItemName();
        ItemStack itemStack = MainGUI.getItemStack();

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
                                inventory.setItem(index++, ingredient);
                            }
                        } else {
                            ItemStack air = new ItemStack(Material.AIR);
                            inventory.setItem(index++, air);
                        }
                    }
                    index += 6; // Move to the next row in the GUI
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                int index = 10; // Starting position in a 3x3 grid
                for (ItemStack ingredient : shapelessRecipe.getIngredientList()) {
                    inventory.setItem(index++, ingredient);
                }
            }
            inventory.setItem(23, craftingTable);
        } else {
            // Fill crafting slots with barriers
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            if (barrierMeta != null) {
                barrierMeta.displayName(Component.text("Pas de Recette Disponible").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
                barrier.setItemMeta(barrierMeta);
            }
            int[] craftingSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
            for (int slot : craftingSlots) {
                inventory.setItem(slot, barrier);
            }
        }
        inventory.setItem(25, itemStack);
        inventory.setItem(44, headback);

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
            if (inventory.getItem(i) == null && !excludedSlots.contains(i)) {
                inventory.setItem(i, glassPane);
            }
        }
    }

    private void initializeItems() {

        //headback in menu (go back)
        ItemStack headback = new ItemStack(Material.PLAYER_HEAD);
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZkOGVmZjRjNjczZTA2MzY5MDdlYTVjMGI1ZmY0ZjY0ZGMzNWM2YWFkOWI3OTdmMWRmNjYzMzUxYjRjMDgxNCJ9fX0=";
        SkullTextureChanger.setSkullTexture(headback, base64Texture);
        SkullMeta headbackmeta = (SkullMeta) headback.getItemMeta();
        if (headbackmeta != null) {
            headbackmeta.displayName(Component.text("Retour").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            headback.setItemMeta(headbackmeta);
        }
        this.headback = headback;

        // crafting table
        ItemStack craftingtable = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta craftingtablemeta = craftingtable.getItemMeta();
        if (craftingtablemeta != null) {
            craftingtablemeta.displayName(Component.text("Table de Craft").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
            lore.add(Component.text("Cette Item peut être").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("fabriqué dans une").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("Table de Craft").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.GRAY));
            craftingtablemeta.lore(lore);
            craftingtable.setItemMeta(craftingtablemeta);
        }
        this.craftingTable = craftingtable;

    }
}
