package com.lc.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;

public class Utils {
	
	public static String truncDouble(Double d, int length) {
		String format = d.toString();
		if (format.indexOf(".") > length) {
			// error
		} else if (format.length() > length) {
			format = format.substring(0, length);
			if (format.endsWith(".")) {
				format = format.substring(0, format.length() - 1);
			}
		} else {
			while (format.length() < length) {
				format += "0";
			}
		}
		
		return format;
	}

	
	
	public static double getCauldronLevel(Block b) {
		if (b == null || b.getType() != Material.CAULDRON)
			return 0;
		Levelled cauldron = (Levelled) b.getBlockData();
		double level = cauldron.getLevel();
		return level / cauldron.getMaximumLevel();
	}
	
	public static double getLiquidLevel(Block b) {
		if (b == null || !b.isLiquid())
			return 0;
		Levelled liquid = (Levelled) b.getBlockData();
		int genuine_max_lvl = liquid.getMaximumLevel() / 2; // 15 / 2 = 7 
		return (genuine_max_lvl - liquid.getLevel()) / (double) genuine_max_lvl;
	}
	
	public static boolean isStationaryLiquid(Block b) {
		if (b == null || !b.isLiquid())
			return false;
		return getLiquidLevel(b) == 1;
	}
	
	public static boolean isStationaryWater(Block b) {
		return b != null && b.getType() == Material.WATER && isStationaryLiquid(b);
	}
	
	/*public static double getCauldronLevel(Block b) {
		if (b == null || b.getType() != Material.CAULDRON)
			return 0;
		Levelled cauldron = (Levelled) b.getBlockData();
		double level = cauldron.getLevel();
		return level / cauldron.getMaximumLevel();
	}
	
	public static int getLiquidLevel(Block b) {
		if (b == null || !b.isLiquid())
			return 0;
		return ((Levelled) b.getBlockData()).getLevel();
	}
	
	public static boolean isStationaryLiquid(Block b) {
		if (b == null || !b.isLiquid())
			return false;
		return getLiquidLevel(b) == ((Levelled) b.getBlockData()).getMaximumLevel() / 2;
	}
	
	public static boolean isStationaryWater(Block b) {
		return b != null && b.getType() == Material.WATER && isStationaryLiquid(b);
	}*/
}
