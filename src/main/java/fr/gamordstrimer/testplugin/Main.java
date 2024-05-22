package fr.gamordstrimer.testplugin;

import fr.gamordstrimer.testplugin.commands.CustomItemsRecipes;
import fr.gamordstrimer.testplugin.commands.CustomItemsGive;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.handlers.ArmorHandler;
import fr.gamordstrimer.testplugin.handlers.ItemframeHandler;
import fr.gamordstrimer.testplugin.handlers.PlayerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("TestPlugin has been enabled!");
        CustomItems.init(this);
        new PlayerHandler(this);
        new ArmorHandler(this); // Pass logger to ArmorHandler constructor
        new ItemframeHandler(this);

        //config parts
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //commands register Here
        getCommand("customitemsgive").setExecutor(new CustomItemsGive(this));
        getCommand("customitemsgive").setTabCompleter(new CustomItemsGive(this));
        getCommand("customitemsrecipes").setExecutor(new CustomItemsRecipes(this, getLogger()));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
