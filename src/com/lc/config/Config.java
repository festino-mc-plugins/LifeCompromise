package com.lc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.lc.Main;

public class Config implements IConfig {
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");
	private final String PLUGIN_PATH;

	private final Main plugin;
	private final FileConfiguration config;
	
	private final HashMap<String, Object> map = new HashMap<>();
	private final List<TemperatureBlock> Tblocks = new ArrayList<>();
	
	public Config(Main main) {
		this.plugin = main;
		this.config = main.getConfig();
		PLUGIN_PATH = main.getDataFolder().getAbsolutePath();
		load();
	}
	
	public void load() {
		map.putAll(config.getValues(true));
		loadArmorTable();
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
		// TODO armor temp impact and weight to file
	}

	public void save() {
		for (Key key : Key.values()) {
			config.set(key.name, get(key));
		}
		plugin.saveConfig();
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
		DEFAULT_OUTPUT_TICKS("default-output-ticks", 10), // int

		IS_BUFFING("is-buffing", true),
		IS_DEBUFFING("is-debuffing", true),
		
		TEMP_SPEED("temp-speed", 1d / 900),
		TEMP_DIFFERENCE_IMPACT("temp-difference-impact", 1d / 10),
		K_SKIN_TENDENCY("k-skin-tendency", 1.5),
		THIRST_TICKS("thirst-ticks", 240), // int
		HUNGER_POWER("hunger-power", 0.05),
		WATER_REGEN("water-regen", 0.1),
		DEFAULT_SPEED("default-speed", 0.1),
		K_FIRE_RESISTANCE("k-fire-resistance", 1d / 4),
		
		BIOME_TEMP_DIFF_SMALL("biome-temp-diff-small", -10d),
		BIOME_TEMP_DIFF_BIG("biome-temp-diff-big", -30d),
		RANDOM_TEMP_DIFF("random-max-temp-diff", 2d),
		HEIGHT_MAX_T("height-max-temp", 63d),
		HEIGHT_T_DIFF("height-temp-diff-blocks", 20d),
		STORM_TEMP_DIFF("storm-temp-diff", 4d),
		THUNDER_TEMP_DIFF("thunder-temp-diff", 6d),
		NETHER_ROOF_TEMPERATURE("nether-roof-temperature", 31d),

		THIRST_SLOWNESS_PERCENT("thirst-debuff-1-pct", 50d),
		THIRST_NAUSEA_PERCENT("thirst-debuff-2-pct", 25d),
		TEMP_SLOWNESS("temp-slowness-pct", 20d),
		THIRST_SLOWNESS("thirst-slowness-pct", 15d),
		FROSTBITE_DEATH_DIFF("temp-diff-debuff-3f", -20d),
		FROSTBITE_2_DIFF("temp-diff-debuff-2f", -15d),
		FROSTBITE_1_DIFF("temp-diff-debuff-1f", -10d),
		SKIN_T_DEFAULT("temp-diff-normal", 26d),
		HEATSTROKE_1_DIFF("temp-diff-debuff-1h", 10d),
		HEATSTROKE_2_DIFF("temp-diff-debuff-2h", 15d),
		HEATSTROKE_DEATH_DIFF("temp-diff-debuff-3h", 20d),

		//BotAdaptationLimit: -10
		//TopAdaptationLimit: 10

		BUFF_SPEED("buff-speed", 10d),
		BUFF_SEC_REGEN("buff-regen-sec", 0.2),
		BUFF_JUMP_VELOCITY("buff-jump-vel", 0.1),
		BUFF_LIMIT_TEMP_DIFF("buff-limit-temp-diff", 4d),
		BUFF_LIMIT_THIRST("buff-limit-thirst", 60d),

		LEATHER_COLD("temp-armor-leather-f", 2.5),
		LEATHER_WARM("temp-armor-leather-h", 2.5),
		GOLDEN_COLD("temp-armor-golden-f", 1d),
		GOLDEN_WARM("temp-armor-golden-h", 0.5),
		CHAINMAIL_COLD("temp-armor-chainmail-f", 1d),
		CHAINMAIL_WARM("temp-armor-chainmail-h", 1d),
		IRON_COLD("temp-armor-iron-f", 0.5),
		IRON_WARM("temp-armor-iron-h", 0.5),
		DIAMOND_COLD("temp-armor-diamond-f", 0.35),
		DIAMOND_WARM("temp-armor-diamond-h", 0.35),
		NETHERITE_COLD("temp-armor-netherite-f", 0.25),
		NETHERITE_WARM("temp-armor-netherite-h", 3d),
		ELYTRA_COLD("temp-armor-elytra-f", 1d),
		ELYTRA_WARM("temp-armor-elytra-h", 1.5),
		TURTLE_COLD("temp-armor-turtle-f", 3d),
		TURTLE_WARM("temp-armor-turtle-h", 3d),
		WATER_COLD("temp-water-f", 0.5),
		WATER_WARM("temp-water-h", 1.5),
		
		WEIGHT_1("armor-weight-1", 7d),
		WEIGHT_2("armor-weight-2", 10d),
		WEIGHT_3("armor-weight-3", 14d),
		WEIGHT_4("armor-weight-4", 21d),

		URAN_1_RATE("uran-1-probability", 0.005),
		URAN_2_RATE("uran-2-probability", 0.001),
		URAN_3_RATE("uran-3-probability", 0.0004);
		
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
