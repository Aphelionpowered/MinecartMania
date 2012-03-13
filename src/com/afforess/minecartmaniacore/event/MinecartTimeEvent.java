package com.afforess.minecartmaniacore.event;

import java.util.Calendar;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartTimeEvent extends MinecartManiaEvent {
    private final MinecartManiaMinecart minecart;
    private final Calendar oldCalendar;
    private final Calendar currentCalendar;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartTimeEvent(final MinecartManiaMinecart cart, final Calendar oldCal, final Calendar newCal) {
        /*
         * The MinecartmaniaEvent already know it's name super("MinecartTimeEvent");
         */
        super();
        minecart = cart;
        oldCalendar = oldCal;
        currentCalendar = newCal;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public Calendar getOldCalendar() {
        return oldCalendar;
    }
    
    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }
}
