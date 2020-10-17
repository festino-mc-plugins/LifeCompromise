package com.lc.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.lc.LCPlayer;
import com.lc.config.Config;
import com.lc.config.Config.Key;
import com.lc.config.TemperatureBlock;

public class UtilsTemperature {
	public static final double MIN_T = -273.15;
	public static final double MAX_T = 147_000000_000000_000000.0; // 147 quintillion degrees 
	private static final int DAY_TICKS = 20 * 60 * 20;
	private static final double REGULAR_TEMP = 24;
	
	private static final int RANDOM_TICKS_MAX = 300;
	private static final double RANDOM_DAY_DIFF_IMPACT = 1 / 6; // day to night and night to day
	
	private static final double BOOTS_IMPACT = 1, LEGGINGS_IMPACT = 2, CHESTPLATE_IMPACT = 3, HELMET_IMPACT = 1;
	private static final double FULL_IMPACT = BOOTS_IMPACT + LEGGINGS_IMPACT + CHESTPLATE_IMPACT + HELMET_IMPACT;
	
	private final Config config;
	private final TempWorld[] world_list;
	
	private int rand_ticks = 0;
	private double randomT = 0;
	
	public UtilsTemperature(Config config, List<World> world_list) {
		this.config = config;
		this.world_list = new TempWorld[world_list.size()];
		int i = 0;
		for (World w : world_list) {
			this.world_list[i] = new TempWorld(w);
			i++;
		}
	}
	
	/** Updates random */
	public void onTick() {
		rand_ticks++;
		if (rand_ticks >= RANDOM_TICKS_MAX) {
			rand_ticks = 0;
			randomT = config.get(Key.RANDOM_TEMP_DIFF);
			randomT *=  UtilsRandom.nextDouble() * 2 - 1;
		}
		
		for (TempWorld pair : world_list) {
			pair.update();
		}
	}
	
	public void tick(LCPlayer lcp)
	{
		double Taround = getAirTemperature(lcp.getPlayer().getLocation(), getFireResist(lcp.getPlayer()));
		lcp.setAirT(Taround);
		if (Taround < MIN_T) Taround = MIN_T;
		if (Taround > MAX_T) Taround = MAX_T;
		
		double Tplayer = lcp.getTemperature();
	    double dT = Taround - Tplayer;
	    
	    double temp_speed = getTemperatureSpeed(lcp.getPlayer(), dT > 0);
	    double diff_impact = config.get(Key.TEMP_DIFFERENCE_IMPACT);

		// Tbody = (diff_impact*Tbody + Taround) / temp_speed;
	    double temp_shift = (dT * diff_impact + Math.signum(dT)) * temp_speed;
	    
	    double dTnormal = lcp.getTemperature() - (Double) config.get(Key.SKIN_T_DEFAULT);
	    int dir_normal = (int) Math.signum(dTnormal);
	    int dir_real = (int) Math.signum(dT);
	    
	    if(dir_normal == dir_real)
	    	temp_shift = temp_shift * (Double) config.get(Key.K_SKIN_TENDENCY);
	    
	    if(-temp_shift < dT && dT < temp_shift)
	    	Tplayer = Taround;
	    else
	    	Tplayer += temp_shift;
	    
		lcp.setTemperature(Tplayer);
	}
	
	public TempDebuffLevel getDebuffLevel(double temp) {
		temp -= (Double) config.get(Key.SKIN_T_DEFAULT);
		double frostbite_temp[] = {config.get(Key.FROSTBITE_DEATH_DIFF), config.get(Key.FROSTBITE_2_DIFF), config.get(Key.FROSTBITE_1_DIFF)};
		double heatstroke_temp[] = {config.get(Key.HEATSTROKE_DEATH_DIFF), config.get(Key.HEATSTROKE_2_DIFF), config.get(Key.HEATSTROKE_1_DIFF)};
		if (temp < frostbite_temp[0]) return TempDebuffLevel.FROSTBITE_3;
		if (temp < frostbite_temp[1]) return TempDebuffLevel.FROSTBITE_2;
		if (temp < frostbite_temp[2]) return TempDebuffLevel.FROSTBITE_1;
		if (temp > heatstroke_temp[0]) return TempDebuffLevel.HEATSTROKE_3;
		if (temp > heatstroke_temp[1]) return TempDebuffLevel.HEATSTROKE_2;
		if (temp > heatstroke_temp[2]) return TempDebuffLevel.HEATSTROKE_1;
		return TempDebuffLevel.NONE;
	}
	
	public void updateDebuffLevel(LCPlayer lcp) {
		lcp.setTemperatureDebuff(getDebuffLevel(lcp.getTemperature()));
	}
	
	public double getFireResist(Player p)
	{
		if(p.getPotionEffect(PotionEffectType.FIRE_RESISTANCE) == null)
			return 1;
		else
			return config.get(Key.K_FIRE_RESISTANCE);
	}

	public double getAirTemperature(Location loc, double fire_resist_k) {
		double t_around = getBiomeTemperature(loc) + getBlocksTemperature(loc, fire_resist_k) + getRandomTemperature();
		
		return t_around;
	}
	
	public double getTemperatureSpeed(Player p, boolean is_increasing)
	{
		// TODO refactor
		double leather, golden, chainmail, iron, diamond, netherite;
		double elytra, turtle, water;
		if (is_increasing) {
			leather = config.get(Key.LEATHER_WARM);
			golden = config.get(Key.GOLDEN_WARM);
			chainmail = config.get(Key.CHAINMAIL_WARM);
			iron = config.get(Key.IRON_WARM);
			diamond = config.get(Key.DIAMOND_WARM);
			netherite = config.get(Key.NETHERITE_WARM);
			elytra = config.get(Key.ELYTRA_WARM);
			turtle = config.get(Key.TURTLE_WARM);
			water = config.get(Key.WATER_WARM);
		} else {
			leather = config.get(Key.LEATHER_COLD);
			golden = config.get(Key.GOLDEN_COLD);
			chainmail = config.get(Key.CHAINMAIL_COLD);
			iron = config.get(Key.IRON_COLD);
			diamond = config.get(Key.DIAMOND_COLD);
			netherite = config.get(Key.NETHERITE_COLD);
			elytra = config.get(Key.ELYTRA_COLD);
			turtle = config.get(Key.TURTLE_COLD);
			water = config.get(Key.WATER_COLD);
		}
	    
		ItemStack helmet = p.getInventory().getHelmet();
		ItemStack chestplate = p.getInventory().getChestplate();
		ItemStack leggings = p.getInventory().getLeggings();
		ItemStack boots = p.getInventory().getBoots();

		double slot_impact[] = {HELMET_IMPACT, CHESTPLATE_IMPACT, LEGGINGS_IMPACT, BOOTS_IMPACT};
		ItemStack armor[] = new ItemStack[4];
		if (UtilsArmor.isHelmet(helmet))
			armor[0] = helmet;
		if (UtilsArmor.isChestplate(chestplate))
			armor[1] = chestplate;
		if (UtilsArmor.isLeggings(leggings))
			armor[2] = leggings;
		if (UtilsArmor.isBoots(boots))
			armor[3] = boots;
		
		double armor_impact = 1;
		for (int i = 0; i < 4; i++) {
			ItemStack cur_is = armor[i];
			double material_impact = 1;
			if (UtilsArmor.isLeather(cur_is))
				material_impact = leather;
			if (UtilsArmor.isGolden(cur_is))
				material_impact = golden;
			if (UtilsArmor.isChainmail(cur_is))
				material_impact = chainmail;
			if (UtilsArmor.isIron(cur_is))
				material_impact = iron;
			if (UtilsArmor.isDiamond(cur_is))
				material_impact = diamond;
			if (UtilsArmor.isNetherite(cur_is))
				material_impact = netherite;
			if (UtilsArmor.isElytra(cur_is))
				material_impact = elytra;
			if (UtilsArmor.isTurtleHelmet(cur_is))
				material_impact = turtle;
			armor_impact *= Math.pow(material_impact, slot_impact[i] / FULL_IMPACT);
		}
		
	    double temp_speed = config.get(Key.TEMP_SPEED);
		temp_speed /= armor_impact;
		
	    if (Utils.isInWater(p))
	    	temp_speed /= water;
	    
	    return temp_speed;
	}
	
	public double getRandomTemperature() {
		return randomT;
	}
	
	public double getBlocksTemperature(Location loc, double fire_resist_k) {
		List<TemperatureBlock> Tblocks = config.getTemperatureBlocks();
		double max_dist = 0;
		// boolean nether
		for (TemperatureBlock tblock : Tblocks)
			if (tblock.r > max_dist)
				max_dist = tblock.r;
		int block_radius = (int) Math.ceil(max_dist);
		
		Block center_block = loc.getBlock();
		
		double result = 0;
		for (int i = -block_radius; i <= block_radius; i++) {
			for (int j = -block_radius; j <= block_radius; j++) {
				for (int k = -block_radius; k <= block_radius; k++) {
					Block b = center_block.getRelative(i, j, k);
					double distance = getDistance(loc, b);
					if (distance > block_radius)
						continue;
					Material m = b.getType();
					for (TemperatureBlock tblock : Tblocks) {
						if (tblock.r >= distance && tblock.m == m) {
							if (tblock.nether_only && !isNether(b.getBiome()))
								break;
							if (tblock.hasTag() && tblock.getTag() == "lit") // TODO explicit behavior (i.e. in TemperatureBlock)
								//System.out.print(tblock.toString());
								if (b.getBlockData() instanceof Lightable) {
									Lightable bd = (Lightable) b.getBlockData();
									if (!bd.isLit())
										break;
								}
							
							double temp = tblock.t * (tblock.r + 1 - distance);
							if (tblock.fireness)
								temp *= fire_resist_k;
							result += temp;
						}
					}
				}
			}
		}

		if (!isNether(loc.getBlock().getBiome())) {
			double height_max_t = config.get(Key.HEIGHT_MAX_T);
			double height_t_diff = config.get(Key.HEIGHT_T_DIFF);
			if (loc.getY() > height_max_t)
				result += (height_max_t - loc.getY()) / height_t_diff;
			else
				if (!isEnd(loc.getBlock().getBiome()))
					result -= 3 * (height_max_t - loc.getY()) / height_t_diff;
				else
					result -= (height_max_t - loc.getY()) / height_t_diff;
		}
		return result;
	}
	
	public double getDistance(Location loc, Block b) {
		return b.getLocation().add(0.5, 0.5, 0.5).distance(loc);
	}

	public double getBiomeTemperature(Location loc)
	{
		Biome biome = loc.getBlock().getBiome();
		World world = loc.getWorld();
		
		if (isNether(biome) && loc.getBlockY() > 127)
			return (Double) config.get(Key.NETHER_ROOF_TEMPERATURE);
		
		double temperature = getPureBiomeTemperature(biome);

		if (isNether(biome) || isEnd(biome))
			return temperature;
		
		if (world.hasStorm() && isRainy(biome))
		{
			temperature += (Double) config.get(Key.STORM_TEMP_DIFF);
			if(world.isThundering())
				temperature += (Double) config.get(Key.THUNDER_TEMP_DIFF);
		}
		
		double night_impact = getTimeTemperature(world);
		double dayly_temp_diff;
		if (isHeatCapacious(biome))
			dayly_temp_diff = config.get(Key.BIOME_TEMP_DIFF_BIG);
		else
			dayly_temp_diff = config.get(Key.BIOME_TEMP_DIFF_SMALL);

		double randomTday = 0;
		for (TempWorld pair : world_list) {
			if (pair.getWorld() == loc.getWorld()) {
				randomTday = pair.getDayRandom();
				break;
			}
		}
		temperature += dayly_temp_diff * (night_impact + (1 - 2 * night_impact) * randomTday);
		//System.out.println(temperature + " " + Insularity[i]);
		
		double isolation = 1 - getBiomityBySunlight(loc);
		temperature = temperature * (1 - isolation) + REGULAR_TEMP * isolation;
		return temperature;
	}
	
	public static double getPureBiomeTemperature(Biome biome) {
		switch (biome) {
		case BIRCH_FOREST: return 24;
		case TALL_BIRCH_FOREST: return 22;
		case BIRCH_FOREST_HILLS: return 21;
		case TALL_BIRCH_HILLS: return 19;
		case FOREST: return 24;
		case WOODED_HILLS: return 21;
		case FLOWER_FOREST: return 25;
		case DARK_FOREST: return 22;
		case DARK_FOREST_HILLS: return 20;
		case PLAINS: return 25;
		case SUNFLOWER_PLAINS: return 22;

		case BEACH: return 22;
		case RIVER: return 25;
		case SNOWY_BEACH: return 16;
		case STONE_SHORE: return 28;
		
		case MOUNTAINS: return 22;
		case WOODED_MOUNTAINS: return 20;
		case GRAVELLY_MOUNTAINS: return 20;
		case MODIFIED_GRAVELLY_MOUNTAINS: return 20;
		case MOUNTAIN_EDGE: return 18;

		case WARM_OCEAN: return 27;
		case DEEP_WARM_OCEAN: return 22;
		case LUKEWARM_OCEAN: return 20;
		case DEEP_LUKEWARM_OCEAN: return 14;
		case OCEAN: return 15;
		case DEEP_OCEAN: return 8;
		case COLD_OCEAN: return 9;
		case DEEP_COLD_OCEAN: return 3;
		case FROZEN_OCEAN: return -12;
		case DEEP_FROZEN_OCEAN: return -15;
		
		case FROZEN_RIVER: return -4;
		case SNOWY_TUNDRA: return 4;
		case ICE_SPIKES: return -4;
		case SNOWY_MOUNTAINS: return 2;
		
		case JUNGLE: return 38;
		case JUNGLE_EDGE: return 30;
		case JUNGLE_HILLS: return 32; 
		case MODIFIED_JUNGLE: return 34;
		case MODIFIED_JUNGLE_EDGE: return 31;
		case BAMBOO_JUNGLE: return 31;
		case BAMBOO_JUNGLE_HILLS: return 28;

		case DESERT: return 40; //
		case DESERT_LAKES: return 37; //
		case DESERT_HILLS: return 38; //
		case BADLANDS: return 35;
		case ERODED_BADLANDS: return 35;
		case BADLANDS_PLATEAU: return 35;
		case MODIFIED_BADLANDS_PLATEAU: return 35;
		case WOODED_BADLANDS_PLATEAU: return 35;
		case MODIFIED_WOODED_BADLANDS_PLATEAU: return 35;
		case SAVANNA: return 29;
		case SHATTERED_SAVANNA: return 27;
		case SAVANNA_PLATEAU: return 29;
		case SHATTERED_SAVANNA_PLATEAU: return 27;
		
		case MUSHROOM_FIELDS: return 22;
		case MUSHROOM_FIELD_SHORE: return 21;
		
		case GIANT_TREE_TAIGA: return 23;
		case GIANT_SPRUCE_TAIGA: return 23;
		case GIANT_TREE_TAIGA_HILLS: return 22;
		case GIANT_SPRUCE_TAIGA_HILLS: return 23;
		case SWAMP: return 25;
		case SWAMP_HILLS: return 22;
		case TAIGA: return 17;
		case TAIGA_MOUNTAINS: return 15;
		case SNOWY_TAIGA: return 12;
		case SNOWY_TAIGA_MOUNTAINS: return 8;
		case SNOWY_TAIGA_HILLS: return 10;
		case TAIGA_HILLS: return 15;

		case NETHER_WASTES: return 35;
		case CRIMSON_FOREST: return 34;
		case WARPED_FOREST: return 33;
		case BASALT_DELTAS: return 37;
		case SOUL_SAND_VALLEY: return 32;
		case THE_END:
		case END_BARRENS:
		case END_HIGHLANDS:
		case END_MIDLANDS:
		case SMALL_END_ISLANDS: return 16;
		case THE_VOID: return MIN_T;
		
		default: return 0;
		}
	}

	public double getTimeTemperature(World world) {
	    long time = world.getTime();
	    if (12000 < time && time < 13800) // morning
	    	return ((double)time - 12000) / 1800;
	    if (13800 <= time && time <= 22200) // night
	    	return 1;
	    if (22200 < time && time < 24000) // evening
	    	return (24000 - (double)time) / 1800;
	    return 0;
	}
	
	// TODO rework
	public double getBiomityBySunlight(Location l)
	{
		double result;
		if (l.getY() >= 63)
			result = 1;
		else
		{
			result = (l.getY() - 10) / 63;
			if (result < 0)
				result = 0;
			double temp = l.getBlock().getLightFromSky() / 10.0;
			if (temp > 1)
				temp = 1;
			result = result + temp + 0.1; // min 0.1
			if (result > 1)
				result = 1;
		}
		return result;
	}
	
	public static boolean isHeatCapacious(Biome biome) {
		switch (biome) {
		case DESERT:
		case DESERT_LAKES:
		case DESERT_HILLS:
		case BADLANDS:
		case ERODED_BADLANDS:
		case BADLANDS_PLATEAU:
		case MODIFIED_BADLANDS_PLATEAU:
		case WOODED_BADLANDS_PLATEAU:
		case MODIFIED_WOODED_BADLANDS_PLATEAU: 
		case SAVANNA:
		case SHATTERED_SAVANNA:
		case SAVANNA_PLATEAU:
		case SHATTERED_SAVANNA_PLATEAU:
		case STONE_SHORE:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isRainy(Biome biome) {
		switch (biome) {
		case DESERT:
		case DESERT_LAKES:
		case DESERT_HILLS:
		case BADLANDS:
		case ERODED_BADLANDS:
		case BADLANDS_PLATEAU:
		case MODIFIED_BADLANDS_PLATEAU:
		case WOODED_BADLANDS_PLATEAU:
		case MODIFIED_WOODED_BADLANDS_PLATEAU: 
		case SAVANNA:
		case SHATTERED_SAVANNA:
		case SAVANNA_PLATEAU:
		case SHATTERED_SAVANNA_PLATEAU:
			return false;
		default:
			break;
		}
		if (isNether(biome) || isEnd(biome))
			return false;
		return true;
	}
	
	public static boolean isNether(Biome biome) {
		switch (biome) {
		case NETHER_WASTES: return true;
		case CRIMSON_FOREST: return true;
		case WARPED_FOREST: return true;
		case BASALT_DELTAS: return true;
		case SOUL_SAND_VALLEY: return true;
		default: return false;
		}
	}
	
	public static boolean isEnd(Biome biome) {
		switch (biome) {
		case THE_END:
		case END_BARRENS:
		case END_HIGHLANDS:
		case END_MIDLANDS:
		case SMALL_END_ISLANDS: return true;
		default: return false;
		}
	}

	/*
	public void TemperatureAdaptation()
	{
		long t1 = System.nanoTime();

		switch(this.TempDebuff)
		{
		case -2: this.unwellTime+=2; break;
		case -1: this.unwellTime+=1; break;
		case 1: this.unwellTime+=1; break;
		case 2: this.unwellTime+=2; break;
		}
		if(unwellTime >= 100)
		{
			unwellTime-=100;
			NormalT = (double)Math.round(NormalT*100+TempDebuff)/100;
			if(NormalT > Config.TopAdaptationLimit) NormalT = Config.TopAdaptationLimit;
			else if(NormalT < Config.BotAdaptationLimit) NormalT = Config.BotAdaptationLimit;
		}

		mainListener.ExecutionAllTime[6] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[6] +=1;
	}*/
	
	private class TempWorld {
		private World w;
		private long daytime;
		private double randomTday = 0;
		
		public TempWorld(World w) {
			this.w = w;
			daytime = w.getFullTime() / DAY_TICKS;
		}
		public boolean update() {
			long daytime = w.getFullTime() / DAY_TICKS;
			if (this.daytime != daytime) {
				this.daytime = daytime;
				randomTday = UtilsRandom.nextDouble() * RANDOM_DAY_DIFF_IMPACT;
				return true;
			}
			return false;
		}
		public World getWorld() {
			return w;
		}
		public double getDayRandom() {
			return randomTday;
		}
	}
}
