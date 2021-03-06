package com.afforess.minecartmaniacore.signs;

import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class LaunchMinecartAction implements SignAction {
    private volatile Vector launchSpeed = null;
    private volatile boolean previous = false;
    protected Sign sign;
    
    public LaunchMinecartAction(final Sign sign) {
        this.sign = sign;
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (ControlBlockList.getLaunchSpeed(minecart.getSpecificMaterialBeneath()) == 1.0D)
            return false;
        if (minecart.isMoving())
            return false;
        final Vector launch = calculateLaunchSpeed(false);
        if (previous) {
            if (minecart.getPreviousDirectionOfMotion() != CompassDirection.NO_DIRECTION) {
                minecart.setMotion(minecart.getPreviousDirectionOfMotion(), 0.6D);
            }
        } else {
            minecart.minecart.setVelocity(launch);
        }
        return true;
    }
    
    private Vector calculateLaunchSpeed(final boolean force) {
        if ((launchSpeed == null) || force) {
            previous = false;
            CompassDirection dir = CompassDirection.NO_DIRECTION;
            for (int i = 0; i < sign.getNumLines(); i++) {
                String line = StringUtils.removeBrackets(sign.getLine(i).trim()).toLowerCase();
                if (line.contains("previous dir")) {
                    previous = true;
                    break;
                }
                if (line.contains("launch")) {
                    if (line.contains("player")) {
                        // TODO: Handle "launch player"
                    } else {
                        try {
                            dir = CompassDirection.valueOf(line.substring(8).toUpperCase());
                        } catch (final IllegalArgumentException e) {
                            final String fs = String.format("Unknown sign launch direction: \"%s\" in: %s at x:%d y:%d z:%d", line.substring(8), sign.getBlock().getWorld().getName().toString(), sign.getX(), sign.getY(), sign.getZ());
                            MinecartManiaLogger.getInstance().log(fs, true);
                            dir = CompassDirection.NO_DIRECTION;
                        }
                        if (dir != CompassDirection.NO_DIRECTION) {
                            break;
                        }
                    }
                }
            }
            if ((dir != CompassDirection.NO_DIRECTION) || previous) {
                sign.addBrackets();
                launchSpeed = dir.toVector(0.6);
            }
        }
        return launchSpeed;
    }
    
    public boolean async() {
        return true;
    }
    
    public boolean valid(final Sign sign) {
        calculateLaunchSpeed(true);
        return (launchSpeed != null) || previous;
    }
    
    public String getName() {
        return "launchersign";
    }
    
    public String getFriendlyName() {
        return "Launcher Sign";
    }
    
}
