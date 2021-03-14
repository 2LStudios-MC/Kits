package dev._2lstudios.kitsplugin;

import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.kitsplugin.commands.KitCMD;
import dev._2lstudios.kitsplugin.commands.KitPreviewCMD;
import dev._2lstudios.kitsplugin.commands.KitSetupCMD;
import dev._2lstudios.kitsplugin.kits.KitManager;
import dev._2lstudios.kitsplugin.kits.KitPlayerManager;
import dev._2lstudios.kitsplugin.listeners.InventoryClickListener;
import dev._2lstudios.kitsplugin.listeners.PlayerJoinListener;
import dev._2lstudios.kitsplugin.listeners.PlayerQuitListener;
import dev._2lstudios.kitsplugin.utils.ConfigUtil;

public class KitsPlugin extends JavaPlugin {
    private KitManager kitManager = null;
    private KitPlayerManager kitPlayerManager = null;

    @Override
    public void onEnable() {
        final Server server = getServer();
        final PluginManager pluginManager = server.getPluginManager();
        final ConfigUtil configUtil = new ConfigUtil(this);

        kitManager = new KitManager(this, configUtil);
        kitPlayerManager = new KitPlayerManager(this, configUtil);

        for (final Player player : server.getOnlinePlayers()) {
            kitPlayerManager.addPlayer(player);
        }

        pluginManager.registerEvents(new InventoryClickListener(kitManager), this);
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

        for (final Inventory inventory : kitManager.getKitInventories()) {
            for (final HumanEntity humanEntity : inventory.getViewers()) {
                humanEntity.closeInventory();
            }
        }
    }
}