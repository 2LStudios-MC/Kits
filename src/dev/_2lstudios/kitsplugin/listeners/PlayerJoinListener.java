package dev._2lstudios.kitsplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.kitsplugin.kits.KitPlayerManager;

public class PlayerJoinListener implements Listener {
    private final KitPlayerManager kitPlayerManager;

    public PlayerJoinListener(final KitPlayerManager kitPlayerManager) {
        this.kitPlayerManager = kitPlayerManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        kitPlayerManager.addPlayer(event.getPlayer()).load();
    }
}
