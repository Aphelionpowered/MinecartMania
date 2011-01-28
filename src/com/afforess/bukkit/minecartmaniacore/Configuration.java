package com.afforess.bukkit.minecartmaniacore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Material;


public class Configuration {
	/**
	 ** Initializes Minecart Mania Core configuration values
	 ** 
	 **/
	public static void loadConfiguration() {
		readFile();
	}

	public static void readFile() {	
		
		
		File directory = new File("MinecartMania" + File.separator);
		if (!directory.exists())
			directory.mkdir();
		File options = new File("MinecartMania" + File.separator + "MinecartManiaCoreSettings.txt");
		if (!options.exists() || invalidFile(options))
		{
			WriteFile(options);
		}
		ReadFile(options);
	}
	
	private static boolean invalidFile(File file)
	{
		try {
			BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
			for (String s = ""; (s = bufferedreader.readLine()) != null; )
			{
				if (s.indexOf(MinecartManiaCore.description.getVersion()) > -1)
				{
					return false;
				}

			}
			bufferedreader.close();
		}
		catch (IOException exception)
		{
			return true;
		}
		return true;
	}
	
	private static void WriteFile(File file)
	{
		try
		{
			file.createNewFile();
			BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file));
			MinecartManiaFlatFile.createNewHeader(bufferedwriter, "Minecraft Mania Core" + MinecartManiaCore.description.getVersion(), "Minecart Mania Core Config Settings", true);
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Minecarts Kill Mobs", "true", 
			"Minecarts that collide with mobs and animals will kill them and continue uninterrupted.");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Pressure Plate Rails", "true", 
			"Pressure Plats will mimic the effect of rails, and minecarts will pass over them inhindered.");
			MinecartManiaFlatFile.createNewHeader(bufferedwriter, "Minecart Mania Block Settings", "", false);
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "High Speed Booster Block", Material.GOLD_BLOCK.toString(), 
					"Minecarts that pass over this will be boosted to 8x their current speed");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Low Speed Booster Block", Material.GOLD_ORE.toString(), 
					"Minecarts that pass over this will be boosted to 2x their current speed");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "High Speed Brake Block", Material.SOUL_SAND.toString(), 
					"Minecarts that pass over this will be slowed to 1/8 their current speed");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Low Speed Brake Block", Material.GRAVEL.toString(), 
					"Minecarts that pass over this will be slowed to 1/2 their current speed");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Reverse Block", Material.WOOL.toString(), 
					"Minecarts that pass over this will have their momentum and speed reveresed.");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Catcher Block", Material.OBSIDIAN.toString(), 
					"Minecarts that pass over this without being powered by redstone will be stopped");
			MinecartManiaFlatFile.createNewSetting(bufferedwriter, "Ejector Block", Material.IRON_BLOCK.toString(), 
			"Minecarts that pass over this will eject any passengers in the minecart");


			bufferedwriter.close();
		}
		catch (Exception exception)
		{
			MinecartManiaCore.log.severe("Failed to write Minecart Mania settings!");
			exception.printStackTrace();
		}
	}

	private static void ReadFile(File file)
	{
		try {
			MinecartManiaWorld.setConfigurationValue("minecarts kill mobs", new Boolean(
					MinecartManiaFlatFile.getValueFromSetting(file, "Minecarts Kill Mobs", "true")));
			MinecartManiaWorld.setConfigurationValue("Pressure Plate Rails", new Boolean(
					MinecartManiaFlatFile.getValueFromSetting(file, "Pressure Plate Rails", "true")));
			MinecartManiaWorld.setConfigurationValue("high speed booster block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "High Speed Booster Block", Material.GOLD_BLOCK.toString())).getId()));
			MinecartManiaWorld.setConfigurationValue("low speed booster block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "Low Speed Booster Block", Material.GOLD_ORE.toString())).getId()));
			MinecartManiaWorld.setConfigurationValue("high speed brake block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "High Speed Brake Block", Material.SOUL_SAND.toString())).getId()));
			MinecartManiaWorld.setConfigurationValue("low speed brake block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "Low Speed Brake Block", Material.GRAVEL.toString())).getId()));
			MinecartManiaWorld.setConfigurationValue("reverse block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "Reverse Block", Material.WOOL.toString())).getId()));
			MinecartManiaWorld.setConfigurationValue("catcher block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "Catcher Block", Material.OBSIDIAN.toString())).getId()));
			MinecartManiaWorld.setConfigurationValue("ejector block", new Integer(
					Material.valueOf(MinecartManiaFlatFile.getValueFromSetting(file, "Ejector Block", Material.IRON_BLOCK.toString())).getId()));
		}
		catch (Exception exception)
		{
			MinecartManiaCore.log.severe("Failed to read Minecart Mania settings!");
			exception.printStackTrace();
		}
	}

}
