package com.lc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class TempandArmorFunctions {
	static public JavaPlugin p;
	static double timetemperature = 0;
	double[] TBlocks_Sum;
	double[] TBlocks_Num;
	double[] TBlocks_dT;
	
	public double TBlocks(Location l, double Effect, double Tair)
	{
		long t1 = System.nanoTime();

		byte change = 0;
		for(byte i=0;i<TBlocks_dT.length;i++){
			TBlocks_dT[i] = Config.Blocks[i].T-Tair;
			if(TBlocks_dT[i]>0 && change == 0)
				change = i;
			TBlocks_Sum[i] = 0;
			TBlocks_Num[i] = 0;
		}
		double result = 0;
		double num = 0;
		double k=Config.TBlocksK/(Config.TBlocksK-1);
	  	double b=1/(1-Config.TBlocksK);

  		int r = (int) Math.floor(Config.maxR);
		for(int i = -r;i<=r;i++)
		{
			for(int j = -r;j<=r;j++)
			{
				for(int o = -r;o<=r;o++)
				{	
					Material material = l.getWorld().getBlockAt(l.getBlockX()+i,l.getBlockY()+j,l.getBlockZ()+o).getType();
					for(int cur=0;cur<Config.Blocks.length;cur++){
						if(Config.Blocks[cur].id == material){
							double distance = mainListener.distance(l.getX(),l.getY(),l.getZ(), l.getBlockX()+i+0.5,l.getBlockY()+j+0.5,l.getBlockZ()+o+0.5);
							if(distance < Config.Blocks[cur].R){
								double kT = Math.abs(k/Math.pow(Config.TBlocksK,distance/Config.Blocks[cur].R)+b);
								TBlocks_Sum[cur] += TBlocks_dT[cur]*(Config.Blocks[cur].fireness? Effect : 1);
								TBlocks_Num[cur] += kT;
								num += kT; // Num = num, bug
							}
						}
					}
				}
			}
		}
		boolean notEnded = true;
		byte u=change,d=(byte) (change-1),max=u,min=d;
		while(notEnded){
			if( (result<=0 || d<0) && u<TBlocks_dT.length ) {
				if(TBlocks_Num[u] != 0){
					result += TBlocks_Sum[u]*TBlocks_Num[u];
					max = u;
				}
				u+=1;
				continue;
			}
			if( (result>=0 || u >= TBlocks_dT.length) && d>=0){
				if(TBlocks_Num[d] != 0){
					result += TBlocks_Sum[d]*TBlocks_Num[d];
					min = d;
				}
				d-=1;
				continue;
			}
			notEnded = false;
		}
		if(result != 0.0){
			double KOE = 1.5;
			double rad = 0, radn = 0;
			for(byte i=0;i<TBlocks_dT.length;i++){
				rad += Config.Blocks[i].R*TBlocks_Num[i];
				radn += TBlocks_Num[i];
			}
			rad = rad/radn;
			double V = Math.PI*rad*rad*rad/20;
			//System.out.println(result+" "+num);
			num = Math.max(num, V);
			//System.out.println(V +" "+Config.Blocks[max].T+" "+((result/num)*KOE+Tair));
			result = (result/num)*KOE+Tair;
			if(result > Config.Blocks[max].T) result = Config.Blocks[max].T - Tair;
			else if(result < Config.Blocks[min].T) result = Config.Blocks[min].T - Tair;
			else result = result - Tair;
		}
		if(l.getY() > Config.heightMaxT)
			result += (Config.heightMaxT-l.getY())/Config.heightTDifference;
		else
			if(l.getBlock().getBiome() != Biome.SKY)
				result -= 3*(Config.heightMaxT-l.getY())/Config.heightTDifference;
			else
				result -= (Config.heightMaxT-l.getY())/Config.heightTDifference;
		
		mainListener.ExecutionAllTime[1] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[1] +=1;
		return result; //Лёд в 2 блоках, огонь в 3, а лава в 5
	}
	
	public double BiomeTemperature(LCPlayer lcpp)
	{
		long t1 = System.nanoTime();
		Location l = lcpp.p.getLocation();
		int a = 5;
		double temperature = 0;
		for(int i=-a/2; i<=a/2; i++)
			for(int j=-a/2; j<=a/2; j++)
			{
				Location l2 = l;
				l2.setX(l2.getX()+i);
				l2.setZ(l2.getZ()+j);
				temperature += biomeToTemperature(l2.getBlock().getBiome(), timetemperature);
			}
		temperature = temperature/(a*a);
		//System.out.println(temperature + " " + Insularity[i]);
		temperature = temperature*lcpp.Insularity+24*(1-lcpp.Insularity);
		mainListener.ExecutionAllTime[0] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[0] +=1;
		return temperature;
	}
	
	public double biomeToTemperature(Biome b, double timetemperature) {
		double temperature = 0; 
		if(b == Biome.BEACHES) temperature = 29;
		else if(b == Biome.BIRCH_FOREST) temperature = 24;
		else if(b == Biome.MUTATED_BIRCH_FOREST) temperature = 22;
		else if(b == Biome.BIRCH_FOREST_HILLS) temperature = 21;
		else if(b == Biome.MUTATED_BIRCH_FOREST_HILLS) temperature = 19;
		else if(b == Biome.COLD_BEACH) temperature = 16;
		else if(b == Biome.DEEP_OCEAN) temperature = 8;
		else if(b == Biome.DESERT) temperature = 40; //
		else if(b == Biome.MUTATED_DESERT) temperature = 37; //
		else if(b == Biome.DESERT_HILLS) temperature = 38; //
		else if(b == Biome.EXTREME_HILLS) temperature = 22;
		else if(b == Biome.MUTATED_EXTREME_HILLS) temperature = 20;
		else if(b == Biome.EXTREME_HILLS_WITH_TREES) temperature = 20;
		else if(b == Biome.SMALLER_EXTREME_HILLS) temperature = 18;
		else if(b == Biome.FOREST) temperature = 24;
		else if(b == Biome.MUTATED_FOREST) temperature = 25;
		else if(b == Biome.FOREST_HILLS) temperature = 21;
		else if(b == Biome.FROZEN_OCEAN) temperature = -12;
		else if(b == Biome.FROZEN_RIVER) temperature = -4;
		else if(b == Biome.ICE_FLATS) temperature = 4;
		else if(b == Biome.MUTATED_ICE_FLATS) temperature = -4;
		else if(b == Biome.ICE_MOUNTAINS) temperature = 2;
		else if(b == Biome.JUNGLE) temperature = 38;
		else if(b == Biome.MUTATED_JUNGLE) temperature = 34;
		else if(b == Biome.JUNGLE_EDGE) temperature = 30;
		else if(b == Biome.JUNGLE_HILLS) temperature = 32; 
		else if(b == Biome.MESA) temperature = 35;
		else if(b == Biome.MUTATED_MESA) temperature = 35;
		else if(b == Biome.MESA_CLEAR_ROCK) temperature = 34;
		else if(b == Biome.MUTATED_MESA_CLEAR_ROCK) temperature = 33;
		else if(b == Biome.MESA_ROCK) temperature = 37;
		else if(b == Biome.MUTATED_MESA_ROCK) temperature = 36;
		else if(b == Biome.MUSHROOM_ISLAND) temperature = 27; //22
		else if(b == Biome.MUSHROOM_ISLAND_SHORE) temperature = 26; //21
		else if(b == Biome.OCEAN) temperature = 15;
		else if(b == Biome.PLAINS) temperature = 25;
		else if(b == Biome.MUTATED_PLAINS) temperature = 22;
		else if(b == Biome.REDWOOD_TAIGA) temperature = 23;
		else if(b == Biome.MUTATED_REDWOOD_TAIGA) temperature = 23;
		else if(b == Biome.REDWOOD_TAIGA_HILLS) temperature = 22;
		else if(b == Biome.MUTATED_REDWOOD_TAIGA_HILLS) temperature = 23;
		else if(b == Biome.RIVER) temperature = 23;
		else if(b == Biome.ROOFED_FOREST) temperature = 22;
		else if(b == Biome.MUTATED_ROOFED_FOREST) temperature = 20;
		else if(b == Biome.SAVANNA) temperature = 29;
		else if(b == Biome.MUTATED_SAVANNA) temperature = 27;
		else if(b == Biome.SAVANNA_ROCK) temperature = 29;
		else if(b == Biome.MUTATED_SAVANNA_ROCK) temperature = 27;
		else if(b == Biome.STONE_BEACH) temperature = 28;
		else if(b == Biome.SWAMPLAND) temperature = 25;
		else if(b == Biome.MUTATED_SWAMPLAND) temperature = 22;
		else if(b == Biome.TAIGA) temperature = 17;
		else if(b == Biome.MUTATED_TAIGA) temperature = 15;
		else if(b == Biome.TAIGA_COLD) temperature = 12;
		else if(b == Biome.MUTATED_TAIGA_COLD) temperature = 8;
		else if(b == Biome.TAIGA_COLD_HILLS) temperature = 10;
		else if(b == Biome.TAIGA_HILLS) temperature = 15;
		else if(b == Biome.HELL) temperature = 29;
		else if(b == Biome.SKY) temperature = 16;
		else if(b == Biome.VOID) temperature = -272;
		else temperature = Config.skinTemperature; //hell 42, sky 16

		if(b == Biome.SKY || b == Biome.HELL)
			return temperature;
		
		if(b != Biome.SKY && b != Biome.HELL && p.getServer().getWorld("world").hasStorm()
		&& b != Biome.DESERT && b != Biome.DESERT_HILLS && b != Biome.MUTATED_DESERT 
        && b != Biome.SAVANNA && b != Biome.SAVANNA_ROCK && b != Biome.MUTATED_SAVANNA && b != Biome.MUTATED_SAVANNA_ROCK
        && b == Biome.MESA && b == Biome.MUTATED_MESA && b == Biome.MESA_ROCK && b == Biome.MUTATED_MESA_ROCK&& b == Biome.MESA_CLEAR_ROCK && b == Biome.MUTATED_MESA_CLEAR_ROCK)
		{
			temperature -= Config.StormTemperatureDecrease;
			if(p.getServer().getWorld("world").isThundering())
				temperature -= Config.ThunderTemperatureDecrease;
		}

		if(b == Biome.DESERT || b == Biome.DESERT_HILLS || b == Biome.MUTATED_DESERT ||
		   b == Biome.SAVANNA || b == Biome.SAVANNA_ROCK || b == Biome.MUTATED_SAVANNA || b == Biome.MUTATED_SAVANNA_ROCK ||
		   b == Biome.MESA || b == Biome.MUTATED_MESA || b == Biome.MESA_ROCK || b == Biome.MUTATED_MESA_ROCK|| b == Biome.MESA_CLEAR_ROCK || b == Biome.MUTATED_MESA_CLEAR_ROCK ||
		   b == Biome.STONE_BEACH ) //"60 в месе, пустыне, саванне и каменном пляже"
			temperature -= timetemperature*(double)Config.Difference2 + (0.5-timetemperature)*mainListener.RandomTDayDifference*(double)Config.Difference2/30; //60 -> 2*0.5*20*60*x = 20 60*x=1  x = 1/60
		else
			temperature -= timetemperature*(double)Config.Difference1 + 3*(0.5-timetemperature)*mainListener.RandomTDayDifference*(double)Config.Difference1/80; //20 -> 3*0.5/4*20*20*x = 7.5 3*20*20/8*x=7.5 300*x = 15 x = 1/20
		
		return temperature;
	}

	public static double TimeTemperature() { //once on tick  // ==0 днём, >0 в остальное время(==1 ночью)
	    long time = p.getServer().getWorld("world").getTime();
	    double k = 0;
	    if(12000 < time && time < 13800) //morning
	    	k += ((double)time-12000)/1800;
	    else if(13800 <= time && time <= 22200) //night
	    	k += 1;
	    else if(22200 < time && time < 24000) //evening
	    	k += (24000-(double)time)/1800;
	    return k;
	}
	
	
	
	public static double getArmorWeight(Player p)
	{
		double Weight = 0;
		org.bukkit.inventory.ItemStack m = p.getInventory().getHelmet();
		if(m != null)
			if(m.getType() == Material.LEATHER_HELMET) Weight += 1;
			else if(m.getType() == Material.GOLD_HELMET)    Weight += 1.5;
			else if(m.getType() == Material.CHAINMAIL_HELMET) Weight += 1.5;
			else if(m.getType() == Material.IRON_HELMET)    Weight += 2;
			else if(m.getType() == Material.DIAMOND_HELMET) Weight += 3;
			else Weight += 1;
		m = p.getInventory().getChestplate();
		if(m != null)
			if(m.getType() == Material.LEATHER_CHESTPLATE) Weight += 3;
			else if(m.getType() == Material.GOLD_CHESTPLATE)    Weight += 4.5;
			else if(m.getType() == Material.CHAINMAIL_CHESTPLATE) Weight += 4.5;
			else if(m.getType() == Material.IRON_CHESTPLATE)    Weight += 6;
			else if(m.getType() == Material.DIAMOND_CHESTPLATE) Weight += 9;
			else if(m.getType() == Material.ELYTRA) Weight += 2;
		m = p.getInventory().getLeggings();
		if(m != null)
			if(m.getType() == Material.LEATHER_LEGGINGS) Weight += 2;
			else if(m.getType() == Material.GOLD_LEGGINGS)    Weight += 3;
			else if(m.getType() == Material.CHAINMAIL_LEGGINGS) Weight += 3;
			else if(m.getType() == Material.IRON_LEGGINGS)    Weight += 4;
			else if(m.getType() == Material.DIAMOND_LEGGINGS) Weight += 6;
		m = p.getInventory().getBoots();
		if(m != null)
			if(m.getType() == Material.LEATHER_BOOTS) Weight += 1;
			else if(m.getType() == Material.GOLD_BOOTS)    Weight += 1.5;
			else if(m.getType() == Material.CHAINMAIL_BOOTS) Weight += 1.5;
			else if(m.getType() == Material.IRON_BOOTS)    Weight += 2;
			else if(m.getType() == Material.DIAMOND_BOOTS) Weight += 3;
		return Weight;
	}

	public double getK(Player p)
	{
		if(p.getPotionEffect(PotionEffectType.FIRE_RESISTANCE) == null)
			return 1;
		else
			return Config.FireResistanceK;
	}

	public double EvilT(Location l)
	{
		if(p.getServer().getPlayer("EvilGeniys") != null/*.isOnline()*/)
		{
			double len = mainListener.distance(l.getX(),l.getY(),l.getZ(),p.getServer().getPlayer("EvilGeniys").getLocation().getX(),p.getServer().getPlayer("EvilGeniys").getLocation().getY(),p.getServer().getPlayer("EvilGeniys").getLocation().getZ());
			if(len == 0)
				return 0;
			if(len <=15)
				return 5;
		}
		return 0;
	}
	
	public static void updateTime() {
		timetemperature = TimeTemperature();
	}
	
	public boolean isColorable(Material m) {
		return (m == Material.WOOL || m == Material.CARPET || m == Material.STAINED_CLAY || m == Material.BANNER || m == Material.CONCRETE_POWDER || m == Material.CONCRETE ||
				m.toString().contains("LEATHER_") || m.toString().contains("TERRACOTTA"));
	}
}
