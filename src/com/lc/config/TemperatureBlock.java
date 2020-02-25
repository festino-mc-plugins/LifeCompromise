package com.lc.config;

import org.bukkit.Material;

public class TemperatureBlock {
	Material m;
	double r;
	double t;
	boolean fireness;
	boolean nether_only;
	
	public TemperatureBlock(Material m, double r, double t)
	{
		this.m = m;
		this.r = r;
		this.t = t;
		this.fireness = false;
	}

	public TemperatureBlock(Material m, double radius, double temp, boolean fireness, boolean nether_only)
	{
		this.m = m;
		this.r = radius;
		this.t = temp;
		this.fireness = fireness;
		this.nether_only = nether_only;
	}
}
