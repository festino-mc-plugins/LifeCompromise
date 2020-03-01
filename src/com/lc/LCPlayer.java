package com.lc;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lc.config.Config.Key;
import com.lc.utils.ArmorWeight;
import com.lc.utils.TempDebuffLevel;
import com.lc.utils.ThirstDebuffLevel;
import com.lc.utils.Utils;
import com.lc.utils.UtilsTemperature;
import com.lc.utils.UtilsThirst;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LCPlayer {
	public enum LCMode { NONE, FORCED_ON, FORCED_OFF };
	
	Main plugin;
	Player p;
	private boolean softLC = false;
	private LCMode forcedLC = LCMode.NONE;
	
	private Double NormalT = 26d;
	private Double T;
	private Double thirst;
	private boolean logged;
	private int unbed;
	//private int unwellTime;
	private int maxOutputTicks;
	private int outputTicks = maxOutputTicks;
	private double Taround;

	public TempDebuffLevel temp_debuff = TempDebuffLevel.NONE;
	public ThirstDebuffLevel thirst_debuff = ThirstDebuffLevel.NONE;
	private ArmorWeight armor_weight;

	//private double dTnormal;
	// private double insularity;
	// private double BiomeTemperature;
	// private double BlocksTemperature;
	// private double EvilTemperature;
	private Location last_loc;
	private int swimming_ticks = 0;
	private int cauldron_ticks = 0;
	
	public LCPlayer(Player p, Main plugin)
	{
		this.plugin = plugin;
		this.p = p;
		maxOutputTicks = plugin.config.get(Key.DEFAULT_OUTPUT_TICKS);
		reset();
		logged = true;
		unbed = 0;
		updateMove();
	}
	
	public void reset() {
		T = NormalT;
		thirst = 100.0;
	}

	public void normalize() {
		plugin.getPlayerHandler().normalize(this);
	}
	
	private boolean isLCble() {
		return forcedLC == LCMode.FORCED_ON || softLC && forcedLC != LCMode.FORCED_OFF;
	}
	
	public boolean isLC() {
		GameMode gm = p.getGameMode();
		return isLoggedIn() && isLCble() && !p.isDead() && (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE);
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
		return thirst;
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
		if (T > UtilsTemperature.MAX_T) T = UtilsTemperature.MAX_T;
		if (T < UtilsTemperature.MIN_T) T = UtilsTemperature.MIN_T;
		this.T = T;
	}

	public void setThirst(double thirst) {
		if (thirst > UtilsThirst.MAX_THIRST) thirst = UtilsThirst.MAX_THIRST;
		if (thirst < UtilsThirst.MIN_THIRST) thirst = UtilsThirst.MIN_THIRST;
		this.thirst = thirst;
	}

	public void addThirst(double thirst) {
		setThirst(this.thirst + thirst);
	}
	
	public void setTitleCooldown(int ticks) {
		if (ticks < 0) {
			ticks = 0;
		}
		unbed = ticks;
	}
	
	public TempDebuffLevel getTemperatureDebuff() {
		return temp_debuff;
	}
	
	public ThirstDebuffLevel getThirstDebuff() {
		return thirst_debuff;
	}
	
	public void setTemperatureDebuff(TempDebuffLevel temp_debuff) {
		this.temp_debuff = temp_debuff;
	}
	
	public void setThirstDebuff(ThirstDebuffLevel thirst_debuff) {
		this.thirst_debuff = thirst_debuff;
	}
	
	public void output()
	{
		if (!isLCble())
			return;
		
		if (unbed > 0) {
			unbed--;
		}
		
		if (outputTicks >= maxOutputTicks) {
			outputTicks = 0;
		} else {
			outputTicks++;
			return;
		}
		

		if (this.unbed <= 0)
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
			
			double dT = Taround - T;
			String temp_trend;
			if(dT < 0)       temp_trend = "↓";
			else if(dT == 0) temp_trend = "•";
			else             	temp_trend = "↑";
			
			ChatColor color_thirst;
			switch(thirst_debuff)
			{
			case NONE: color_thirst = ChatColor.WHITE; break;
			case DEBUFF_1: color_thirst = ChatColor.GOLD; break;
			case DEBUFF_2: color_thirst = ChatColor.RED; break;
			default: color_thirst = ChatColor.BLACK; break;
			}
			// Т:26.25|, Ж:100.0
			String title =
					 String.format(color_temp + 		"Т:%s", Utils.truncDouble(this.T, 5))
					+ String.format(ChatColor.WHITE + 	"%s, ", temp_trend)
					+ String.format(color_thirst + 		"Ж:%s", Utils.truncDouble(this.thirst, 5));
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(title));
		}
	}
	
	public double getAirT()
	{
		return this.Taround;
	}
	
	public void setAirT(double t)
	{
		this.Taround = t;
	}
	
	public int getOutputTicks() {
		return maxOutputTicks;
	}
	
	public void setOutputTicks(int ticks) {
		if (ticks > 0)
			maxOutputTicks = ticks;
	}
	
	public ArmorWeight getArmorWeight() {
		return armor_weight;
	}
	
	public void setArmorWeight(ArmorWeight armor_weight) {
		this.armor_weight = armor_weight;
	}
	
	public Location getLastLocation() {
		return last_loc;
	}
	
	public Vector getLastMove() {
		return p.getLocation().toVector().subtract(last_loc.toVector());
	}
	
	public void updateMove() {
		// TODO security fix
		last_loc = p.getLocation();
		if (swimming_ticks > 0) {
			swimming_ticks--;
		}
		if (cauldron_ticks > 0) {
			cauldron_ticks--;
		}
	}
	
	public void setSwimmingTicks(int ticks) {
		if (ticks > 0)
			swimming_ticks = ticks;
	}
	public boolean canSwim() {
		return swimming_ticks > 0;
	}
	
	public void setCauldronTicks(int ticks) {
		if (ticks > 0)
			cauldron_ticks = ticks;
	}
	public boolean canDrinkCauldron() {
		return cauldron_ticks <= 0;
	}
}
