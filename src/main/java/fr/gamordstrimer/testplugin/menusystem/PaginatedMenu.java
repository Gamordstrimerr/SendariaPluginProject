package fr.gamordstrimer.testplugin.menusystem;

import fr.gamordstrimer.testplugin.Utils.SkullTextureChanger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginatedMenu extends Menu{

    protected int page = 0;

    //28 empty slot per page
    protected int maxItemsPerPage = 28;

    protected int index = 0;

    protected ItemStack right;
    protected ItemStack left;
    protected ItemStack close;
    protected ItemStack headRandom;

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

        //Button Close
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

        //Random Dice (headRandom)
        ItemStack diceRandom = new ItemStack(Material.PLAYER_HEAD);
        String base64Texture2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUyMmMyOThlN2M2MzM2YWYxNzkwOWFjMWYxZWU2ODM0YjU4YjFhM2NjOTlhYmEyNTVjYTdlYWViNDc2MTczIn19fQ==";
        SkullTextureChanger.setSkullTexture(diceRandom, base64Texture2);
        SkullMeta diceRandomMeta = (SkullMeta) diceRandom.getItemMeta();
        if (diceRandomMeta != null) {
            diceRandomMeta.displayName(Component.text("Téléportation Aléatoire").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(" "));
            lore.add(Component.text("\uD83C\uDFB2 Clique pour te Téléporter").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("   aléatoirement à un joueur").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(" "));
            diceRandomMeta.lore(lore);
            diceRandom.setItemMeta(diceRandomMeta);
        }
        headRandom = diceRandom;

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
