package com.lc;

import org.bukkit.Location;
import org.bukkit.Material;

public class UtilsThirst {

	public static void tick(LCPlayer lcp) {
		
	}
	
	/*
	if(this.Thirst>Config.minSlownessThirstProcent) this.ThirstDebuff=0;
	else if(this.Thirst>Config.minNauseaThirstProcent) this.ThirstDebuff=1;
	else if(this.Thirst > 0) this.ThirstDebuff=2;
	else this.ThirstDebuff=3;
	
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
			if(l.getBlock().getType() == Material.STATIONARY_WATER/* && l.getBlock().getMetadata("") == * /) ////////
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
	}*/
}
