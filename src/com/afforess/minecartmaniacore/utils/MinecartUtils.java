package com.afforess.minecartmaniacore.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Wolf;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.config.MinecartManiaConfiguration;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class MinecartUtils {
    private static final CompassDirection[] validDirections = new CompassDirection[] { CompassDirection.NORTH, CompassDirection.EAST, CompassDirection.SOUTH, CompassDirection.WEST };
    
    public static boolean validDirection(final CompassDirection dir) {
        for (final CompassDirection td : getValiddirections()) {
            if (td.equals(dir))
                return true;
        }
        return false;
    }
    
    public static boolean isTrack(final Block block) {
        return isTrack(block.getTypeId());
    }
    
    public static boolean isTrack(final Material item) {
        return isTrack(item.getId());
    }
    
    public static boolean isTrack(final int id) {
        return (id == Material.RAILS.getId()) || (id == Material.POWERED_RAIL.getId()) || (id == Material.DETECTOR_RAIL.getId());
    }
    
    public static boolean isTrack(final Location location) {
        return isTrack(location.getBlock().getTypeId());
    }
    
    public static boolean validMinecartTrackAnyDirection(final World w, final int x, final int y, final int z, final int range) {
        return validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.NORTH) || validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.EAST) || validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.SOUTH) || validMinecartTrack(w, x, y, z, range, DirectionUtils.CompassDirection.WEST);
    }
    
    public static boolean isSlopedTrack(final Block rail) {
        return isSlopedTrack(rail.getWorld(), rail.getX(), rail.getY(), rail.getZ());
    }
    
    public static boolean isSlopedTrack(final World w, final int x, final int y, final int z) {
        final int data = MinecartManiaWorld.getBlockData(w, x, y, z);
        return (data >= 0x2) && (data <= 0x5);
    }
    
    public static boolean isCurvedTrack(final Block rail) {
        final int data = rail.getData();
        return (data > 5) && (data < 10);
    }
    
    public static boolean validMinecartTrack(final Location loc, final int range, final CompassDirection direction) {
        return validMinecartTrack(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range, direction);
    }
    
    //TODO this method is not a perfect detection of track. It will give false positives for having 2 sets of parallel track, and when double curves are used
    public static boolean validMinecartTrack(final World w, int x, int y, int z, int range, CompassDirection direction) {
        if (!isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z))) {
            y--;
            if (!isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z)))
                return false;
        }
        range--;
        while (range > 0) {
            final BlockVector bv = direction.toVector(range).toBlockVector();
            x += bv.getBlockX();
            z += bv.getBlockZ();
            
            if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y - 1, z))) {
                y--;
            } else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y + 1, z))) {
                y++;
            }
            if (!isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z)))
                return false;
            
            for (final CompassDirection dir : CompassDirection.getCardinalDirections()) {
                final Location loc = new Location(w, x, y, z);
                loc.add(dir.toVector(1));
                if (isTrack(loc)) {
                    direction = dir;
                }
            }
            // Old
            //            if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x - 1, y, z))) {
            //                direction = CompassDirection.NORTH;
            //            } else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z - 1))) {
            //                direction = CompassDirection.EAST;
            //            } else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x + 1, y, z))) {
            //                direction = CompassDirection.SOUTH;
            //            } else if (isTrack(MinecartManiaWorld.getBlockIdAt(w, x, y, z + 1))) {
            //                direction = CompassDirection.WEST;
            //            }
            range--;
        }
        return true;
    }
    
    // (Etsija) Directionality fix
    public static boolean isAtIntersection(final World w, final int x, final int y, final int z) {
        int paths = 0;
        
        final int data = MinecartManiaWorld.getBlockData(w, x, y, z);
        
        switch (data) {
            case 1: // west-east straight
                if (hasTrackConnectedOn(w, x, y, z, BlockFace.EAST) && hasTrackConnectedOn(w, x, y, z, BlockFace.WEST)) {
                    paths = 2;
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.NORTH)) {
                        paths++;
                    }
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.SOUTH)) {
                        paths++;
                    }
                }
                break;
            case 0: // north-south straight
                if (hasTrackConnectedOn(w, x, y, z, BlockFace.NORTH) && hasTrackConnectedOn(w, x, y, z, BlockFace.SOUTH)) {
                    paths = 2;
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.WEST)) {
                        paths++;
                    }
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.EAST)) {
                        paths++;
                    }
                }
                break;
            case 7: // west-south corner
                if (hasTrackConnectedOn(w, x, y, z, BlockFace.SOUTH) && hasTrackConnectedOn(w, x, y, z, BlockFace.WEST)) {
                    paths = 2;
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.NORTH)) {
                        paths++;
                    }
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.EAST)) {
                        paths++;
                    }
                }
                break;
            case 8: // west-north corner
                if (hasTrackConnectedOn(w, x, y, z, BlockFace.NORTH) && hasTrackConnectedOn(w, x, y, z, BlockFace.WEST)) {
                    paths = 2;
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.EAST)) {
                        paths++;
                    }
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.SOUTH)) {
                        paths++;
                    }
                }
                break;
            case 9: // north-east corner
                if (hasTrackConnectedOn(w, x, y, z, BlockFace.NORTH) && hasTrackConnectedOn(w, x, y, z, BlockFace.EAST)) {
                    paths = 2;
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.SOUTH)) {
                        paths++;
                    }
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.WEST)) {
                        paths++;
                    }
                }
                break;
            case 6: // east-south corner
                if (hasTrackConnectedOn(w, x, y, z, BlockFace.EAST) && hasTrackConnectedOn(w, x, y, z, BlockFace.SOUTH)) {
                    paths = 2;
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.NORTH)) {
                        paths++;
                    }
                    if (hasTrackConnectedOn(w, x, y, z, BlockFace.WEST)) {
                        paths++;
                    }
                }
                break;
        }
        return paths > 2;
    }
    
    /**
     * Checks whether a track piece at the given coordinate has another track piece logically connect to it in the given direction.
     * 
     * valid: =7 L= F= == etc invalid: 7= =L =F =| etc
     * 
     * Valid track data values for the given directions: WEST: 1, 6, 9 (3) NORTH: 0, 6, 7 (4) EAST: 1, 7, 8 (2) SOUTH: 0, 8, 9 (5) values in braces are for the slanted up track. -- can add a check for the lower level too, but these will probably cause issues anyway -- so just keep the requirement of having flat track
     * 
     * @param w
     * @param x
     * @param y
     * @param z
     * @param direction
     * @return
     */
    public static boolean hasTrackConnectedOn(final World w, final int x, final int y, final int z, final BlockFace direction) {
        // (Etsija) Directionality fix
        final Block base = MinecartManiaWorld.getBlockAt(w, x, y, z);
        
        // (Etsija) Temp fix for Bukkit getRelative() method still returning old Notch directions
        BlockFace corrDirection = direction;
        switch (direction) {
            case NORTH:
                corrDirection = BlockFace.EAST;
                break;
            case EAST:
                corrDirection = BlockFace.SOUTH;
                break;
            case SOUTH:
                corrDirection = BlockFace.WEST;
                break;
            case WEST:
                corrDirection = BlockFace.NORTH;
                break;
        }
        final Block next = base.getRelative(corrDirection);
        
        if (isTrack(next)) {
            final byte nextData = next.getData();
            switch (direction) {
                case WEST:
                    return (nextData == 1) || (nextData == 6) || (nextData == 9);
                case NORTH:
                    return (nextData == 0) || (nextData == 6) || (nextData == 7);
                case EAST:
                    return (nextData == 1) || (nextData == 7) || (nextData == 8);
                case SOUTH:
                    return (nextData == 0) || (nextData == 8) || (nextData == 9);
            }
        }
        return false;
    }
    
    public static void doMinecartNearEntityCheck(final MinecartManiaMinecart minecart, final List<Entity> entities) {
        //Set a flag to stop this event from happening twice
        minecart.setDataValue("MinecartNearEntityEvent", true);
        final Vector location = minecart.minecart.getLocation().toVector();
        final int range = minecart.getRange() * minecart.getRange();
        for (final Entity e : entities) {
            
            if (e.isDead()) {
                continue;
            }
            
            final double distance = e.getLocation().toVector().distanceSquared(location);
            if (distance < range) {
                if (minecart.isStorageMinecart() && (e instanceof org.bukkit.entity.Item)) {
                    if (((MinecartManiaStorageCart) minecart).canAddItem(((org.bukkit.entity.Item) e).getItemStack())) {
                        if (((MinecartManiaStorageCart) minecart).addItem(((org.bukkit.entity.Item) e).getItemStack())) {
                            e.remove();
                        }
                    }
                }
            }
            
            if (!e.isDead() & (distance < 6)) {
                if (shouldKillEntity(minecart, e)) {
                    e.remove();
                } else if (clearedItemFromRails(e, minecart)) {
                    ;
                }
            }
        }
        //Reset the flag
        minecart.setDataValue("MinecartNearEntityEvent", null);
    }
    
    private static boolean shouldKillEntity(final MinecartManiaMinecart minecart, final Entity entity) {
        if (entity instanceof Arrow)
            return true; //special case, replaces them with arrow itemstack
        if (MinecartManiaConfiguration.isMinecartsKillMobs()) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof HumanEntity)
                    return false;
                if (entity instanceof Wolf)
                    return false;
                if (minecart.minecart.getPassenger() != null) {
                    if (minecart.minecart.getPassenger().getEntityId() == entity.getEntityId())
                        return false;
                }
                return true;
            }
        }
        return false;
    }
    
    private static boolean clearedItemFromRails(final Entity e, final MinecartManiaMinecart minecart) {
        if (MinecartManiaConfiguration.getMinecartsClearRailsSetting() != 0) {
            if (e.getEntityId() == minecart.minecart.getEntityId())
                return false;
            if (e instanceof Vehicle)
                return false;
            if ((minecart.minecart.getPassenger() != null) && (minecart.minecart.getPassenger().getEntityId() == e.getEntityId()))
                return false;
            if ((MinecartManiaConfiguration.getMinecartsClearRailsSetting() == 1) && (e instanceof LivingEntity))
                return false;
            if ((MinecartManiaConfiguration.getMinecartsClearRailsSetting() == 2) && (e instanceof Player))
                return false;
            if ((e instanceof Player) && minecart.isOwner(e))
                return false;
            
            if (minecart.isApproaching(e.getLocation().toVector())) {
                final Location current = e.getLocation();
                if (minecart.getMotionX() != 0.0D) {
                    final Location test = current.clone();
                    test.setZ(current.getZ() - 3);
                    Location loc = EntityUtils.getValidLocation(test.getBlock(), 1);
                    if (loc != null) {
                        e.teleport(loc);
                        return true;
                    }
                    test.setZ(current.getZ() + 3);
                    loc = EntityUtils.getValidLocation(test.getBlock(), 1);
                    if (loc != null) {
                        e.teleport(loc);
                        return true;
                    }
                }
                if (minecart.getMotionZ() != 0.0D) {
                    final Location test = current.clone();
                    test.setX(current.getX() - 3);
                    Location loc = EntityUtils.getValidLocation(test.getBlock(), 1);
                    if (loc != null) {
                        e.teleport(loc);
                        return true;
                    }
                    test.setX(current.getX() + 3);
                    loc = EntityUtils.getValidLocation(test.getBlock(), 1);
                    if (loc != null) {
                        e.teleport(loc);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static void updateNearbyItems(final MinecartManiaMinecart minecart) {
        if (minecart.getDataValue("MinecartNearEntityEvent") != null)
            return;
        final List<Entity> list = minecart.minecart.getWorld().getEntities();
        final Thread update = new Thread() {
            @Override
            public void run() {
                doMinecartNearEntityCheck(minecart, list);
            }
        };
        update.start();
    }
    
    public static CompassDirection[] getValiddirections() {
        return validDirections;
    }
}
