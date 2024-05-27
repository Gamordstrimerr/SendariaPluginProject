package fr.gamordstrimer.testplugin.staff;

import org.bukkit.entity.Player;

public class DefineTarget {

    private Player target;

    public DefineTarget(Player target) {
        this.target = target;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }
}
