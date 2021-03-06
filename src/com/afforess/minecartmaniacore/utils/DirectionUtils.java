package com.afforess.minecartmaniacore.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public abstract class DirectionUtils {
    public enum CompassDirection {
        NO_DIRECTION(-1),
        NORTH(0),
        NORTH_EAST(1),
        EAST(2),
        SOUTH_EAST(3),
        SOUTH(4),
        SOUTH_WEST(5),
        WEST(6),
        NORTH_WEST(7);
        private final int id;
        private static Map<Integer, CompassDirection> map;
        
        private CompassDirection(final int id) {
            this.id = id;
            add(id, this);
        }
        
        private static void add(final int type, final CompassDirection name) {
            if (map == null) {
                map = new HashMap<Integer, CompassDirection>();
            }
            
            map.put(type, name);
        }
        
        public int getType() {
            return id;
        }
        
        public static CompassDirection fromId(final int type) {
            return map.get(type);
        }
        
        @Override
        public String toString() {
            if (equals(CompassDirection.NORTH))
                return "North";
            if (equals(CompassDirection.NORTH_EAST))
                return "North-East";
            if (equals(CompassDirection.EAST))
                return "East";
            if (equals(CompassDirection.SOUTH_EAST))
                return "South-East";
            if (equals(CompassDirection.SOUTH))
                return "South";
            if (equals(CompassDirection.SOUTH_WEST))
                return "South-West";
            if (equals(CompassDirection.WEST))
                return "West";
            if (equals(CompassDirection.NORTH_WEST))
                return "North-West";
            return "No Direction";
        }
        
        // (Etsija) Directionality fix
        public Vector toVector(final double speed) {
            switch (this) {
                case NO_DIRECTION:
                    return new Vector(0, 0, 0);
                case NORTH:
                    return new Vector(0, 0, -speed);
                case NORTH_EAST:
                    return new Vector(speed, 0, -speed);
                case EAST:
                    return new Vector(speed, 0, 0);
                case SOUTH_EAST:
                    return new Vector(speed, 0, speed);
                case SOUTH:
                    return new Vector(0, 0, speed);
                case SOUTH_WEST:
                    return new Vector(-speed, 0, speed);
                case WEST:
                    return new Vector(-speed, 0, 0);
                case NORTH_WEST:
                    return new Vector(-speed, 0, -speed);
            }
            return null;
        }
        
        /**
         * Returns all 4 major compass directions.
         * @returns Major compass headings
         */
        public static CompassDirection[] getCardinalDirections() {
            return new CompassDirection[] { CompassDirection.NORTH, CompassDirection.EAST, CompassDirection.SOUTH, CompassDirection.WEST, };
        }
    }
    
    public static boolean isEqualOrNoDirection(final CompassDirection e1, final CompassDirection e2) {
        if (e1 == CompassDirection.NO_DIRECTION)
            return true;
        if (e2 == CompassDirection.NO_DIRECTION)
            return true;
        if (e1 == e2)
            return true;
        return false;
    }
    
    public static boolean isOrthogonalDirection(final CompassDirection dir) {
        return (dir == CompassDirection.NORTH) || (dir == CompassDirection.SOUTH) || (dir == CompassDirection.EAST) || (dir == CompassDirection.WEST);
    }
    
    public static CompassDirection getLeftDirection(final CompassDirection efacingDir) {
        if (efacingDir == CompassDirection.NORTH)
            return CompassDirection.WEST;
        if (efacingDir == CompassDirection.EAST)
            return CompassDirection.NORTH;
        if (efacingDir == CompassDirection.SOUTH)
            return CompassDirection.EAST;
        if (efacingDir == CompassDirection.WEST)
            return CompassDirection.SOUTH;
        return CompassDirection.NO_DIRECTION;
    }
    
    public static CompassDirection getRightDirection(final CompassDirection efacingDir) {
        if (efacingDir == CompassDirection.NORTH)
            return CompassDirection.EAST;
        if (efacingDir == CompassDirection.EAST)
            return CompassDirection.SOUTH;
        if (efacingDir == CompassDirection.SOUTH)
            return CompassDirection.WEST;
        if (efacingDir == CompassDirection.WEST)
            return CompassDirection.NORTH;
        return CompassDirection.NO_DIRECTION;
    }
    
    // (Etsija) Directionality fix
    public static Block getBlockTypeAhead(final World w, final CompassDirection efacingDir, final int x, final int y, final int z) {
        if (efacingDir == CompassDirection.WEST)
            return MinecartManiaWorld.getBlockAt(w, x - 1, y, z);
        if (efacingDir == CompassDirection.NORTH)
            return MinecartManiaWorld.getBlockAt(w, x, y, z - 1);
        if (efacingDir == CompassDirection.EAST)
            return MinecartManiaWorld.getBlockAt(w, x + 1, y, z);
        if (efacingDir == CompassDirection.SOUTH)
            return MinecartManiaWorld.getBlockAt(w, x, y, z + 1);
        return null;
    }
    
    // (Etsija) Directionality fix
    public static int getMinetrackRailDataForDirection(final CompassDirection eOverrideDir, final CompassDirection eFacingDir) {
        if (eFacingDir == CompassDirection.NORTH) {
            if (eOverrideDir == CompassDirection.EAST)
                return 6;
            if (eOverrideDir == CompassDirection.NORTH)
                return 0;
            if (eOverrideDir == CompassDirection.WEST)
                return 7;
        }
        if (eFacingDir == CompassDirection.EAST) {
            if (eOverrideDir == CompassDirection.EAST)
                return 1;
            if (eOverrideDir == CompassDirection.NORTH)
                return 8;
            if (eOverrideDir == CompassDirection.SOUTH)
                return 7;
        }
        if (eFacingDir == CompassDirection.WEST) {
            if (eOverrideDir == CompassDirection.WEST)
                return 1;
            if (eOverrideDir == CompassDirection.NORTH)
                return 9;
            if (eOverrideDir == CompassDirection.SOUTH)
                return 6;
        }
        if (eFacingDir == CompassDirection.SOUTH) {
            if (eOverrideDir == CompassDirection.WEST)
                return 8;
            if (eOverrideDir == CompassDirection.EAST)
                return 9;
            if (eOverrideDir == CompassDirection.SOUTH)
                return 0;
        }
        return -1;
    }
    
    public static CompassDirection getOppositeDirection(final CompassDirection direction) {
        int val = direction.getType();
        if (val < 4) {
            val += 4;
        } else {
            val -= 4;
        }
        return CompassDirection.fromId(val);
    }
    
    private static boolean isFacingNorth(final double degrees, final double leeway) {
        return ((0 <= degrees) && (degrees < (45 + leeway))) || (((315 - leeway) <= degrees) && (degrees <= 360));
    }
    
    private static boolean isFacingEast(final double degrees, final double leeway) {
        return ((45 - leeway) <= degrees) && (degrees < (135 + leeway));
    }
    
    private static boolean isFacingSouth(final double degrees, final double leeway) {
        return ((135 - leeway) <= degrees) && (degrees < (225 + leeway));
    }
    
    private static boolean isFacingWest(final double degrees, final double leeway) {
        return ((225 - leeway) <= degrees) && (degrees < (315 + leeway));
    }
    
    public static CompassDirection getDirectionFromMinecartRotation(double degrees) {
        
        while (degrees < 0D) {
            degrees += 360D;
        }
        while (degrees > 360D) {
            degrees -= 360D;
        }
        
        final CompassDirection direction = getDirectionFromRotation(degrees);
        
        final double leeway = 15;
        if (direction.equals(CompassDirection.NORTH) || direction.equals(CompassDirection.SOUTH)) {
            if (isFacingEast(degrees, leeway))
                return CompassDirection.EAST;
            if (isFacingWest(degrees, leeway))
                return CompassDirection.WEST;
        } else if (direction.equals(CompassDirection.EAST) || direction.equals(CompassDirection.WEST)) {
            if (isFacingNorth(degrees, leeway))
                return CompassDirection.NORTH;
            if (isFacingSouth(degrees, leeway))
                return CompassDirection.SOUTH;
        }
        
        return direction;
    }
    
    public static CompassDirection getDirectionFromRotation(double degrees) {
        
        while (degrees < 0D) {
            degrees += 360D;
        }
        while (degrees > 360D) {
            degrees -= 360D;
        }
        if (isFacingNorth(degrees, 0))
            return CompassDirection.NORTH;
        if (isFacingEast(degrees, 0))
            return CompassDirection.EAST;
        if (isFacingSouth(degrees, 0))
            return CompassDirection.SOUTH;
        if (isFacingWest(degrees, 0))
            return CompassDirection.WEST;
        
        return CompassDirection.NO_DIRECTION;
    }
    
    // (Etsija) Fixed wrong directions for L and R
    public static CompassDirection getDirectionFromString(final String dir, final CompassDirection facingDir) {
        
        if ((dir.indexOf("W") > -1) || (dir.toLowerCase().indexOf("west") > -1))
            return CompassDirection.WEST;
        if ((dir.indexOf("E") > -1) || (dir.toLowerCase().indexOf("east") > -1))
            return CompassDirection.EAST;
        if ((dir.indexOf("S") > -1) || (dir.toLowerCase().indexOf("south") > -1))
            return CompassDirection.SOUTH;
        if ((dir.indexOf("N") > -1) || (dir.toLowerCase().indexOf("north") > -1))
            return CompassDirection.NORTH;
        if (!facingDir.equals(CompassDirection.NO_DIRECTION)) {
            if ((dir.indexOf("STR") > -1) || (dir.toLowerCase().indexOf("straight") > -1))
                return facingDir;
            if ((dir.indexOf("L") > -1) || (dir.toLowerCase().indexOf("left") > -1))
                return getLeftDirection(facingDir);
            if ((dir.indexOf("R") > -1) || (dir.toLowerCase().indexOf("right") > -1))
                return getRightDirection(facingDir);
        }
        
        return CompassDirection.NO_DIRECTION;
    }
    
    // (Etsija) Directionality fix
    public static CompassDirection getSignFacingDirection(final Sign sign) {
        final int data = MinecartManiaWorld.getBlockData(sign.getWorld(), sign.getX(), sign.getY(), sign.getZ());
        final Block block = sign.getBlock();
        if (block.getType().equals(Material.SIGN_POST)) {
            if (data == 0x0)
                return DirectionUtils.CompassDirection.SOUTH;
            if ((data == 0x1) || (data == 0x2) || (data == 0x3))
                return DirectionUtils.CompassDirection.SOUTH_WEST;
            if (data == 0x4)
                return DirectionUtils.CompassDirection.WEST;
            if ((data == 0x5) || (data == 0x6) || (data == 0x7))
                return DirectionUtils.CompassDirection.NORTH_WEST;
            if (data == 0x8)
                return DirectionUtils.CompassDirection.NORTH;
            if ((data == 0x9) || (data == 0xA) || (data == 0xB))
                return DirectionUtils.CompassDirection.NORTH_EAST;
            if (data == 0xC)
                return DirectionUtils.CompassDirection.EAST;
            if ((data == 0xD) || (data == 0xE) || (data == 0xF))
                return DirectionUtils.CompassDirection.SOUTH_EAST;
            return DirectionUtils.CompassDirection.NO_DIRECTION;
        } else {
            if (data == 0x3)
                return DirectionUtils.CompassDirection.SOUTH;
            if (data == 0x4)
                return DirectionUtils.CompassDirection.WEST;
            if (data == 0x2)
                return DirectionUtils.CompassDirection.NORTH;
            if (data == 0x5)
                return DirectionUtils.CompassDirection.EAST;
            
            return DirectionUtils.CompassDirection.NO_DIRECTION;
        }
    }
    
    public static BlockFace CompassDirectionToBlockFace(final CompassDirection dir) {
        if (dir.equals(CompassDirection.EAST))
            return BlockFace.EAST;
        if (dir.equals(CompassDirection.WEST))
            return BlockFace.WEST;
        if (dir.equals(CompassDirection.NORTH))
            return BlockFace.NORTH;
        if (dir.equals(CompassDirection.SOUTH))
            return BlockFace.SOUTH;
        if (dir.equals(CompassDirection.NORTH_EAST))
            return BlockFace.NORTH_EAST;
        if (dir.equals(CompassDirection.NORTH_WEST))
            return BlockFace.NORTH_WEST;
        if (dir.equals(CompassDirection.SOUTH_EAST))
            return BlockFace.SOUTH_EAST;
        if (dir.equals(CompassDirection.SOUTH_WEST))
            return BlockFace.SOUTH_WEST;
        
        return BlockFace.SELF;
    }
}
