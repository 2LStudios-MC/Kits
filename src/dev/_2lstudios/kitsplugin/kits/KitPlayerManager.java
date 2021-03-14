package dev._2lstudios.kitsplugin.kits;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
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
        final KitPlayer kitPlayer = new KitPlayer(player.getName());

        players.put(player.getUniqueId(), kitPlayer);

        return kitPlayer;
    }

    public KitPlayer removePlayer(final Player player) {
        final KitPlayer kitPlayer = getPlayer(player);

        players.remove(player.getUniqueId());

        return kitPlayer;
    }

    public void load(final KitPlayer kitPlayer) {
        final YamlConfiguration config = configUtil.get("%datafolder%/players/" + kitPlayer.getName() + ".yml");
        final ConfigurationSection cooldownSection = config.getConfigurationSection("cooldowns");

        if (cooldownSection != null) {
            for (final String id : cooldownSection.getKeys(false)) {
                final long cooldown = config.getLong("cooldowns." + id);

                kitPlayer.setCooldown(id, cooldown);
            }
        }
    }

    public void save(final KitPlayer kitPlayer) {
        final YamlConfiguration config = new YamlConfiguration();

        for (final Entry<String, Long> entry : kitPlayer.getCooldowns().entrySet()) {
            final String id = entry.getKey();
            final long cooldown = entry.getValue();

            if (kitPlayer.hasCooldown(id, cooldown)) {
                config.set("cooldowns." + id, cooldown);
            }
        }

        configUtil.save(config, "%datafolder%/players/" + kitPlayer.getName() + ".yml");
    }

    public void save() {
        for (final KitPlayer kitPlayer : getPlayers()) {
            save(kitPlayer);
        }
    }
}
