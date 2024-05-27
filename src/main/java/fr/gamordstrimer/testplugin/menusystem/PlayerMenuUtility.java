package fr.gamordstrimer.testplugin.menusystem;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    private Menu currentMenu;

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

}
