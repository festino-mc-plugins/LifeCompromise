package com.lc;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Insularity extends JavaPlugin
{
	public double BiomityWithSunlight(Location l)
	{
		double result;
		if(l.getY() >= 63)
			result = 1;
		else
		{
			result = (l.getY()-10)/63;
			if(result<0)
				result = 0;
			double temp = (l.getBlock().getLightFromSky())/10;
			if(temp > 1)
				temp = 1;
			result = result + temp + 0.1;
			if(result > 1)
				result = 1;
		}
		return result;
	}
	
	/*
	public double InsularityWithSunlight(Location l) //А ЧТО ЕСЛИ СВЕТА БОЛЬШЕ СО СТОРОНЫ, В КОТОРОЙ СТЕКЛО?
	{
		double InsK = 0;
		Location l1 = l;
		while(InsK < 1)
		{
			if(l.getBlock().getType() != Material.AIR)
			{
				if(l.getBlock().getType() == Material.GLASS)           InsK += 0.3;
				else if(l.getBlock().getType() == Material.IRON_BLOCK) InsK += 0.15;
				else if(l.getBlock().getType() == Material.WOOL)       InsK += 1;
				else                                                   InsK += 0.2;
			}
			if(l.getBlock().getLightFromSky() == 15 && l.getBlockY()<256) //Рассматривать и пути вбок, они могут быть чисто воздушные
				l.setY(l.getY()+1);
			else if(l.getBlock().getLightFromSky() == 15)
			{
				break;
			}
			else
			{ //Делать экземпляры некоего класса, создающие дерево путей
				l1.setX(l.getX()+1);
				if(l1.getBlock().getLightFromSky() > l.getBlock().getLightFromSky())
					{l.setX(l.getX()+1);
					continue;}
				l1.setX(l.getX()-1);
				if(l1.getBlock().getLightFromSky() > l.getBlock().getLightFromSky())
					{l.setX(l.getX()-1);
					continue;}
				l1.setX(l.getX());
				l1.setZ(l.getZ()+1);
				if(l1.getBlock().getLightFromSky() > l.getBlock().getLightFromSky())
					{l.setZ(l.getZ()+1);
					continue;}
				l1.setZ(l.getZ()-1);
				if(l1.getBlock().getLightFromSky() > l.getBlock().getLightFromSky())
					{l.setZ(l.getZ()-1);
					continue;}
				l.setY(l.getY()+1);
			}
		}
		if(InsK > 1)
			InsK = 1;
		return InsK;
	}

	public double InsularityWithoutSunlight(Location l)
	{
		double InsK = -1;
		while(InsK < 0)
		{
			
		}
		return InsK;
	}*/
}
