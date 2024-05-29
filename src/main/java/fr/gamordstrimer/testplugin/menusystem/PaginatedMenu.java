package fr.gamordstrimer.testplugin.menusystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PaginatedMenu extends Menu{

    protected int page = 0;

    //28 empty slot per page
    protected int maxItemsPerPage = 28;

    protected int index = 0;

    protected ItemStack right;
    protected ItemStack left;
    protected ItemStack close;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder() {

        //Button Left
        ItemStack buttonLeft = new ItemStack(Material.SPRUCE_BUTTON);
        ItemMeta buttonLeftMeta = buttonLeft.getItemMeta();
        if (buttonLeftMeta != null) {
            buttonLeftMeta.displayName(Component.text("Page Précédente").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonLeft.setItemMeta(buttonLeftMeta);
        }
        inventory.setItem(48, buttonLeft);
        left = buttonLeft;

        ItemStack buttonClose = new ItemStack(Material.BARRIER);
        ItemMeta buttonCloseMeta = buttonClose.getItemMeta();
        if (buttonCloseMeta != null) {
            buttonCloseMeta.displayName(Component.text("Fermer le Menu").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonClose.setItemMeta(buttonCloseMeta);
        }
        inventory.setItem(49, buttonClose);
        close = buttonClose;

        //Button Right
        ItemStack buttonRight = new ItemStack(Material.SPRUCE_BUTTON);
        ItemMeta buttonRightMeta = buttonRight.getItemMeta();
        if (buttonRightMeta != null) {
            buttonRightMeta.displayName(Component.text("Page Suivante").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            buttonRight.setItemMeta(buttonRightMeta);
        }
        inventory.setItem(50, buttonRight);
        right = buttonRight;

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }


    }

}
