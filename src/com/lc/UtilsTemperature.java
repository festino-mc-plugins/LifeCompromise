package com.lc;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class UtilsTemperature {
	private int randomMaxTicks = 300;
	
	public static void tick(LCPlayer lcp) {
		
	}

	/*if(dTnormal>Config.heatDeathT) this.TempDebuff=3;
	else if(dTnormal<Config.frostDeathT) this.TempDebuff=-3;
	else if(dTnormal>Config.heatstroke2) this.TempDebuff=2;
	else if(dTnormal<Config.frostbite2) this.TempDebuff=-2;
	else if(dTnormal>Config.heatstroke1) this.TempDebuff=1;
	else if(dTnormal<Config.frostbite1) this.TempDebuff=-1;
	else {this.TempDebuff=0; this.unwellTime = 0;}

	
	public void AlterTemperature()
	{
		//double TimeAndWeather = TimeWeatherTemperature();
	    Location l = p.getLocation();
	    l.setY(l.getY()+1);
		Taround = BiomeTemperature + BlocksTemperature + mainListener.RandomT + EvilTemperature;
		if(Taround > 147000000000000000000000.0) Taround = 147000000000000000000000.0;  //147 квинтиллионов градусов
		//Tbody = (k*Tbody+Taround)/(k+1);
	    dTair = this.T-Taround;
		ItemStack CurrentSlot = p.getInventory().getHelmet();
	    double KTime2 = Config.KTime;
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_HELMET)
	    		if(dTair > 0) KTime2 += Config.leatherColdBonus;
	    		else       KTime2 += Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_HELMET)
	    		if(dTair > 0) KTime2 += Config.goldColdBonus;
	    		else       KTime2 += Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_HELMET)
	    		if(dTair > 0) KTime2 += Config.chainmailColdBonus;
	    		else       KTime2 += Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_HELMET)
	    		if(dTair > 0) KTime2 += Config.ironColdBonus;
	    		else       KTime2 += Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_HELMET)
	    		if(dTair > 0) KTime2 += Config.diamondColdBonus;
	    		else       KTime2 += Config.diamondWarmBonus;
		CurrentSlot = p.getInventory().getChestplate();
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_CHESTPLATE)
	    		if(dTair > 0) KTime2 += 3*Config.leatherColdBonus;
	    		else       KTime2 += 3*Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_CHESTPLATE)
	    		if(dTair > 0) KTime2 += 3*Config.goldColdBonus;
	    		else       KTime2 += 3*Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_CHESTPLATE)
	    		if(dTair > 0) KTime2 += 3*Config.chainmailColdBonus;
	    		else       KTime2 += 3*Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_CHESTPLATE)
	    		if(dTair > 0) KTime2 += 3*Config.ironColdBonus;
	    		else       KTime2 += 3*Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_CHESTPLATE)
	    		if(dTair > 0) KTime2 += 3*Config.diamondColdBonus;
	    		else       KTime2 += 3*Config.diamondWarmBonus;
		CurrentSlot = p.getInventory().getLeggings();
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_LEGGINGS)
	    		if(dTair > 0) KTime2 += 2*Config.leatherColdBonus;
	    		else       KTime2 += 2*Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_LEGGINGS)
	    		if(dTair > 0) KTime2 += 2*Config.goldColdBonus;
	    		else       KTime2 += 2*Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_LEGGINGS)
	    		if(dTair > 0) KTime2 += 2*Config.chainmailColdBonus;
	    		else       KTime2 += 2*Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_LEGGINGS)
	    		if(dTair > 0) KTime2 += 2*Config.ironColdBonus;
	    		else       KTime2 += 2*Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_LEGGINGS)
	    		if(dTair > 0) KTime2 += 2*Config.diamondColdBonus;
	    		else       KTime2 += 2*Config.diamondWarmBonus;
		CurrentSlot = p.getInventory().getBoots();
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_BOOTS)
	    		if(dTair > 0) KTime2 += Config.leatherColdBonus;
	    		else       KTime2 += Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_BOOTS)
	    		if(dTair > 0) KTime2 += Config.goldColdBonus;
	    		else       KTime2 += Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_BOOTS)
	    		if(dTair > 0) KTime2 += Config.chainmailColdBonus;
	    		else       KTime2 += Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_BOOTS)
	    		if(dTair > 0) KTime2 += Config.ironColdBonus;
	    		else       KTime2 += Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_BOOTS)
	    		if(dTair > 0) KTime2 += Config.diamondColdBonus;
	    		else       KTime2 += Config.diamondWarmBonus;
	    if(p.getLocation().getBlock().getType() == Material.WATER ||
		   p.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
	    	if(dTair > 0) //Tskin-Tair
	    		KTime2 *= Config.WaterColdBonus;
	    	else
	    		KTime2 *= Config.WaterWarmBonus;
	    double mT = (-dTair/Config.KRatio-mainListener.sgn(dTair))/KTime2;
	    if(mainListener.sgn(this.T-this.NormalT) == mainListener.sgn(dTair)) //dTair = T[i]-Taround;
	    	mT = mT*Config.skinTendencyK;
	    if(-mT < dTair && dTair < mT)
	    	this.T = Taround;
	    else
	    	this.T += mT;
		dTnormal = this.T - this.NormalT;
	}
	
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
	}

	dayticks = getServer().getWorld(WORLD_NAME).getTime();
	

	public double TBlocks(Location l, double Effect)
	{
		double result = 0;
		if(l.getBlock().getBiome() == Biome.HELL)
			for(int i = Netherradius;i>=-Netherradius;i--)
			{
				for(int j = Netherradius;j>=-Netherradius;j--)
				{
					for(int o = Netherradius;o>=-Netherradius;o--)
					{
						Material material = l.getWorld().getBlockAt(l.getBlockX()+i,l.getBlockY()+j,l.getBlockZ()+o).getType();
						double distance = distance(l.getX(),l.getY(),l.getZ(), l.getBlockX()+i+0.5,l.getBlockY()+j+0.5,l.getBlockZ()+o+0.5);
						if(distance < Lavaradius && (material == Material.LAVA || material == Material.STATIONARY_LAVA))
							result += LavaT*(Lavaradius+1-distance)*Effect;
						else if(distance < Fireradius && material == Material.FIRE)
							result += FireT*(Fireradius+1-distance)*Effect;
						else if(distance < Iceradius && material == Material.ICE)
							result += IceT*(Iceradius+1-distance);
						else if(distance < Packediceradius && material == Material.PACKED_ICE)
							result += PackedIceT*(Packediceradius+1-distance);
						else if(distance < Magmaradius && material == Material.MAGMA)
							result += MagmaT*(Magmaradius+1-distance)*Effect;
						else if(distance < Netherradius && material == Material.NETHERRACK)
							result += NetherT*(Netherradius-distance)*Effect;
						else if(distance < Furnaceradius && material == Material.BURNING_FURNACE)
							result += FurnaceT*(Furnaceradius+1-distance);
						else if(distance < Torchradius && material == Material.TORCH){
							distance = distance(l.getX(),l.getY()+1.5,l.getZ(), l.getBlockX()+i+0.5,l.getBlockY()+j+0.5,l.getBlockZ()+o+0.5);
							if(distance < Torchradius)
								result += TorchT*(Torchradius+0.25-distance);}
					}
				}
			}
		else
		{
			for(int i = -5;i<=5;i++)
			{
				for(int j = -5;j<=5;j++)
				{
					for(int o = -5;o<=5;o++)
					{
						Material material = l.getWorld().getBlockAt(l.getBlockX()+i,l.getBlockY()+j,l.getBlockZ()+o).getType();
						double distance = distance(l.getX(),l.getY(),l.getZ(), l.getBlockX()+i+0.5,l.getBlockY()+j+0.5,l.getBlockZ()+o+0.5);
						if(distance < Lavaradius && material == Material.LAVA)
							result += LavaT/2*(Lavaradius+1-distance)*Effect;
						else if(distance < Lavaradius && material == Material.STATIONARY_LAVA)
							result += LavaT*(Lavaradius+1-distance)*Effect;
						else if(distance < Fireradius && material == Material.FIRE)
							result += FireT*(Fireradius+1-distance)*Effect;
						else if(distance < Iceradius && material == Material.ICE)
							result += IceT*(Iceradius+1-distance);
						else if(distance < Packediceradius && material == Material.PACKED_ICE)
							result += PackedIceT*(Packediceradius+1-distance);
						else if(distance < Magmaradius && material == Material.MAGMA)
							result += MagmaT*(Magmaradius+1-distance)*Effect;
						else if(distance < Furnaceradius && material == Material.BURNING_FURNACE)
							result += FurnaceT*(Furnaceradius+1-distance);
						else if(distance-1.5 < Torchradius && material == Material.TORCH){
							distance = distance(l.getX(),l.getY()+1.5,l.getZ(), l.getBlockX()+i+0.5,l.getBlockY()+j+0.5,l.getBlockZ()+o+0.5);
							if(distance < Torchradius)
								result += TorchT*(Torchradius+0.25-distance);}
					}
				}
			}
			if(l.getY() > heightMaxT)
				result += (heightMaxT-l.getY())/heightTDifference;
			else
				if(l.getBlock().getBiome() != Biome.SKY)
					result -= 3*(heightMaxT-l.getY())/heightTDifference;
				else
					result -= (heightMaxT-l.getY())/heightTDifference;
		}
		return result; //ЋЄд в 2 блоках, огонь в 3, а лава в 5
	}

	public double TimeTemperature() {
	    long time = getServer().getWorld("world").getTime();
	    double i = 0;
	    if(12000 < time && time < 13800) //утро
	    	i += ((double)time-12000)/1800;
	    else if(13800 <= time && time <= 22200) //ночь
	    	i += 1;
	    else if(22200 < time && time < 24000) //вечер
	    	i += (24000-(double)time)/1800;
	    return i;
	}
	
	public double getK(Player p)
	{
		if(p.getPotionEffect(PotionEffectType.FIRE_RESISTANCE) == null)
			return 1;
		else
			return FireResistanceK;
	}
	
	public double distance(double l1,double l2,double l3,double l4,double l5,double l6)
	{
		return Math.sqrt((l1-l4)*(l1-l4)+(l2-l5)*(l2-l5)+(l3-l6)*(l3-l6));
	}
	
	public int sgn(double x)
	{
		if(x == 0)
			return 0;
		else if(x > 0)
			return 1;
		else return -1;
	}
	
	public double BiomeTemperature(Biome b, int i)
	{
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
		else if(b == Biome.MUSHROOM_ISLAND) temperature = 22;
		else if(b == Biome.MUSHROOM_ISLAND_SHORE) temperature = 21;
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
		else temperature = skinTemperature; //hell 42, sky 16
	
		if(b != Biome.SKY && b != Biome.HELL && getServer().getWorld("world").hasStorm()
		&& b != Biome.DESERT && b != Biome.DESERT_HILLS && b != Biome.MUTATED_DESERT 
        && b != Biome.SAVANNA && b != Biome.SAVANNA_ROCK && b != Biome.MUTATED_SAVANNA && b != Biome.MUTATED_SAVANNA_ROCK
        && b == Biome.MESA && b == Biome.MUTATED_MESA && b == Biome.MESA_ROCK && b == Biome.MUTATED_MESA_ROCK&& b == Biome.MESA_CLEAR_ROCK && b == Biome.MUTATED_MESA_CLEAR_ROCK)
		{
			temperature -= StormTemperatureDecrease;
			if(getServer().getWorld("world").isThundering())
				temperature -= ThunderTemperatureDecrease;
		}
		if(b == Biome.SKY || b == Biome.HELL)
			return temperature;
		double TEMP = TimeTemperature(); // ==0 днЄм, >0 в остальное врем€(==1 ночью)
		if(b == Biome.DESERT || b == Biome.DESERT_HILLS || b == Biome.MUTATED_DESERT ||
		   b == Biome.SAVANNA || b == Biome.SAVANNA_ROCK || b == Biome.MUTATED_SAVANNA || b == Biome.MUTATED_SAVANNA_ROCK ||
		   b == Biome.MESA || b == Biome.MUTATED_MESA || b == Biome.MESA_ROCK || b == Biome.MUTATED_MESA_ROCK|| b == Biome.MESA_CLEAR_ROCK || b == Biome.MUTATED_MESA_CLEAR_ROCK ||
		   b == Biome.STONE_BEACH ) //"60 в месе, пустыне, саванне и каменном пл€же"
			temperature -= TEMP*(double)Difference2 + (0.5-TEMP)*RandomTDayDifference*(double)Difference2/30; //60 -> 2*0.5*20*60*x = 20 60*x=1  x = 1/60
		else
			temperature -= TEMP*(double)Difference1 + 3*(0.5-TEMP)*RandomTDayDifference*(double)Difference1/80; //20 -> 3*0.5/4*20*20*x = 7.5 3*20*20/8*x=7.5 300*x = 15 x = 1/20
		//System.out.println(temperature + " " + Insularity[i]);
		temperature = temperature*Insularity[i]+24*(1-Insularity[i]);
		return temperature;
		//if( b == Biome.BEACHES);
		/*Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.COLD_BEACH,Biome.DEEP_OCEAN,Biome.DESERT
		Biome.DESERT_HILLS,Biome.EXTREME_HILLS,Biome.EXTREME_HILLS_WITH_TREES,Biome.FOREST,Biome.FOREST_HILLS,
		Biome.FROZEN_OCEAN,Biome.FROZEN_RIVER,Biome.HELL,Biome.ICE_FLATS,Biome.ICE_MOUNTAINS,Biome.JUNGLE,
		Biome.JUNGLE_EDGE,Biome.JUNGLE_HILLS,Biome.MESA,Biome.MESA_CLEAR_ROCK,Biome.MESA_ROCK,Biome.MUSHROOM_ISLAND,
		Biome.MUSHROOM_ISLAND_SHORE,Biome.MUTATED...
		Biome.OCEAN,Biome.PLAINS,Biome.REDWOOD_TAIGA,Biome.REDWOOD_TAIGA_HILLS,Biome.RIVER,Biome.ROOFED_FOREST,
		Biome.SAVANNA,Biome.SAVANNA_ROCK,Biome.SKY,Biome.SMALLER_EXTREME_HILLS,Biome.STONE_BEACH,Biome.SWAMPLAND,
		Biome.TAIGA,Biome.TAIGA_COLD,Biome.TAIGA_COLD_HILLS,Biome.TAIGA_HILLS,Biome.VOID<<<* /
	}*/
}
