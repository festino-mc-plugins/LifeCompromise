package com.lc;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lc.config.Config.Key;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LCPlayer {
	public enum LCMode { NONE, FORCED_ON, FORCED_OFF };
	
	Main plugin;
	Player p;
	public boolean softLC;
	public LCMode forcedLC;
	
	public Double NormalT = 26d;
	public Double T;
	public Double Thirst;
	public boolean logged;
	public int unbed;
	// private int unwellTime;
	public int maxOutputTicks;
	public int outputTicks = maxOutputTicks;

	public TempDebuffLevel temp_debuff;
	public byte thirst_debuff;
	public double insularity;
	//private double dTnormal;
	public double dTair;
	private double Taround;
	public double BiomeTemperature;
	public double BlocksTemperature;
	public double EvilTemperature;
	public double ArmorWeight;
	
	public LCPlayer(Player p, Main plugin)
	{
		this.plugin = plugin;
		this.p = p;
		softLC = false;
		forcedLC = LCMode.NONE;
		maxOutputTicks = plugin.config.get(Key.DEFAULT_OUTPUT_TICKS);
		reset();
		temp_debuff = TempDebuffLevel.NONE;
		thirst_debuff = 0;
		unbed = 0;
	}
	
	public void reset() {
		T = NormalT;
		Thirst = 100.0;
	}

	public void normalize() {
		plugin.getPlayerHandler().normalize(this);
	}
	
	private boolean isLCble() {
		return forcedLC == LCMode.FORCED_ON || softLC && forcedLC != LCMode.FORCED_OFF;
	}
	
	public boolean isLC() {
		return isLoggedIn() && isLCble();
	}
	
	public LCMode getForcedLC() {
		return forcedLC;
	}
	
	public boolean getSoftLC() {
		return softLC;
	}

	public boolean isLoggedIn() {
		return logged;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public UUID getUUID() {
		return p.getUniqueId();
	}
	
	public double getTemperature() {
		return T;
	}

	public double getThirst() {
		return Thirst;
	}
	
	public void setForcedLC(LCMode mode) {
		this.forcedLC = mode;
	}
	
	public void setSoftLC(boolean softLC) {
		this.softLC = softLC;
	}

	public void setLoggedIn(boolean is_logged) {
		this.logged = is_logged;
	}
	
	public void setTemperature(double T) {
		this.T = T;
	}

	public void setThirst(double thirst) {
		this.Thirst = thirst;
	}
	
	public void setTitleCooldown(int ticks) {
		if (ticks < 0) {
			ticks = 0;
		}
		unbed = ticks;
	}
	
	public void output()
	{
		if (!isLCble())
			return;
		
		if (outputTicks >= maxOutputTicks) {
			outputTicks = 0;
		} else {
			outputTicks++;
			return;
		}
		

		if (this.unbed == 0)
		{
			ChatColor color_temp;
			switch(temp_debuff)
			{
			case FROSTBITE_2: color_temp = ChatColor.BLUE; break;
			case FROSTBITE_1: color_temp = ChatColor.AQUA; break;
			case NONE: color_temp = ChatColor.WHITE; break;
			case HEATSTROKE_1: color_temp = ChatColor.GOLD; break;
			case HEATSTROKE_2: color_temp = ChatColor.RED; break;
			default: color_temp = ChatColor.BLACK; break;
			}
			String temp_trend;
			if(dTair > 0)       temp_trend = "↓";
			else if(dTair == 0) temp_trend = "•";
			else             	temp_trend = "↑";
			
			ChatColor color_thirst;
			switch(thirst_debuff)
			{
			case 0: color_thirst = ChatColor.WHITE; break;
			case 1: color_thirst = ChatColor.GOLD; break;
			case 2: color_thirst = ChatColor.RED; break;
			case 3: color_thirst = ChatColor.BLACK; break;
			default: color_thirst = ChatColor.BLACK; break;
			}
			// Т:26.25|, Ж:100.0
			/*String title =
					 String.format("[{\"text\": \"Т:%5f\"\",\"color\":\"%s\"},", (double)Math.round(this.T*100)/100, color_temp)
					+ String.format("{\"text\": \"%s, \",\"color\":\"white\"},", temp_trend)
					+ String.format("{\"text\": \"Ж:%6f\",\"color\":\"%s\"}]", (double)Math.round(this.Thirst*100)/100, color_thirst);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(title));*/
			String title =
					 String.format(color_temp + 		"Т:%s", Utils.truncDouble(this.T, 5))
					+ String.format(ChatColor.WHITE + 	"%s, ", temp_trend)
					+ String.format(color_thirst + 		"Ж:%s", Utils.truncDouble(this.Thirst, 5));
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(title));
		}
		else
			this.unbed -= 1;
	}
	
	public double getAirT()
	{
		return this.Taround;
	}
	
	public void setAirT(double t)
	{
		this.Taround = t;
	}
}
