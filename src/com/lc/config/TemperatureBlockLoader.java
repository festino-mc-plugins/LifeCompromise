package com.lc.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TemperatureBlockLoader {
	
	private static final String KEY_TEMP = "T", KEY_RADIUS = "R", KEY_FIRE = "fiery", KEY_NETHER = "Nether-only";
	
	public static List<TemperatureBlock> load(String path) {
		List<TemperatureBlock> Tblocks = new ArrayList<>();

		File data_file = new File(path);
		if (!data_file.exists())	{
			try {
				// TODO info message
				System.out.print("created \"" + path + "\"");
				data_file.createNewFile();
			} catch (IOException e) {
				// TODO error message
				System.out.print("Couldn't create new file \"" + path + "\"");
			}
			return Tblocks;
		}
		
		FileConfiguration yml_format = YamlConfiguration.loadConfiguration(data_file);
		for(String block_id : yml_format.getKeys(false)) {
			if (!yml_format.isConfigurationSection(block_id)) {
				// TODO error
				System.out.print(block_id + " is not ConfigurationSection");
				continue;
			}
			
			Material m;
			try {
				m = Material.valueOf(block_id.toUpperCase());
			} catch (Exception e) {
				// TODO error
				System.out.print(block_id + " is not valid Material");
				continue;
			}
			
			ConfigurationSection cs = yml_format.getConfigurationSection(block_id);
			double temp = cs.getDouble(KEY_TEMP, Double.NEGATIVE_INFINITY);
			double radius = cs.getDouble(KEY_RADIUS, -1);
			boolean fireness = cs.getBoolean(KEY_FIRE, false);
			boolean nether_only = cs.getBoolean(KEY_NETHER, false);
			if (temp == Double.NEGATIVE_INFINITY || radius < 0) {
				// error
				System.out.print("Invalid arguments " + temp + " or " + radius);
				continue;
			}
			
			TemperatureBlock tblock = new TemperatureBlock(m, radius, temp, fireness, nether_only);
			// System.out.print(tblock.toString());
			Tblocks.add(tblock);
		}
		
		return Tblocks;
	}
}
