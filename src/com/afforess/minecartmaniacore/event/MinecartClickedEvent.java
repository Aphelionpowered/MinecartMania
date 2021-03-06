package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class MinecartClickedEvent extends MinecartManiaEvent implements MinecartEvent {
    boolean action = false;
    MinecartManiaMinecart minecart;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartClickedEvent(final MinecartManiaMinecart minecart) {
        /*
         * The MinecartmaniaEvent already know it's name super("MinecartClickedEvent");
         */
        super();
        this.minecart = minecart;
    }
    
    public boolean isActionTaken() {
        return action;
    }
    
    public void setActionTaken(final boolean Action) {
        action = Action;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
}
