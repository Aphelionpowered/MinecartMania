package com.afforess.minecartmaniacore.event;

import org.bukkit.event.HandlerList;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartMeetsConditionEvent extends MinecartManiaEvent {
    private final MinecartManiaMinecart minecart;
    private final Sign sign;
    private boolean condition = false;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public MinecartMeetsConditionEvent(final MinecartManiaMinecart minecart, final Sign sign) {
        /*
         * The MinecartmaniaEvent already know it's name super("MinecartMeetsConditionEvent");
         */
        super();
        this.minecart = minecart;
        this.sign = sign;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public Sign getSign() {
        return sign;
    }
    
    public boolean isMeetCondition() {
        return condition;
    }
    
    public void setMeetCondition(final boolean condition) {
        this.condition = condition;
    }
    
}
