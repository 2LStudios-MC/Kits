package dev._2lstudios.kitsplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.InventoryManager;
import dev._2lstudios.inventoryapi.InventoryWrapper;
import dev._2lstudios.inventoryapi.events.InventoryAPICloseEvent;

public class InventoryAPICloseListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryAPIClose(final InventoryAPICloseEvent event) {
        final Inventory inventory = event.getEvent().getInventory();
        final InventoryManager inventoryManager = InventoryAPI.getInstance().getInventoryManager();
        final InventoryWrapper inventoryWrapper = event.getInventoryWrapper();
        final String id = inventoryWrapper.getId();

        if (id.equals("kitpreview") || id.equals("kits")) {
            inventoryManager.remove(inventory);
        }
    }
}