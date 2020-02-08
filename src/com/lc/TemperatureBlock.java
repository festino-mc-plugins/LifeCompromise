package com.lc;

import org.bukkit.Material;

public class TemperatureBlock {
	Material id;
	Double R;
	Double T;
	boolean fireness;

	public TemperatureBlock()
	{
		this.id = null;
		this.R = null;
		this.T = null;
		this.fireness = false;
	}
	
	public TemperatureBlock(Material m, double r, double t)
	{
		this.id = m;
		this.R = r;
		this.T = t;
		this.fireness = false;
	}

	public TemperatureBlock(Material m, double r, double t, boolean b)
	{
		this.id = m;
		this.R = r;
		this.T = t;
		this.fireness = b;
	}
}
