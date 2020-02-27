package com.lc.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lc.LCPlayer;
import com.lc.config.Config;
import com.lc.config.Config.Key;

public class UtilsThirst {
	
	public static final double MIN_THIRST = 0, MAX_THIRST = 100;

	Config config;
	
	public UtilsThirst(Config config) {
		this.config = config;
	}

	public void tick(LCPlayer lcp)
	{
		int thirst_ticks = config.get(Key.THIRST_TICKS);
		double water_left = -1d / thirst_ticks;
		
		double dThirst = water_left;
		
		// is this debuffing?
		if (lcp.getTemperatureDebuff().getDebuffLvl() > 0)
		{
			dThirst += water_left / 2;
			if (lcp.getTemperatureDebuff().getDebuffLvl() > 1)
				dThirst += water_left / 2;
		}
		
		double water_regen = config.get(Key.WATER_REGEN);
		
		Player p = lcp.getPlayer();
		Location l = p.getLocation();
		Block b = l.getBlock();
		if (p.isSneaking())
			if (Utils.isStationaryWater(b))
				dThirst += water_regen;
			else if (Utils.getCauldronLevel(b) > 0)
				dThirst += water_regen * Utils.getCauldronLevel(b);
		
		l.setY(l.getY() + 0.99);
		b = l.getBlock();
		if (Utils.isStationaryWater(b))
			dThirst += water_regen;
		else if (Utils.getCauldronLevel(b) > 0)
			dThirst += water_regen * Utils.getCauldronLevel(b);
		
		dThirst = Math.min(water_regen, dThirst);
		
		lcp.addThirst(dThirst);
	}
	
	public ThirstDebuffLevel getDebuffLevel(double thirst) {
		double pct = config.get(Key.THIRST_SLOWNESS_PERCENT);
		if (thirst > pct) return ThirstDebuffLevel.NONE;
		pct = config.get(Key.THIRST_NAUSEA_PERCENT);
		if (thirst > pct) return ThirstDebuffLevel.DEBUFF_1;
		pct = MIN_THIRST;
		if (thirst > pct) return ThirstDebuffLevel.DEBUFF_2;
		return ThirstDebuffLevel.DEBUFF_3;
	}

	public void updateDebuffLevel(LCPlayer lcp) {
		lcp.setThirstDebuff(getDebuffLevel(lcp.getThirst()));
	}
}
