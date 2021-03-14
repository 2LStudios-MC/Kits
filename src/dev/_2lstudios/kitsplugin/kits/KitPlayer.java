package dev._2lstudios.kitsplugin.kits;

import java.util.HashMap;
import java.util.Map;

public class KitPlayer {
    private final String name;
    private final Map<String, Long> cooldowns = new HashMap<>();

    public KitPlayer(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasCooldown(final String id, final long cooldown) {
        return System.currentTimeMillis() - cooldowns.getOrDefault(id, 0L) < cooldown;
    }

    public void setCooldown(final String id, final long cooldown) {
        this.cooldowns.put(id, cooldown);
    }

    public void setCooldown(final String id) {
        setCooldown(id, System.currentTimeMillis());
    }

    public long getCooldown(final String id) {
        return this.cooldowns.getOrDefault(id, 0L);
    }

    public Map<String, Long> getCooldowns() {
        return cooldowns;
    }
}
