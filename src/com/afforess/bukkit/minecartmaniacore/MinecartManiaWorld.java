package com.afforess.bukkit.minecartmaniacore;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class MinecartManiaWorld {
	private static ConcurrentHashMap<Integer,MinecartManiaMinecart> minecarts = new ConcurrentHashMap<Integer,MinecartManiaMinecart>();
	private static ConcurrentHashMap<String, Object> configuration = new ConcurrentHashMap<String,Object>();
	
	/**
	 ** Returns a new MinecartManiaMinecart from storage if it already exists, or creates and stores a new MinecartManiaMinecart object, and returns it
	 ** @param the minecart to wrap
	 **/
	 public static MinecartManiaMinecart getMinecartManiaMinecart(Minecart minecart) {
        MinecartManiaMinecart testMinecart = minecarts.get(new Integer(minecart.getEntityId()));
        if (testMinecart == null) {
        	MinecartManiaMinecart newCart = new MinecartManiaMinecart(minecart);
        	minecarts.put(new Integer(minecart.getEntityId()), newCart);
        	return newCart;
        } else {
           return testMinecart;
        }
    }
	 
	/**
	 ** Returns true if the Minecart with the given entityID was deleted, false if not.
	 ** @param the id of the minecart to delete
	 **/
	 public static boolean delMinecartManiaMinecart(int entityID) {
        if (minecarts.containsKey(new Integer(entityID))) {
            minecarts.remove(new Integer(entityID));
            return true;
        }
        return false;
    }
	 
	 /**
	 ** Returns any minecart at the given location, or null if none is present
	 ** @param the x - coordinate to check
	 ** @param the y - coordinate to check
	 ** @param the z - coordinate to check
	 **/
	 public static MinecartManiaMinecart getMinecartManiaMinecartAt(int x, int y, int z) {
		 Iterator<Entry<Integer, MinecartManiaMinecart>> i = minecarts.entrySet().iterator();
		 while (i.hasNext()) {
			 Entry<Integer, MinecartManiaMinecart> e = i.next();
			 if (e.getValue().minecart.getLocation().getBlockX() == x) {
				 if (e.getValue().minecart.getLocation().getBlockY() == y) {
					 if (e.getValue().minecart.getLocation().getBlockZ() == z) {
						 return e.getValue();
					 }
				 }
			 }
		 }
		
		 return null;
	 }
	 
	/**
	 ** Returns the value from the loaded configuration
	 ** @param the string key the configuration value is associated with
	 **/
	 public static Object getConfigurationValue(String key) {
		 if (configuration.containsKey(key)) {
			 return configuration.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new configuration value if it does not already exists, or resets an existing value
	 ** @param the string key the configuration value is associated with
	 ** @param the value to store
	 **/	 
	 public static void setConfigurationValue(String key, Object value) {
		 if (value == null) {
			 configuration.remove(key);
		 }
		 else {
			 configuration.put(key, value);
		 }
	 }
	 
	/**
	 ** Returns an integer value from the given object, if it exists
	 ** @param the object containing the value
	 **/		 
	 public static int getIntValue(Object o) {
		 if (o != null) {
			if (o instanceof Integer) {
				return ((Integer)o).intValue();
			}
		}
		return 0;
	 }
	 

	public static int getReverseBlockId() {
		return getIntValue(getConfigurationValue("reverse block"));
	}
	
	public static int getHighSpeedBoosterBlockId() {
		return getIntValue(getConfigurationValue("high speed booster block"));
	}
	
	public static int getLowSpeedBoosterBlockId() {
		return getIntValue(getConfigurationValue("low speed booster block"));
	}
	
	public static int getHighSpeedBrakeBlockId() {
		return getIntValue(getConfigurationValue("high speed brake block"));
	}
	
	public static int getLowSpeedBrakeBlockId() {
		return getIntValue(getConfigurationValue("low speed brake block"));
	}
	
	public static int getCatcherBlockId() {
		return getIntValue(getConfigurationValue("catcher block"));
	}
	
	public static int getEjectorBlockId() {
		return getIntValue(getConfigurationValue("ejector block"));
	}
	
	public static boolean isMinecartsKillMobs() {
		Object o = getConfigurationValue("minecarts kill mobs");
		if (o != null) {
			Boolean value = (Boolean)o;
			return value.booleanValue();
		}
		return true;
	}
	
	public static boolean isPressurePlateRails() {
		Object o = getConfigurationValue("Pressure Plate Rails");
		if (o != null) {
			Boolean value = (Boolean)o;
			return value.booleanValue();
		}
		return true;
	}
	
	/**
	 ** Returns the world that server is hosting
	 **/
	public static World getWorld() {
		return MinecartManiaCore.server.getWorlds()[0];
	}
	
	/**
	 ** Returns the block at the given x, y, z coordinates
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/	
	public static Block getBlockAt(int x, int y, int z) {
		return getWorld().getBlockAt(x, y, z);
	}
	
	/**
	 ** Returns the block at the given x, y, z coordinates
	 ** @param new block type id
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static void setBlockAt(int type, int x, int y, int z) {
		getWorld().getBlockAt(x, y, z).setTypeId(type);
	}
	
	/**
	 ** Returns the block data at the given x, y, z coordinates
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static byte getBlockData(int x, int y, int z) {
		return getWorld().getBlockAt(x, y, z).getData();
	}
	
	/**
	 ** sets the block data at the given x, y, z coordinates
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param new data to set
	 **/
	public static void setBlockData(int x, int y, int z, int data) {
		//Better to crash than to write bad data!
		if (data == -1 || data > Byte.MAX_VALUE) throw new IllegalArgumentException();
		getWorld().getBlockAt(x, y, z).setData((byte) (data));
	}
	
	/**
	 ** Returns true if the block at the given x, y, z coordinates is indirectly powered
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static boolean isBlockIndirectlyPowered(int x, int y, int z) {
		return isBlockPowered(x+1, y, z) || isBlockPowered(x-1, y, z) || isBlockPowered(x, y, z+1) || isBlockPowered(x, y, z-1) || isBlockPowered(x, y-1, z);
	}
	
	/**
	 ** Returns true if the block at the given x, y, z coordinates is directly powered
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static boolean isBlockPowered(int x, int y, int z) {
		MaterialData md = getWorld().getBlockAt(x, y, z).getState().getData();
		if (md instanceof Redstone) {
			return ((Redstone) md).isPowered();
		}
		return false;
	}
}
