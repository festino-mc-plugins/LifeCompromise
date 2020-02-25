package com.lc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.lc.Main;

public class Config implements IConfig {
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");
	private final String PLUGIN_PATH;
	
	private final FileConfiguration config;
	
	private final HashMap<String, Object> map = new HashMap<>();
	private final List<TemperatureBlock> Tblocks = new ArrayList<>();
	
	public Config(Main main) {
		this.config = main.getConfig();
		PLUGIN_PATH = main.getDataFolder().getAbsolutePath();
		load();
	}
	
	public void load() {
		map.putAll(config.getValues(true));
		// loadArmorTable();
		loadTemperatureBlocks();
	}
	
	private void loadTemperatureBlocks() {
		List<TemperatureBlock> newTblocks = TemperatureBlockLoader.load(PLUGIN_PATH + PATH_SEPARATOR + "temperature-blocks.yml");
		Tblocks.clear();
		Tblocks.addAll(newTblocks);
	}
	
	public List<TemperatureBlock> getTemperatureBlocks() {
		return Tblocks;
	}
	
	public void loadArmorTable() {
		
	}

	public void save() {
		
	}
	
	// public void backup
	// public void restore
	public void set(Key key, Object value) {
		map.put(key.toString(), value);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(Key key, T default_value) {
		applyDefault(key, default_value);
		
		Class<?> clazz;
		if (default_value != null) {
			clazz = default_value.getClass();
		} else {
			clazz = key.getDefault().getClass();
		}
		
		Object res = map.getOrDefault(key.toString(), default_value);
		if (clazz.isInstance(res)) {
			return (T) res;
		}
		return default_value;
	}
	public <T extends Object> T get(Key key) {
		return get(key, null);
	}
	
	

	@Override
	public Object getObject(IConfig.Key key, Object default_value) {
		return map.getOrDefault(key.toString(), default_value);
	}

	@Override
	public Object getObject(IConfig.Key key) {
		return getObject(key, key.getDefault());
	}
	
	
	
	private void applyDefault(String key, Object default_value) {
		if (!map.containsKey(key)) {
			map.put(key, default_value);
		}
	}
	private void applyDefault(Key key, Object default_value) {
		if (default_value == null)
			applyDefault(key);
		else
			applyDefault(key.toString(), default_value);
	}
	private void applyDefault(Key key) {
		applyDefault(key.toString(), key.getDefault());
	}
	
	
	
	public enum Key implements IConfig.Key {
		LC_ON("LC-on", false),
		MODULE_TEMPERATURE("module-temperature", true),
		MODULE_TEMPERATURE_ADAPTATION("module-temperature-adaptation", true),
		MODULE_THIRST("module-thirst", true),
		MODULE_ARMOR_WEIGHT("module-armor-weight", true),
		MODULE_URAN_DROP("module-uran-drop", true),
		DEFAULT_OUTPUT_TICKS("default-output-ticks", (Integer) 10),

		IS_BUFFING("is-buffing", true),
		IS_DEBUFFING("is-debuffing", true),
		
		TEMP_SPEED("temp-speed", 1 / 900),
		TEMP_DIFFERENCE_IMPACT("temp-difference-impact", 1 / 10),
		K_SKIN_TENDENCY("k-skin-tendency", 1.5),
		THIRST_TICKS("thirst-ticks", 240),
		HUNGER_POWER("hunger-power", 0.05),
		WATER_REGEN("water-regen", 0.1),
		DEFAULT_SPEED("default-speed", 0.1),
		K_FIRE_RESISTANCE("k-fire-resistance", 1 / 11),
		
		BIOME_TEMP_DIFF_SMALL("biome-temp-diff-small", 10),
		BIOME_TEMP_DIFF_BIG("biome-temp-diff-big", 30),
		RANDOM_TEMP_DIFF("random-max-temp-diff", 2),
		HEIGHT_MAX_T("height-max-temp", 63),
		HEIGHT_T_DIFF("height-temp-diff-blocks", 20),
		STORM_TEMP_DIFF("storm-temp-diff", 4),
		THUNDER_TEMP_DIFF("thunder-temp-diff", 6),


/*minSlownessThirstProcent: 50
minNauseaThirstProcent: 25
speedTempDecrease: 20
speedThirstDecrease: 15*/
		THIRST_SLOWNESS_PERCENT("", 50),
		THIRST_NAUSEA_PERCENT("", 25),
		TEMP_SLOWNESS("", 20),
		THIRST_SLOWNESS("", 15),
		FROSTBITE_DEATH_DIFF("", -20),
		FROSTBITE_2_DIFF("", -15),
		FROSTBITE_1_DIFF("", -10),
		SKIN_T_DEFAULT("", 26),
		HEATSTROKE_1_DIFF("", 10),
		HEATSTROKE_2_DIFF("", 15),
		HEATSTROKE_DEATH_DIFF("", 20),

		//BotAdaptationLimit: -10
		//TopAdaptationLimit: 10

		BUFF_SPEED("", 10),
		BUFF_SEC_REGEN("", 0.2),
		BUFF_JUMP_VELOCITY("", 0.1),
		BUFF_LIMIT_TEMP_DIFF("", 4),
		BUFF_LIMIT_THIRST("", 60),

		LEATHER_COLD("", 2.5),
		LEATHER_WARM("", 2.5),
		GOLDEN_COLD("", 0),
		GOLDEN_WARM("", -2),
		CHAINMAIL_COLD("", 0),
		CHAINMAIL_WARM("", 0),
		IRON_COLD("", -2),
		IRON_WARM("", -2),
		DIAMOND_COLD("", -3),
		DIAMOND_WARM("", -3),
		WATER_COLD("", 2),
		WATER_WARM("", -1.5),
		
		WEIGHT_1("armorweight-1", 7),
		WEIGHT_2("armorweight-2", 10),
		WEIGHT_3("armorweight-3", 14),
		WEIGHT_4("armorweight-4", 21),

		URAN_1_RATE("uran-1-probability", 0.5),
		URAN_2_RATE("uran-2-probability", 0.1),
		URAN_3_RATE("uran-3-probability", 0.05);
		
		private final String name;
		private final Object default_value;

		Key(String name, Object default_value) {
			this.name = name;
			this.default_value = default_value;
		}
		public Object getDefault() { return default_value; }
		@Override
		public String toString() { return name; }
		
		public Object validateValue(String value_str) {
			try {
				if (default_value instanceof Boolean) {
					return Boolean.parseBoolean(value_str);
				}
				if (default_value instanceof Integer) {
					return Integer.parseInt(value_str);
				}
				if (default_value instanceof Double) {
					return Double.parseDouble(value_str);
				}
				if (default_value instanceof String) {
					return value_str;
				}
			} catch (Exception e) {}
			return null;
		}
		
		public Class<?> getValueClass() {
			if (default_value instanceof Boolean) {
				return Boolean.class;
			}
			if (default_value instanceof Integer) {
				return Integer.class;
			}
			if (default_value instanceof Double) {
				return Double.class;
			}
			if (default_value instanceof String) {
				return String.class;
			}
			return null;
		}
		
		public static boolean isValidKey(String key_str) {
			return getKey(key_str) != null;
		}
		
		public static Key getKey(String key_str) {
			for (Key key : Key.values())
				if (key.name.equalsIgnoreCase(key_str))
					return key;
			return null;
		}
		
		public static List<String> getKeys() {
			List<String> keys = new ArrayList<>();
			for (Key key : Key.values()) {
				keys.add(key.name);
			}
			return keys;
		}
	}
}
