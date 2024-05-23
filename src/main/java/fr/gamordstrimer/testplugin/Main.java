package fr.gamordstrimer.testplugin;

import fr.gamordstrimer.testplugin.commands.CustomItemsRecipes;
import fr.gamordstrimer.testplugin.commands.CustomItemsGive;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.handlers.ArmorHandler;
import fr.gamordstrimer.testplugin.handlers.ItemframeHandler;
import fr.gamordstrimer.testplugin.handlers.PlayerHandler;
import fr.gamordstrimer.testplugin.staff.Staff;
import fr.gamordstrimer.testplugin.staff.StaffHandler;
import fr.gamordstrimer.testplugin.staff.StaffMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {
    private CooldownManager cooldownManager;
    private List<UUID> staff;
    private Map<UUID, Location> freezedplayer;

    // ================================================
    //              onEnable() & onDisable()
    // ================================================

    @Override
    public void onEnable() {
        getLogger().info("TestPlugin has been enabled!"); // Message sent in the console at the start of the plugin
        setup();
    }

    @Override
    public void onDisable() {
        cooldownManager.saveCooldowns(); // Save cooldowns before shutting down the server
    }

    // ================================================

    private void setup() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Initialize the CooldownManager first
        cooldownManager = new CooldownManager(getDataFolder());

        // Initialize Variable
        staff = new ArrayList<>();
        freezedplayer = new HashMap<>();

        // Registering elements
        CustomItems.init(this);
        registerCommands();
        registerEvents();
    }

    private void registerEvents() {
        new PlayerHandler(this, cooldownManager);
        new ArmorHandler(this); // Pass logger to ArmorHandler constructor
        new ItemframeHandler(this);
        new StaffHandler(this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("customitemsgive")).setExecutor(new CustomItemsGive(this));
        Objects.requireNonNull(getCommand("customitemsgive")).setTabCompleter(new CustomItemsGive(this));
        Objects.requireNonNull(getCommand("customitemsrecipes")).setExecutor(new CustomItemsRecipes(this, getLogger()));

        // Staff commands
        Objects.requireNonNull(getCommand("resetcooldown")).setExecutor(new Staff(this, cooldownManager));
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new Staff(this, cooldownManager));
        Objects.requireNonNull(getCommand("staff")).setExecutor(new StaffMode(this));
    }

    public List<UUID> getStaff() {
        return staff;
    }

    public boolean isInStaff(Player player) {
        return getStaff().contains(player.getUniqueId());
    }

    public Map<UUID, Location> getFreezedplayer() {
        return freezedplayer;
    }

    public boolean isFreeze(Player player) {
        return getFreezedplayer().containsKey(player.getUniqueId());
    }
}
