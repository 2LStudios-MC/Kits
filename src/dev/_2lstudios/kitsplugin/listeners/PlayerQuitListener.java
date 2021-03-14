package dev._2lstudios.kitsplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.kitsplugin.kits.KitPlayerManager;

public class PlayerQuitListener implements Listener {
    private final KitPlayerManager kitPlayerManager;

    public PlayerQuitListener(final KitPlayerManager kitPlayerManager) {
        this.kitPlayerManager = kitPlayerManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        kitPlayerManager.save(kitPlayerManager.removePlayer(event.getPlayer()));
    }
}
