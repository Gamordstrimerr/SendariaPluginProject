package fr.gamordstrimer.testplugin;

import fr.gamordstrimer.testplugin.commands.CustomItemsRecipes;
import fr.gamordstrimer.testplugin.commands.CustomItemsGive;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.handlers.ArmorHandler;
import fr.gamordstrimer.testplugin.handlers.ItemframeHandler;
import fr.gamordstrimer.testplugin.handlers.PlayerHandler;
import fr.gamordstrimer.testplugin.staff.Staff;
import org.bukkit.Location;
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
        getLogger().info("TestPlugin has been enabled!"); //message send in the console at the start of the plugin
        setup();
    }

    @Override
    public void onDisable() {
        cooldownManager.saveCooldowns(); //saving cooldowns before shutting down the server
    }

    // ================================================

    private void setup() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //registering elements
        CustomItems.init(this);
        registerCommands();
        registerEvents();

        // Initialize the CooldownManager first
        cooldownManager = new CooldownManager(getDataFolder());

        //Initialize Variable
        staff = new ArrayList<>();
        freezedplayer = new HashMap<>();
    }

    private void registerEvents() {
        new PlayerHandler(this, cooldownManager);
        new ArmorHandler(this); // Pass logger to ArmorHandler constructor
        new ItemframeHandler(this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("customitemsgive")).setExecutor(new CustomItemsGive(this));
        Objects.requireNonNull(getCommand("customitemsgive")).setTabCompleter(new CustomItemsGive(this));
        Objects.requireNonNull(getCommand("customitemsrecipes")).setExecutor(new CustomItemsRecipes(this, getLogger()));
        //Staff
        Objects.requireNonNull(getCommand("resetcooldown")).setExecutor(new Staff(this, cooldownManager));
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new Staff(this, cooldownManager));
    }
}
