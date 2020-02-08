package com.lc;

import java.util.UUID;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LCPlayer {
	public UUID Tid;
	public boolean isOnLC;
	
	public Double NormalT;
	public Double T;
	public Double Thirst;
	public int AdditionalDataMain = -1;
	public int AdditionalDataOff = -1;
	public boolean interacted = false;
	public double Y;
	public double dY;
	public double Insularity;
	public boolean Logged;
	public byte Unbed;
	public double dTair;
	private double dTnormal;
	private int unwellTime;
	public byte TDebuff;
	public byte ThirstDebuff;
	private double Taround;
	public double BiomeTemperature;
	public double BlocksTemperature;
	public double EvilTemperature;
	Player p;
	public int maxOutputTicks = Config.outputTicks;
	public int outputTicks = Config.outputTicks;
	public double ArmorWeight;

	public double Fat;
	public double Muscles;
	public double Sleepiness;
	public boolean isSleeping;
	public double SwordMastery;
	
	public LCPlayer()
	{
		Tid = null;
		isOnLC = Config.defaultLCon;
		T = null;
		Thirst = null;
		NormalT = null;
		Unbed = 0;
		TDebuff = 0;
		ThirstDebuff = 0;
		unwellTime = 0;
	}
	
	public void PlayerHandling()
	{
		long t1 = System.nanoTime();
		if(Config.FunctionsON.get("Temperature")) {
			AlterTemperature();
		}
		mainListener.ExecutionAllTime[2] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[2] +=1;

		t1 = System.nanoTime();
		if(Config.FunctionsON.get("Thirst")) {
			AlterThirst();
		}
		mainListener.ExecutionAllTime[3] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[3] +=1;
		//...
	}

	public void DataTest() //check hands, set temp and thirst debuffs, calc armorweight 
	{
		long t1 = System.nanoTime();

		  if(!p.isDead()) //getServer().getPlayer(this.Tid).
		  {
			interacted = false;
			ItemStack IS = p.getInventory().getItemInMainHand();
			if(IS.getType() == Material.POTION)
				this.AdditionalDataMain = IS.getData().getData();//.getDurability();
			else if(IS.getType() == Material.AIR)
				this.AdditionalDataMain = -1;
			else if(IS.getType() == Material.GLASS_BOTTLE)
				this.AdditionalDataMain = -2;
			else if(IS.getType() == Material.WATER_BUCKET)
				this.AdditionalDataMain = -3;
			else if(IS.getType() == Material.MILK_BUCKET)
				this.AdditionalDataMain = -4;
			else
				this.AdditionalDataMain = -1000;
			IS = p.getInventory().getItemInOffHand();
			if(IS.getType() == Material.POTION)
				this.AdditionalDataOff = IS.getData().getData();//.getDurability();
			else if(IS.getType() == Material.AIR)
				this.AdditionalDataOff = -1;
			else if(IS.getType() == Material.GLASS_BOTTLE)
				this.AdditionalDataOff = -2;
			else if(IS.getType() == Material.WATER_BUCKET)
				this.AdditionalDataOff = -3;
			else if(IS.getType() == Material.MILK_BUCKET)
				this.AdditionalDataOff = -4;
			else
				this.AdditionalDataOff = -1000;
			if(this.Thirst>100) this.Thirst=100.0;
			if(this.Thirst<0) this.Thirst=0.0;

			if(dTnormal>Config.heatDeathT) this.TDebuff=3;
			else if(dTnormal<Config.frostDeathT) this.TDebuff=-3;
			else if(dTnormal>Config.heatstroke2) this.TDebuff=2;
			else if(dTnormal<Config.frostbite2) this.TDebuff=-2;
			else if(dTnormal>Config.heatstroke1) this.TDebuff=1;
			else if(dTnormal<Config.frostbite1) this.TDebuff=-1;
			else {this.TDebuff=0; this.unwellTime = 0;}
			
			if(this.Thirst>Config.minSlownessThirstProcent) this.ThirstDebuff=0;
			else if(this.Thirst>Config.minNauseaThirstProcent) this.ThirstDebuff=1;
			else if(this.Thirst > 0) this.ThirstDebuff=2;
			else this.ThirstDebuff=3;
			
			ArmorWeight = Config.FunctionsON.get("ArmorWeight") ? TempandArmorFunctions.getArmorWeight(p) : -1;
		  }
		mainListener.ExecutionAllTime[4] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[4] +=1;
	}
	
	public void Output(Player p)
	{
		long t1 = System.nanoTime();

		if(isOnLC)
		if(this.Unbed == 0)
		{
			//IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"ЖАЖДА!\"}");
			
			String str1 = "T:" + Double.toString((double)Math.round(this.T*100)/100); //Т:26.25|, Ж:100.0
			while(str1.length() < 7) str1 += ' '; //7 (18)
			switch(TDebuff)
			{
			case -2: str1 += "\",\"color\":\"blue\"},{\"text\": \""; break;
			case -1: str1 += "\",\"color\":\"aqua\"},{\"text\": \""; break;
			case 0: str1 += "\",\"color\":\"white\"},{\"text\": \""; break;
			case 1: str1 += "\",\"color\":\"gold\"},{\"text\": \""; break;
			case 2: str1 += "\",\"color\":\"red\"},{\"text\": \""; break;
			}
			if(dTair > 0)       str1 += "↓";
			else if(dTair == 0) str1 += "•"; //1 (46-48)
			else             str1 += "↑";
			String str2 = "G:" + Double.toString((double)Math.round(this.Thirst*100)/100);
			while(str2.length() < 9) str2 += ' '; //9 (55-57)
			switch(ThirstDebuff)
			{
			case 0: str2 += "\",\"color\":\"white\"}]"; break;
			case 1: str2 += "\",\"color\":\"gold\"}]"; break;
			case 2: str2 += "\",\"color\":\"red\"}]"; break;
			case 3: str2 += "\",\"color\":\"black\"}]"; break;
			}
			IChatBaseComponent chatTitle = ChatSerializer.a("[{\"text\": \"" + str1 + ", \",\"color\":\"white\"},{\"text\": \"" + str2); //"\",\"color\":\"white\"}]");
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, chatTitle, 5, 5, 5); //11+31=42 (114-118)
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
		
		}
		else
			this.Unbed -= 1;
		
		mainListener.ExecutionAllTime[5] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[5] +=1;
	}
	
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
	
	@SuppressWarnings("deprecation")
	public void AlterThirst()
	{
		this.Thirst -= (double)1/Config.thirstTicks;
		if(dTnormal < Config.frostbite1 || dTnormal > Config.heatstroke1)
		{
			this.Thirst -= (double)1/Config.thirstTicks/2;
			if(dTnormal < Config.frostbite2 || dTnormal > Config.heatstroke2)
				this.Thirst -= (double)1/Config.thirstTicks/2;
		}
		double regen = 0;
		Location l = p.getLocation();
		if(p.isSneaking())
			if(l.getBlock().getType() == Material.STATIONARY_WATER/* && l.getBlock().getMetadata("") == */) ////////
				regen += Config.WaterRegen;
			else if(l.getBlock().getType() == Material.CAULDRON && l.getBlock().getState().getData().getData() != 0)
				regen += Config.WaterRegen*l.getBlock().getState().getData().getData()/3;
		l.setY(l.getY()+0.99);
		if(l.getBlock().getType() == Material.STATIONARY_WATER) ////////
			regen += Config.WaterRegen;
		else if(l.getBlock().getType() == Material.CAULDRON && l.getBlock().getState().getData().getData() != 0)
			regen += Config.WaterRegen*l.getBlock().getState().getData().getData()/3;
		this.Thirst += Math.min(Config.WaterRegen,regen);
		
		if(this.Thirst > 100) this.Thirst = 100.0;
		else if(this.Thirst < 0) this.Thirst = 0.0;
	}
	
	public void TemperatureAdaptation()
	{
		long t1 = System.nanoTime();

		switch(this.TDebuff)
		{
		case -2: this.unwellTime+=2; break;
		case -1: this.unwellTime+=1; break;
		case 1: this.unwellTime+=1; break;
		case 2: this.unwellTime+=2; break;
		}
		if(unwellTime >= 100)
		{
			unwellTime-=100;
			NormalT = (double)Math.round(NormalT*100+TDebuff)/100;
			if(NormalT > Config.TopAdaptationLimit) NormalT = Config.TopAdaptationLimit;
			else if(NormalT < Config.BotAdaptationLimit) NormalT = Config.BotAdaptationLimit;
		}

		mainListener.ExecutionAllTime[6] += System.nanoTime()-t1;
		mainListener.ExecutionTimes[6] +=1;
	}
	
	public double getAirT()
	{
		return this.Taround;
	}
	
	public void setAirT(double t)
	{
		this.Taround = t;
	}
	
	public void setPlayer(Player p)
	{
		this.p = p;
	}

	public void normalize() {
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.DEFAULT_SPEED);
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
		if(p.getPotionEffect(PotionEffectType.SLOW_DIGGING) != null && p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getDuration() >= 1000 )
			p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		if(p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) != null && p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() >= 1000)
			p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		if(p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null && (p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() >= 3000))
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		if(p.getPotionEffect(PotionEffectType.HUNGER) != null && p.getPotionEffect(PotionEffectType.HUNGER).getDuration() >= 10000)
			p.removePotionEffect(PotionEffectType.HUNGER);
		outputTicks = maxOutputTicks;
	}
}
