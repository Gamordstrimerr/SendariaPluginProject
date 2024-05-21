package fr.gamordstrimer.testplugin;

import fr.gamordstrimer.testplugin.commands.CustomItemsRecipes;
import fr.gamordstrimer.testplugin.commands.CustomItemsGive;
import fr.gamordstrimer.testplugin.commands.GamemodeAliases;
import fr.gamordstrimer.testplugin.customitems.CustomItems;
import fr.gamordstrimer.testplugin.handlers.ArmorHandler;
import fr.gamordstrimer.testplugin.handlers.ItemframeHandler;
import fr.gamordstrimer.testplugin.handlers.PlayerHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("TestPlugin has been enabled!");
        CustomItems.init(this);
        CustomItems.logCustomItemRecipes();
        new PlayerHandler(this);
        new ArmorHandler(this); // Pass logger to ArmorHandler constructor
        new ItemframeHandler(this);
        Set<String> keys = CustomItems.getCustomItems().keySet();
        for (String key : keys) {
            getLogger().info(key);
        }

        //config parts
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //commands register Here
        getCommand("customitemsgive").setExecutor(new CustomItemsGive(this));
        getCommand("customitemsgive").setTabCompleter(new CustomItemsGive(this));
        getCommand("customitemsrecipes").setExecutor(new CustomItemsRecipes(this, getLogger()));
        getCommand("gamemode").setExecutor(new GamemodeAliases(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
