package com.lc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import net.minecraft.server.v1_12_R1.Block;

public class Config {
	//Temperature, (T)Adaptation, (T)Insularity, Thirst, ArworWeight, WaterSurface, Urandrop, Reactors, extendedSleep, day timeskip with sleep, DaySpeed, 
	//Two modes: all allowed, choose to off and all banned, choose to allow. Separately on and off all the plugin
	public static Map<String,Boolean> FunctionsON = new HashMap<>();
	static public boolean defaultLCon = false;
	public static int outputTicks = 20;
	
	static public double DIFFICULTY_LVL = 1;
	
	static int thirstTicks = 240;
	static int minSlownessThirstProcent = 50;
	static int minNauseaThirstProcent = 25;
	static double WaterRegen = (double)2/20;
	
	static double DEFAULT_SPEED = 0.1;

	static double SpeedTempDecrease = 0.02;
	static double SpeedThirstDecrease = 0.015;

	static double frostDeathT = 11;
	static double frostbite2 = 16;
	static double frostbite1 = 21;
	static double skinTemperature = 26;
	static double heatstroke1 = 31;
	static double heatstroke2 = 36;
	static double heatDeathT = 41;
	static double skinTendencyK = 1.5;
	
	static int KTime = 900;
	static int KRatio = 10;
	static double leatherColdBonus = (double)2.5/7*KTime;
	static double leatherWarmBonus = (double)2.5/7*KTime;
	static double goldColdBonus = (double)0*KTime;
	static double goldWarmBonus = (double)-1/2/7*KTime;
	static double chainmailColdBonus = (double)0*KTime;
	static double chainmailWarmBonus = (double)0*KTime;
	static double ironColdBonus = (double)-1/2/7*KTime;
	static double ironWarmBonus = (double)-1/2/7*KTime;
	static double diamondColdBonus = (double)-2/3/7*KTime;
	static double diamondWarmBonus = (double)-2/3/7*KTime;
	static double WaterWarmBonus = 2;
	static double WaterColdBonus = 1.5;
	
	static double StormTemperatureDecrease = 4;
	static double ThunderTemperatureDecrease = 2;
	
	static int Lavaradius = 5;
	static int Fireradius = 3;
	static int Iceradius = 5;
	static int Packediceradius = 2;
	static int Magmaradius = 2;
	static int Netherradius = 10;
	static int Furnaceradius = 3;
	static double Torchradius = 1.5;
	static double LavaT = (double)50/Lavaradius/2; //50
	static double FireT = (double)30/Fireradius/2; //30
	static double IceT  = (double)-20/Iceradius/2;
	static double PackedIceT  = (double)-20/Packediceradius/2;
	static double MagmaT = (double)10/Magmaradius/2;
	static double NetherT = (double)0.1/Netherradius;
	static double FurnaceT = (double)8/Furnaceradius;
	static double TorchT = (double)3/Torchradius;
	
	static double TBlocksK = 9;
	static double maxR = 6;
	
	static double FireResistanceK = (double)1/4;
	
	static int Difference1 = 10; //20
	static int Difference2 = 30; //60
	
	static int heightMaxT = 63;
	static int heightTDifference = 20;
	
	static int RandomMaxTicks = 300;
	static double RandomTDifference = 2;

	static double WeightLimit1 = 7;
	static double WeightLimit2 = 10;
	static double WeightLimit3 = 14;
	static double WeightLimit4 = 21;
	
	static double hungerPower = 0.1;
	
	static boolean isBuffing = true;
	static boolean isDebuffing = true;
	static double GoodHealthSpeedBonus = 1.1;
	static double GoodHealthTickRegen = 0.01;
	static double GoodHealthBonusJumpVelocity = 0.1;
	static double BuffUpperLimitT = 30;
	static double BuffBottomLimitT = 22;
	static double BuffLimitThirst = 60;

	static double BotAdaptationLimit = 16;
	static double TopAdaptationLimit = 36;
	
	static TemperatureBlock[] Blocks;
	
	static double VELOCITY = 0.01;
	static double timeK = 1;
	static int RAND_ALL_CHANCES = 100000;
	static double uranRand1 = RAND_ALL_CHANCES*0.5/100;
	static double uranRand2 = RAND_ALL_CHANCES*0.1/100;
	static double uranRand3 = RAND_ALL_CHANCES*0.05/100;

	public static void loadConfig(MemoryConfiguration c, mainListener ml)
	{
		c.addDefault("LC-ON", true);
		c.addDefault("default-LC-on-players", true);
		c.addDefault("fun-on-Temperature", false);
		c.addDefault("fun-on-Adaptation", true);
		c.addDefault("fun-on-Insularity", true);
		c.addDefault("fun-on-Thirst", false);
		c.addDefault("fun-on-ArmorWeight", false);
		c.addDefault("fun-on-WaterSurface", true);
		c.addDefault("fun-on-Urandrop", true);
		c.addDefault("fun-on-Reactors", false);
		c.addDefault("fun-on-DaySpeed", false);
		c.addDefault("outputTicks", 10);
		c.addDefault("difficultyLvl", 1);
		c.addDefault("isbuffing", true);
		c.addDefault("isdebuffing", true);
		c.addDefault("skinTendencyK", 1.5);
		c.addDefault("thirstTicks", 240);
		c.addDefault("minSlownessThirstProcent", 50);
		c.addDefault("minNauseaThirstProcent", 25);
		c.addDefault("waterRegen", 0.1);
		c.addDefault("defaultSpeed", 0.1);
		c.addDefault("speedTempDecrease", 20);
		c.addDefault("speedThirstDecrease", 15);
		c.addDefault("frostbiteDeathTemp", -20);
		c.addDefault("frostbiteTempLvl2", -15);
		c.addDefault("frostbiteTempLvl1", -10);
		c.addDefault("skinDefaultTemperature", 26);
		c.addDefault("heatstrokeTempLvl1", 10);
		c.addDefault("heatstrokeTempLvl2", 15);
		c.addDefault("heatstrokeDeathTemp", 20);
		c.addDefault("KTime", 900);
		c.addDefault("KRatio", 10);
		c.addDefault("leatherColdBonus", 2.5);
		c.addDefault("leatherWarmBonus", 2.5);
		c.addDefault("goldColdBonus", 0);
		c.addDefault("goldWarmBonus", -2);
		c.addDefault("chainmailColdBonus", 0);
		c.addDefault("chainmailWarmBonus", 0);
		c.addDefault("ironColdBonus", -2);
		c.addDefault("ironWarmBonus", -2);
		c.addDefault("diamondColdBonus", -3);
		c.addDefault("diamondWarmBonus", -3);
		c.addDefault("waterWarmBonus", 2);
		c.addDefault("waterColdBonus", -1.5);
		c.addDefault("stormTemperatureDecrease", 4);
		c.addDefault("thunderTemperatureDecrease", 2);
		/*c.addDefault("lavaradius", 6);
		c.addDefault("fireradius", 4);
		c.addDefault("iceradius", 5);
		c.addDefault("packediceradius", 3.5);
		c.addDefault("magmaradius", 4);
		c.addDefault("netherradius", 10);
		c.addDefault("furnaceradius", 4);
		c.addDefault("torchradius", 3);
		c.addDefault("lavaT", 900);
		c.addDefault("fireT", 100);
		c.addDefault("iceT", -70);
		c.addDefault("packedIceT", -90);
		c.addDefault("magmaT", 70);
		c.addDefault("netherT", 0.1);
		c.addDefault("furnaceT", 50);
		c.addDefault("torchT", 20);*/
		c.addDefault("fireResistanceK", 11);
		c.addDefault("differenceType1", 10);
		c.addDefault("differenceType2", 30);
		c.addDefault("heightMaxT", 63);
		c.addDefault("heightTDifference", 20);
		c.addDefault("randomMaxTicks", 300);
		c.addDefault("randomTDifference", 2);
		c.addDefault("weightLimit1", 7);
		c.addDefault("weightLimit2", 10);
		c.addDefault("weightLimit3", 14);
		c.addDefault("weightLimit4", 21);
		c.addDefault("hungerPower", 0.05);
		c.addDefault("goodHealthSpeedBonus", 10);
		c.addDefault("goodHealthRegenPerSec",0.2);
		c.addDefault("goodHealthBonusJumpVelocity",0.1);
		c.addDefault("buffUpperLimitT",30);
		c.addDefault("buffBottomLimitT",22);
		c.addDefault("buffLimitThirst",60);
		c.addDefault("BotAdaptationLimit",-10);
		c.addDefault("TopAdaptationLimit",10);
		
		c.addDefault("waterVelocity", 0.05);
		c.addDefault("uraniumChanceLvl1", 0.5);
		c.addDefault("uraniumChanceLvl2", 0.1);
		c.addDefault("uraniumChanceLvl3", 0.05);
		c.addDefault("timeK", 1);
		c.options().copyDefaults(true);
		
		ml.saveConfig();

		ml.LCon = c.getBoolean("LC-ON");
		Config.defaultLCon = c.getBoolean("default-LC-on-players");
		Config.FunctionsON.put("Temperature", c.getBoolean("fun-on-Temperature"));
		Config.FunctionsON.put("Adaptation", c.getBoolean("fun-on-Adaptation"));
		Config.FunctionsON.put("Insularity", c.getBoolean("fun-on-Insularity"));
		Config.FunctionsON.put("Thirst", c.getBoolean("fun-on-Thirst"));
		Config.FunctionsON.put("ArmorWeight", c.getBoolean("fun-on-ArmorWeight"));
		Config.FunctionsON.put("WaterSurface", c.getBoolean("fun-on-WaterSurface"));
		Config.FunctionsON.put("Urandrop", c.getBoolean("fun-on-Urandrop"));
		Config.FunctionsON.put("Reactors", c.getBoolean("fun-on-Reactors"));
		Config.FunctionsON.put("DaySpeed", c.getBoolean("fun-on-DaySpeed"));
		Config.outputTicks = c.getInt("outputTicks");
		Config.DIFFICULTY_LVL = c.getDouble("difficultyLvl");
		Config.isBuffing = c.getBoolean("isbuffing");
		Config.isDebuffing = c.getBoolean("isdebuffing");
		Config.skinTendencyK = c.getDouble("skinTendencyK");
		Config.thirstTicks = c.getInt("thirstTicks");
		Config.minSlownessThirstProcent = c.getInt("minSlownessThirstProcent");
		Config.minNauseaThirstProcent = c.getInt("minNauseaThirstProcent");
		Config.WaterRegen  = c.getDouble("waterRegen");
		Config.DEFAULT_SPEED = c.getDouble("defaultSpeed");
		Config.SpeedTempDecrease  = 1-c.getDouble("speedTempDecrease")/100;
		Config.SpeedThirstDecrease  = 1-c.getDouble("speedThirstDecrease")/100;
		Config.skinTemperature = c.getDouble("skinDefaultTemperature");
		ml.Tbody = Config.skinTemperature;
		Config.TopAdaptationLimit = Config.skinTemperature + c.getDouble("TopAdaptationLimit");
		Config.BotAdaptationLimit = Config.skinTemperature + c.getDouble("BotAdaptationLimit");
		Config.frostDeathT = c.getDouble("frostbiteDeathTemp");
		Config.frostbite2  = c.getDouble("frostbiteTempLvl2");
		Config.frostbite1  = c.getDouble("frostbiteTempLvl1");
		Config.heatstroke1 = c.getDouble("heatstrokeTempLvl1");
		Config.heatstroke2 = c.getDouble("heatstrokeTempLvl2");
		Config.heatDeathT  = c.getDouble("heatstrokeDeathTemp");
		Config.KTime  = c.getInt("KTime");
		Config.KRatio = c.getInt("KRatio");
		Config.leatherColdBonus  = c.getDouble("leatherColdBonus");
			if(Config.leatherColdBonus<0) Config.leatherColdBonus = -(Config.leatherColdBonus+1)/Config.leatherColdBonus;
			Config.leatherColdBonus = Config.leatherColdBonus/7*Config.KTime;
		Config.leatherWarmBonus  = c.getDouble("leatherWarmBonus");
			if(Config.leatherWarmBonus<0) Config.leatherWarmBonus = -(Config.leatherWarmBonus+1)/Config.leatherWarmBonus;
			Config.leatherWarmBonus = Config.leatherWarmBonus/7*Config.KTime;
		Config.goldColdBonus  = c.getDouble("goldColdBonus");
			if(Config.goldColdBonus<0) Config.goldColdBonus = -(Config.goldColdBonus+1)/Config.goldColdBonus;
			Config.goldColdBonus = Config.goldColdBonus/7*Config.KTime;
		Config.goldWarmBonus  = c.getDouble("goldWarmBonus");
			if(Config.goldWarmBonus<0) Config.goldWarmBonus = -(Config.goldWarmBonus+1)/Config.goldWarmBonus;
			Config.goldWarmBonus = Config.goldWarmBonus/7*Config.KTime;
		Config.chainmailColdBonus = c.getDouble("chainmailColdBonus");
			if(Config.chainmailColdBonus<0) Config.chainmailColdBonus = -(Config.chainmailColdBonus+1)/Config.chainmailColdBonus;
			Config.chainmailColdBonus = Config.chainmailColdBonus/7*Config.KTime;
		Config.chainmailWarmBonus = c.getDouble("chainmailWarmBonus");
			if(Config.chainmailWarmBonus<0) Config.chainmailWarmBonus = -(Config.chainmailWarmBonus+1)/Config.chainmailWarmBonus;
			Config.chainmailWarmBonus = Config.chainmailWarmBonus/7*Config.KTime;
		Config.ironColdBonus = c.getDouble("ironColdBonus");
			if(Config.ironColdBonus<0) Config.ironColdBonus = -(Config.ironColdBonus+1)/Config.ironColdBonus;
			Config.ironColdBonus = Config.ironColdBonus/7*Config.KTime;
		Config.ironWarmBonus = c.getDouble("ironWarmBonus");
			if(Config.ironWarmBonus<0) Config.ironWarmBonus = -(Config.ironWarmBonus+1)/Config.ironWarmBonus;
			Config.ironWarmBonus = Config.ironWarmBonus/7*Config.KTime;
		Config.diamondColdBonus = c.getDouble("diamondColdBonus");
			if(Config.diamondColdBonus<0) Config.diamondColdBonus = -(Config.diamondColdBonus+1)/Config.diamondColdBonus;
			Config.diamondColdBonus = Config.diamondColdBonus/7*Config.KTime;
		Config.diamondWarmBonus = c.getDouble("diamondWarmBonus");
			if(Config.diamondWarmBonus<0) Config.diamondWarmBonus = -(Config.diamondWarmBonus+1)/Config.diamondWarmBonus;
			Config.diamondWarmBonus = Config.diamondWarmBonus/7*Config.KTime;
		Config.WaterWarmBonus = c.getDouble("waterWarmBonus");
			if(Config.WaterWarmBonus < 0) Config.WaterWarmBonus = -1/Config.WaterWarmBonus;
		Config.WaterColdBonus = c.getDouble("waterColdBonus");
			if(Config.WaterColdBonus < 0) Config.WaterColdBonus = -1/Config.WaterColdBonus;
		Config.StormTemperatureDecrease = c.getDouble("stormTemperatureDecrease");
		Config.ThunderTemperatureDecrease = c.getDouble("thunderTemperatureDecrease");
		
		Config.FireResistanceK = 1/c.getDouble("fireResistanceK");
		Config.Difference1 = c.getInt("differenceType1");
		Config.Difference2 = c.getInt("differenceType2");
		Config.heightMaxT = c.getInt("heightMaxT");
		Config.heightTDifference = c.getInt("heightTDifference");
		Config.RandomMaxTicks = c.getInt("randomMaxTicks");
		Config.RandomTDifference = c.getDouble("randomTDifference");
		Config.WeightLimit1 = c.getDouble("weightLimit1");
		Config.WeightLimit2 = c.getDouble("weightLimit2");
		Config.WeightLimit3 = c.getDouble("weightLimit3");
		Config.WeightLimit4 = c.getDouble("weightLimit4");
		Config.hungerPower = c.getDouble("hungerPower");
		Config.GoodHealthSpeedBonus = 1+c.getDouble("goodHealthSpeedBonus")/100.0;
		Config.GoodHealthTickRegen = c.getDouble("goodHealthRegenPerSec")/20;
		Config.GoodHealthBonusJumpVelocity = c.getDouble("goodHealthBonusJumpVelocity");
		Config.BuffUpperLimitT = c.getDouble("buffUpperLimitT");
		Config.BuffBottomLimitT = c.getDouble("buffBottomLimitT");
		Config.BuffLimitThirst = c.getDouble("buffLimitThirst");
		
		
		//ArmorTempK
		Config.WaterRegen = Config.WaterRegen/Config.DIFFICULTY_LVL;
		Config.NetherT = Config.NetherT*Config.DIFFICULTY_LVL;
		Config.skinTendencyK = 1+(Config.skinTendencyK-1)/Config.DIFFICULTY_LVL;
		Config.KTime = (int)Math.round(Config.KTime/Config.DIFFICULTY_LVL);
		Config.heightTDifference = (int)Math.round(Config.heightTDifference/Config.DIFFICULTY_LVL);
		Config.hungerPower = Config.hungerPower*Config.DIFFICULTY_LVL;

		Config.loadBlocks();
		int N = Config.Blocks.length;
		ml.functions.TBlocks_Sum = new double[N];
		ml.functions.TBlocks_Num = new double[N];
		ml.functions.TBlocks_dT = new double[N];
		/*Config.Blocks[0] = new TemperatureBlock(Material.LAVA,ñ.getDouble("lavaradius"),ñ.getDouble("lavaT")/2, true);
		Config.Blocks[1] = new TemperatureBlock(Material.STATIONARY_LAVA,ñ.getDouble("lavaradius"),ñ.getDouble("lavaT"), true);
		Config.Blocks[2] = new TemperatureBlock(Material.FIRE,ñ.getDouble("fireradius"),ñ.getDouble("fireT"), true);
		Config.Blocks[3] = new TemperatureBlock(Material.ICE,ñ.getDouble("iceradius"),ñ.getDouble("iceT"));
		Config.Blocks[4] = new TemperatureBlock(Material.PACKED_ICE,ñ.getDouble("packediceradius"),ñ.getDouble("packedIceT"));
		Config.Blocks[5] = new TemperatureBlock(Material.MAGMA,ñ.getDouble("magmaradius"),ñ.getDouble("magmaT"), true);
		Config.Blocks[6] = new TemperatureBlock(Material.BURNING_FURNACE,ñ.getDouble("furnaceradius"),ñ.getDouble("furnaceT"));
		Config.Blocks[7] = new TemperatureBlock(Material.TORCH,ñ.getDouble("torchradius"),ñ.getDouble("torchT"));
		Config.Blocks[8] = new TemperatureBlock(Material.NETHERRACK,ñ.getDouble("netherradius"),ñ.getDouble("netherT"), true);
		*/Config.sortBlocks();


		Config.VELOCITY = c.getDouble("waterVelocity");
		Config.uranRand1 = Config.RAND_ALL_CHANCES*c.getDouble("uraniumChanceLvl1")/100;
		Config.uranRand2 = Config.RAND_ALL_CHANCES*c.getDouble("uraniumChanceLvl2")/100+Config.uranRand1;
		Config.uranRand3 = Config.RAND_ALL_CHANCES*c.getDouble("uraniumChanceLvl3")/100+Config.uranRand2;
		Config.timeK = c.getDouble("timeK");
		
		System.out.println("[LifeCompromise] Config Reloaded.");
	}
	
	public static void saveConfig(MemoryConfiguration c, mainListener ml)
	{
		c.set("LC-ON", ml.LCon);
		c.set("fun-on-Temperature", Config.FunctionsON.get("Temperature"));
		c.set("fun-on-Adaptation", Config.FunctionsON.get("Adaptation"));
		c.set("fun-on-Insularity", Config.FunctionsON.get("Insularity"));
		c.set("fun-on-Thirst", Config.FunctionsON.get("Thirst"));
		c.set("fun-on-ArmorWeight", Config.FunctionsON.get("ArmorWeight"));
		c.set("fun-on-WaterSurface", Config.FunctionsON.get("WaterSurface"));
		c.set("fun-on-Urandrop", Config.FunctionsON.get("Urandrop"));
		c.set("fun-on-Reactors", Config.FunctionsON.get("Reactors"));
		c.set("fun-on-DaySpeed", Config.FunctionsON.get("DaySpeed"));
		c.set("outputTicks", Config.outputTicks);
		c.set("difficultyLvl", Config.DIFFICULTY_LVL);
		c.set("isbuffing", Config.isBuffing);
		c.set("isdebuffing", Config.isDebuffing);
		//Config.thirstTicks = c.getInt("thirstTicks");
		//Config.skinTemperature = c.getDouble("skinDefaultTemperature");
		//ml.Tbody = Config.skinTemperature;
		
		Config.VELOCITY = c.getDouble("waterVelocity");
		Config.uranRand1 = Config.RAND_ALL_CHANCES*c.getDouble("uraniumChanceLvl1")/100;
		Config.uranRand2 = Config.RAND_ALL_CHANCES*c.getDouble("uraniumChanceLvl2")/100+Config.uranRand1;
		Config.uranRand3 = Config.RAND_ALL_CHANCES*c.getDouble("uraniumChanceLvl3")/100+Config.uranRand2;
		Config.timeK = c.getDouble("timeK");
		
		ml.saveConfig();
		
		System.out.println("[LifeCompromise] Config successfully saved.");
	}
	
	public static void setLCon(mainListener ml, boolean lcon) {
		ml.LCon = lcon;
		saveConfig(ml.getConfig(), ml);
	}
	
	public static void sortBlocks() {
		if(Blocks.length > 1)
			Arrays.sort(Blocks, (a, b) -> Double.compare(a.T,b.T));
	}

	public static void loadBlocks() {
		File file = new File("plugins/LifeCompromise/temperature-blocks.txt");
	    if(!file.exists()){
			System.out.println("There are not \"plugins/LifeCompromise/temperature-blocks.txt\"!");
	    	return;
	    }
	    try {
	        FileReader fr = new FileReader(file);
	        int c;
	        c = fr.read();
	        List<TemperatureBlock> blocklist = new ArrayList<>();
	        boolean isKey = true;
	        String currentKey = "";
	        String currentValue = "";
	        String id="", T="", R="", F="";
	        while(c != -1){
	        	if(c == ':'){
	        		if(!isKey){
	        			id = currentValue.substring(0, currentValue.length()-1);
	        			currentKey = currentValue.substring(currentValue.length()-1,currentValue.length());
	        			currentValue = "";
	        		}
	        		isKey = false;
	        	}
	        	else if(c == '['){
	    	        currentKey = "";
	    	        currentValue = "";
	    	        id = "";
	    	        R = "";
	    	        T = "";
	    	        F = "";
	        	}
	        	else if(c == ']'){
    				if(currentKey.equalsIgnoreCase("id")) id = currentValue;
    				else if(currentKey.equalsIgnoreCase("T")) T = currentValue;
    				else if(currentKey.equalsIgnoreCase("R")) R = currentValue;
    				else if(currentKey.equalsIgnoreCase("F")) F = currentValue;
    				Material m = getMaterial(id);
	        		if(m!=null && R != "" && T != "")
	        		{
	        			for(int i = 0; i<blocklist.size(); i++)  //REPEATS
	        				if(blocklist.get(i).id == m) {
	        					c = fr.read();
	        					continue;
	        				}
	        			if(T.charAt(T.length()-1) == '.') T+="0";
	        			if(R.charAt(R.length()-1) == '.') R+="0";
	        			try {
		    				if(F != "" && (F.equalsIgnoreCase("true") || F.equals("1")))
			        			blocklist.add(new TemperatureBlock(m, Double.parseDouble(R), Double.parseDouble(T), true) );
		    				else
			        			blocklist.add(new TemperatureBlock(m, Double.parseDouble(R), Double.parseDouble(T)) );
	        			} catch(Exception e) {
		        			System.out.println("[WARNING] TemperatureBlock error on block with id "+id+"! Double parsing exception.");
	        			}
	        		}
	        		else
	        			System.out.println("[WARNING] TemperatureBlock error on block with id "+id+"!");
	        	}
	        	else if(c == ' ') ;
	        	else if(isKey){
	        		currentKey += (char)c;
	        	}
	        	else{
        			if(currentKey.equalsIgnoreCase("F"))
        			{
        				if(c != 'R' && c != 'T' && c != 'i')
    	        			currentValue += (char)c;
        				else
	        			{
        					F = currentValue;
	    	        		isKey = true;
	        				currentKey = ""+(char)c;
	        				currentValue = "";
	    	        		c = fr.read();
	    	        		continue;
	        			}
        			}
        			else if(currentKey.equalsIgnoreCase("T") || currentKey.equalsIgnoreCase("R")) 
	        		{
	        			if((c>='a' && c<='z') || (c>='A' && c<='Z'))
	        			{
	        				if(currentKey.equalsIgnoreCase("T")) T = currentValue;
	        				if(currentKey.equalsIgnoreCase("R")) R = currentValue;
	    	        		isKey = true;
	        				currentKey = ""+(char)c;
	        				currentValue = "";
	    	        		c = fr.read();
	    	        		continue;
	        			}
	        			if(c == ',') currentValue += ".";
	        			else currentValue += (char)c;
	        		}
	        		else currentValue += (char)c;
	        	}
	        	c = fr.read();
	        }
    		Blocks = blocklist.toArray(new TemperatureBlock[0]);
    		Config.maxR = 0;
    		for(int i=0; i<Blocks.length; i++)
    			if(Blocks[i].R > Config.maxR) Config.maxR = Blocks[i].R;
	        fr.close();
			return;
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	private static Material getMaterial(String a)
	{
		a = a.toLowerCase();
		Material m = null;
		if(a.contains("stone") || a.equals("1") || a.equals("1:0")) m = Material.STONE;
		else if(a.contains("grass") || a.equals("2") || a.equals("2:0")) m = Material.GRASS;
		else if(a.contains("dirt") || a.equals("3") || a.equals("3:0")) m = Material.DIRT;
		else if(a.contains("cobblestone") || a.equals("4") || a.equals("4:0")) m = Material.COBBLESTONE;
		else if(a.contains("planks") || a.equals("5") || a.equals("5:0")) m = Material.WOOD;
		else if(a.contains("bedrock") || a.equals("7") || a.equals("7:0")) m = Material.BEDROCK;
		
		else if(a.contains("flowing") || a.equals("10") || a.equals("10:0")) m = Material.LAVA;
		else if(a.contains("lava") || a.equals("11") || a.equals("11:0")) m = Material.STATIONARY_LAVA;
		else if(a.contains("torch") || a.equals("50") || a.equals("50:0")) m = Material.TORCH;
		else if(a.contains("fire") || a.equals("51") || a.equals("51:0")) m = Material.FIRE;
		else if(a.contains("lit_furnace") || a.equals("62") || a.equals("62:0")) m = Material.BURNING_FURNACE;
		//else if(a.contains("layer") || a.equals("78") || a.equals("78:0")) m = Material.SNOW;
		else if(a.contains("ice") || a.equals("79") || a.equals("79:0")) m = Material.ICE;
		//else if(a.contains("snow") || a.equals("81") || a.equals("81:0")) m = Material.SNOW_BLOCK;
		else if(a.contains("netherrack") || a.equals("87") || a.equals("87:0")) m = Material.NETHERRACK;
		//else if(a.contains("glowstone") || a.equals("89") || a.equals("89:0")) m = Material.GLOWSTONE;
		else if(a.contains("packed") || a.equals("174") || a.equals("174:0")) m = Material.PACKED_ICE;
		//else if(a.contains("frosted") || a.equals("212") || a.equals("212:0")) m = Material.FROSTED_ICE;
		else if(a.contains("magma") || a.equals("213") || a.equals("213:0")) m = Material.MAGMA;
		/*Config.Blocks[0] = new TemperatureBlock(Material.LAVA,ñ.getDouble("lavaradius"),ñ.getDouble("lavaT")/2, true);
		Config.Blocks[1] = new TemperatureBlock(Material.STATIONARY_LAVA,ñ.getDouble("lavaradius"),ñ.getDouble("lavaT"), true);
		Config.Blocks[2] = new TemperatureBlock(Material.FIRE,ñ.getDouble("fireradius"),ñ.getDouble("fireT"), true);
		Config.Blocks[3] = new TemperatureBlock(Material.ICE,ñ.getDouble("iceradius"),ñ.getDouble("iceT"));
		Config.Blocks[4] = new TemperatureBlock(Material.PACKED_ICE,ñ.getDouble("packediceradius"),ñ.getDouble("packedIceT"));
		Config.Blocks[5] = new TemperatureBlock(Material.MAGMA,ñ.getDouble("magmaradius"),ñ.getDouble("magmaT"), true);
		Config.Blocks[6] = new TemperatureBlock(Material.BURNING_FURNACE,ñ.getDouble("furnaceradius"),ñ.getDouble("furnaceT"));
		Config.Blocks[7] = new TemperatureBlock(Material.TORCH,ñ.getDouble("torchradius"),ñ.getDouble("torchT"));
		Config.Blocks[8] = new TemperatureBlock(Material.NETHERRACK,ñ.getDouble("netherradius"),ñ.getDouble("netherT"), true);*/
		return m;
		
	}
}
