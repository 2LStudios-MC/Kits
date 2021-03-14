package dev._2lstudios.kitsplugin.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitGiveEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
