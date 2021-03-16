package dev._2lstudios.kitsplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.events.InventoryAPIClickEvent;
import dev._2lstudios.inventoryapi.inventory.InventoryUtil;
import dev._2lstudios.inventoryapi.inventory.InventoryWrapper;
import dev._2lstudios.kitsplugin.kits.Kit;
import dev._2lstudios.kitsplugin.kits.KitManager;
import dev._2lstudios.kitsplugin.kits.KitPlayer;
import dev._2lstudios.kitsplugin.kits.KitPlayerManager;

public class InventoryAPIClickListener implements Listener {
    private final InventoryUtil inventoryUtil;
    private final KitPlayerManager kitPlayerManager;
    private final KitManager kitManager;

    public InventoryAPIClickListener(final KitPlayerManager kitPlayerManager, final KitManager kitManager) {
        this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();
        this.kitPlayerManager = kitPlayerManager;
        this.kitManager = kitManager;
    }

    // This checks for buttons
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInventoryAPIClickLowest(final InventoryAPIClickEvent event) {
        final InventoryClickEvent event1 = event.getEvent();
        final ItemStack item = event1.getCurrentItem();

        if (item != null) {
            final InventoryWrapper inventoryWrapper = event.getInventoryWrapper();
            final int page = inventoryWrapper.getPage();
            final Player player = event.getPlayer();
            final KitPlayer kitPlayer = kitPlayerManager.getPlayer(player);

            if (item.isSimilar(inventoryUtil.getNextItem(page))) {
                kitManager.openInventory(player, kitPlayer, page + 1);
                event.setCancelled(true);
            } else if (item.isSimilar(inventoryUtil.getBackItem(page))) {
                kitManager.openInventory(player, kitPlayer, page - 1);
                event.setCancelled(true);
            } else if (item.isSimilar(inventoryUtil.getCloseItem())) {
                event.setCancelled(true);
                player.closeInventory();
            }
        }
    }

    // This checks for kits
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryAPIClickHighest(final InventoryAPIClickEvent event) {
        final InventoryClickEvent event1 = event.getEvent();
        final ItemStack item = event1.getCurrentItem();

        if (item != null) {
            final ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta != null && itemMeta.hasDisplayName()) {
                final String name = ChatColor.stripColor(itemMeta.getDisplayName());
                final Player player = event.getPlayer();
                final Kit kit = kitManager.getKit(name);

                if (kit != null) {
                    kit.give(player);
                }
            }
        }
    }
}