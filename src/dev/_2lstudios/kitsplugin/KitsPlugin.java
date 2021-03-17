package dev._2lstudios.kitsplugin;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.kitsplugin.commands.KitCMD;
import dev._2lstudios.kitsplugin.commands.KitPreviewCMD;
import dev._2lstudios.kitsplugin.commands.KitSetupCMD;
import dev._2lstudios.kitsplugin.kits.KitManager;
import dev._2lstudios.kitsplugin.kits.KitPlayerManager;
import dev._2lstudios.kitsplugin.listeners.InventoryAPIClickListener;
import dev._2lstudios.kitsplugin.listeners.PlayerJoinListener;
import dev._2lstudios.kitsplugin.listeners.PlayerQuitListener;
import dev._2lstudios.kitsplugin.utils.ConfigUtil;
import net.milkbowl.vault.economy.Economy;

public class KitsPlugin extends JavaPlugin {
    private static KitsPlugin instance;
    private Economy economy = null;
    private KitManager kitManager = null;
    private KitPlayerManager kitPlayerManager = null;

    public static void setInstance(final KitsPlugin instance) {
        KitsPlugin.instance = instance;
    }

    public static KitsPlugin getInstance() {
        return instance;
    }

	public Economy getEconomy() {
		return economy;
	}

    public KitManager getKitManager() {
        return kitManager;
    }

    public KitPlayerManager getKitPlayerManager() {
        return kitPlayerManager;
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return;
        }

        economy = rsp.getProvider();
    }

    @Override
    public void onEnable() {
        setupEconomy();
        setInstance(this);

        final Server server = getServer();
        final PluginManager pluginManager = server.getPluginManager();
        final ConfigUtil configUtil = new ConfigUtil(this);

        kitManager = new KitManager(this, configUtil);
        kitPlayerManager = new KitPlayerManager(this, configUtil);

        for (final Player player : server.getOnlinePlayers()) {
            kitPlayerManager.addPlayer(player);
        }

        pluginManager.registerEvents(new InventoryAPIClickListener(kitPlayerManager, kitManager), this);
        pluginManager.registerEvents(new PlayerJoinListener(kitPlayerManager), this);
        pluginManager.registerEvents(new PlayerQuitListener(kitPlayerManager), this);

        getCommand("kit").setExecutor(new KitCMD(kitManager, kitPlayerManager));
        getCommand("kitsetup").setExecutor(new KitSetupCMD(kitManager));
        getCommand("kitpreview").setExecutor(new KitPreviewCMD(kitManager));
    }

    @Override
    public void onDisable() {
        kitPlayerManager.save();
        kitManager.save();
    }
}