package dev._2lstudios.kitsplugin.kits;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import dev._2lstudios.kitsplugin.utils.ConfigUtil;

public class KitPlayer {
    private final ConfigUtil configurationUtil;
    private final String name;
    private final Map<String, Long> cooldowns = new HashMap<>();

    public KitPlayer(final ConfigUtil configurationUtil, final String name) {
        this.configurationUtil = configurationUtil;
        this.name = name;
    }

    public boolean hasCooldown(final String id, final long cooldown) {
        return System.currentTimeMillis() - cooldowns.getOrDefault(id, 0L) < cooldown;
    }

    public void setCooldown(final String id) {
        this.cooldowns.put(id, System.currentTimeMillis());
    }

    public long getCooldown(final String id) {
        return this.cooldowns.getOrDefault(id, 0L);
    }

    public void load() {
        final YamlConfiguration config = configurationUtil.get("%datafolder%/players/" + name + ".yml");
        final ConfigurationSection cooldownSection = config.getConfigurationSection("cooldowns");

        if (cooldownSection != null) {
            for (final String id : cooldownSection.getKeys(false)) {
                final long cooldown = config.getLong("cooldowns." + id);

                cooldowns.put(id, cooldown);
            }
        }
    }

    public void save() {
        final YamlConfiguration config = new YamlConfiguration();

        for (final Entry<String, Long> entry : cooldowns.entrySet()) {
            final String id = entry.getKey();
            final long cooldown = entry.getValue();

            if (hasCooldown(id, cooldown)) {
                config.set("cooldowns." + id, cooldown);
            }
        }
    }
}
