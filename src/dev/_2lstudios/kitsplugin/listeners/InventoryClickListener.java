package dev._2lstudios.kitsplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev._2lstudios.kitsplugin.kits.KitManager;

public class InventoryClickListener implements Listener {
    private KitManager kitManager;

    public InventoryClickListener(final KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (kitManager.isKitInventory(event.getView().getTopInventory())) {
            event.setCancelled(true);
        }
    }
}