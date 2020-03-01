package com.lc.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;

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

	public static boolean isWeakestPlant(Material m) {
		switch (m) {
		case GRASS:
		case DANDELION:
		case POPPY:
		case BLUE_ORCHID:
		case ALLIUM:
		case AZURE_BLUET:
		case RED_TULIP:
		case ORANGE_TULIP:
		case PINK_TULIP:
		case WHITE_TULIP:
		case OXEYE_DAISY:
		case CORNFLOWER:
		case LILY_OF_THE_VALLEY:
		case WITHER_ROSE:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
			
		case LILY_PAD:
		case DEAD_BUSH:
			return true;
		default:
			return false;
		}
	}

	public static boolean isWeakPlant(Material m) {
		switch (m) {
		case FERN:
		case TALL_GRASS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isStrongPlant(Material m) {
		switch (m) {
		case ACACIA_SAPLING:
		case BIRCH_SAPLING:
		case DARK_OAK_SAPLING:
		case JUNGLE_SAPLING:
		case OAK_SAPLING:
		case SPRUCE_SAPLING:
		case LARGE_FERN:
			return true;
		default:
			return false;
		}
	}
	
	public static double getCauldronLevel(Block b) {
		if (b == null || b.getType() != Material.CAULDRON)
			return 0;
		Levelled cauldron = (Levelled) b.getBlockData();
		double level = cauldron.getLevel();
		return level / cauldron.getMaximumLevel();
	}
	
	public static void decreaseCauldronLevel(Block b) {
		if (b == null || b.getType() != Material.CAULDRON)
			return;
		Levelled cauldron = (Levelled) b.getBlockData();
		cauldron.setLevel(cauldron.getLevel() - 1);
		b.setBlockData(cauldron);
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
	
	public static boolean isInWater(Player p) {
		// waterlogged, return percent(cauldron levels, half blocks, ...)
		Block b = p.getLocation().getBlock();
		if (b.getType() == Material.WATER)
			return true;
		if (getCauldronLevel(b) > 0)
			return true;
		BlockData state = b.getBlockData();
		if (state instanceof Waterlogged) {
			Waterlogged watered = (Waterlogged) state;
			if (watered.isWaterlogged())
				return true;
		}
		return false;
	}
}
