package com.lc.config;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.lc.LCPlayer;
import com.lc.Main;
import com.lc.LCPlayer.LCMode;

public class PlayerFileManager {
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");
	private final String PLAYER_DIR;
	private final Logger logger;
	private final Main plugin;
	
	private static final String KEY_SOFT_LC = "LC-on", KEY_FORSED_LC = "forced-LC", KEY_TEMP = "T", KEY_THIRST = "Thirst", KEY_OUTPUT_TICKS = "output-ticks";
	
	public PlayerFileManager(String plugin_dir_path, Logger logger, Main plugin) {
		this.logger = logger;
		this.plugin = plugin;
		PLAYER_DIR = plugin_dir_path + PATH_SEPARATOR + "playerdata";
	}

	public LCPlayer load(Player p) {
		LCPlayer lcp = new LCPlayer(p, plugin);
		
		File dataFile = new File(PLAYER_DIR, p.getUniqueId() + ".yml");
		if (!dataFile.exists())	{
			return lcp;
		}
		
		FileConfiguration ymlFormat = YamlConfiguration.loadConfiguration(dataFile);
		try {
			String forsedLC_str = ymlFormat.getString(KEY_FORSED_LC);
			LCMode forsedLC = LCMode.valueOf(forsedLC_str);
			lcp.setForcedLC(forsedLC);
		} catch (Exception e) {}
		
		try {
			boolean softLC = ymlFormat.getBoolean(KEY_SOFT_LC);
			lcp.setSoftLC(softLC);
			double T = ymlFormat.getDouble(KEY_TEMP);
			lcp.setTemperature(T);
			double thirst = ymlFormat.getDouble(KEY_THIRST);
			lcp.setThirst(thirst);
			int output_ticks = ymlFormat.getInt(KEY_OUTPUT_TICKS);
			lcp.setOutputTicks(output_ticks);
		} catch (Exception e) {
			logger.severe("Could not load player " + p.getUniqueId() + "(" + p.getName() + ")!");
		}
		return lcp;
	}

	public void save(LCPlayer lcp) {
		if (lcp == null) return;
		
		try
		{
			File dataFile = new File(PLAYER_DIR, lcp.getUUID() + ".yml");
			FileConfiguration ymlFormat = YamlConfiguration.loadConfiguration(dataFile);

			ymlFormat.set(KEY_FORSED_LC, lcp.getForcedLC().toString());
			ymlFormat.set(KEY_SOFT_LC, lcp.getSoftLC());
			ymlFormat.set(KEY_TEMP, lcp.getTemperature());
			ymlFormat.set(KEY_THIRST, lcp.getThirst());
			ymlFormat.set(KEY_OUTPUT_TICKS, lcp.getOutputTicks());

			ymlFormat.save(dataFile);
		} catch (Exception e) {
			logger.severe("Could not save player " + lcp.getUUID() + "(" + lcp.getPlayer().getName() + ")!");
			e.printStackTrace();
		}
	}
}
