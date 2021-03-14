package dev._2lstudios.kitsplugin.kits;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import dev._2lstudios.kitsplugin.utils.ConfigUtil;

public class KitPlayerManager {
    private final ConfigUtil configUtil;
    private final Map<UUID, KitPlayer> players = new HashMap<>();

    public KitPlayerManager(final ConfigUtil configUtil) {
        this.configUtil = configUtil;
    }

    public Collection<KitPlayer> getPlayers() {
        return players.values();
    }

    public KitPlayer getPlayer(final UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    public KitPlayer getPlayer(final Player player) {
        return getPlayer(player.getUniqueId());
    }

    public KitPlayer addPlayer(final Player player) {
        final KitPlayer kitPlayer = new KitPlayer(configUtil, player.getName());

        players.put(player.getUniqueId(), kitPlayer);

        return kitPlayer;
    }

    public KitPlayer removePlayer(final Player player) {
        final KitPlayer kitPlayer = getPlayer(player);

        players.remove(player.getUniqueId());

        return kitPlayer;
    }
}
