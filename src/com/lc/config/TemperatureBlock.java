package com.lc.config;

import org.bukkit.Material;

public class TemperatureBlock {
	public final Material m;
	public final double r;
	public final double t;
	public final boolean fireness;
	public final boolean nether_only;
	
	public TemperatureBlock(Material m, double r, double t)
	{
		this.m = m;
		this.r = r;
		this.t = t;
		this.fireness = false;;
		this.nether_only = false;
	}

	public TemperatureBlock(Material m, double radius, double temp, boolean fireness, boolean nether_only)
	{
		this.m = m;
		this.r = radius;
		this.t = temp;
		this.fireness = fireness;
		this.nether_only = nether_only;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{m:" + m + ", T:" + t + ", R:" + r + ", fireness:" + fireness + ", nether:" + nether_only + "}";
	}
}
