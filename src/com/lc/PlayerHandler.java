package com.lc;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.lc.config.Config;
import com.lc.config.Config.Key;
import com.lc.utils.ArmorWeight;
import com.lc.utils.TempDebuffLevel;
import com.lc.utils.ThirstDebuffLevel;
import com.lc.utils.Utils;
import com.lc.utils.UtilsArmor;
import com.lc.utils.UtilsRandom;
import com.lc.utils.UtilsTemperature;
import com.lc.utils.UtilsThirst;

public class PlayerHandler implements Runnable {
	
	private static final int EFFECT_DURATION = 1000000;
	
	private final LCPlayerList lcp_list;
	private final Config config;

	public final UtilsTemperature utils_temp;
	public final UtilsThirst utils_thirst;
	public final UtilsArmor utils_armor;
	
	public PlayerHandler(LCPlayerList lcp_list, Config config, List<World> world_list) {
		this.lcp_list = lcp_list;
		this.config = config;
		utils_temp = new UtilsTemperature(config, world_list);
		utils_thirst = new UtilsThirst(config);
		utils_armor = new UtilsArmor(config);
	}
	
	/** Thirst, temperature, armor, debuff, buff, output */
	@Override
	public void run()
	{
		boolean lc_on = config.get(Key.LC_ON);
		if (!lc_on)
			return;
		
		boolean temp = config.get(Key.MODULE_TEMPERATURE);
		boolean thirst = config.get(Key.MODULE_THIRST);
		boolean armor = config.get(Key.MODULE_ARMOR_WEIGHT);
		boolean buff = config.get(Key.IS_BUFFING);
		boolean debuff = config.get(Key.IS_DEBUFFING);
		
		if (temp) {
			utils_temp.onTick();
		}
		
		for (LCPlayer lcp : lcp_list.getSnapshot()) {
			if (lcp.isLC()) {
				
				if (temp) {
					utils_temp.tick(lcp);
					utils_temp.updateDebuffLevel(lcp);
				}

				if (thirst) {
					utils_thirst.tick(lcp);
					utils_thirst.updateDebuffLevel(lcp);
				}

				if (armor) {
					utils_armor.update(lcp);
				}
				
				if (buff) {
					
				}
				
				if (debuff) {
					affectHealth(lcp);
					if (armor) {
						affectWeight(lcp);
					}
				}
			} else {
				utils_temp.updateDebuffLevel(lcp);
				utils_thirst.updateDebuffLevel(lcp);
				utils_armor.update(lcp);
				normalize(lcp);
			}

			lcp.output();
		}
		//...
	}
	
	public void normalize(LCPlayer lcp) {
		Player p = lcp.getPlayer();
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(config.get(Key.DEFAULT_SPEED));
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
		removeEffect(p, PotionEffectType.SLOW_DIGGING);
		removeEffect(p, PotionEffectType.DAMAGE_RESISTANCE);
		removeEffect(p, PotionEffectType.INCREASE_DAMAGE);
		removeEffect(p, PotionEffectType.HUNGER);
	}
	
	/** Uses temp and thirst */
	public void affectHealth(LCPlayer lcp)
	{
		Player p = lcp.getPlayer();
		if (p.isDead())
			return;
		
		TempDebuffLevel temp_debuff = lcp.getTemperatureDebuff();
		ThirstDebuffLevel thirst_debuff = lcp.getThirstDebuff();
		int temp_debuff_lvl = temp_debuff.getDebuffLvl();
		int thirst_debuff_lvl = thirst_debuff.getDebuffLvl();
		double default_speed = config.get(Key.DEFAULT_SPEED);
		double temp_slowness = config.get(Key.TEMP_SLOWNESS);
		double thirst_slowness = config.get(Key.THIRST_SLOWNESS);
		
		if (temp_debuff_lvl >= 3)
			killPlayer(p);
		if (thirst_debuff_lvl >= 3)
			multiplyMovementSpeed(p, 1 - thirst_slowness / 100);

		setMovementSpeed(p, default_speed);
		if (temp_debuff_lvl >= 1)
			multiplyMovementSpeed(p, 1 - temp_slowness / 100);
		if (thirst_debuff_lvl >= 1)
			multiplyMovementSpeed(p, 1 - thirst_slowness / 100);

		if (temp_debuff_lvl >= 1 || thirst_debuff_lvl >= 2) {
			// addEffect(p, PotionEffectType.CONFUSION, 0);
			double hunger_power = config.get(Key.HUNGER_POWER);
			p.setExhaustion((float) (p.getExhaustion() + hunger_power)); // TODO function
			addEffect(p, PotionEffectType.HUNGER, 0);
			
			int damage_ticks = (temp_debuff_lvl >= 2) ? 1 : 0;
			damage_ticks += (temp_debuff_lvl >= 3) ? 1 : 0;
			if (p.getTicksLived() % 20 == 0 && damage_ticks >= 1)
				damagePlayer(p, 1);
			if (p.getTicksLived() % 20 == 10 && damage_ticks >= 2)
				damagePlayer(p, 1);
		} else {
			// removeEffect(p, PotionEffectType.CONFUSION);
			removeEffect(p, PotionEffectType.HUNGER);
		}
	}
	
	public void affectWeight(LCPlayer lcp) {
		Player p = lcp.getPlayer();
		ArmorWeight armor_weight = lcp.getArmorWeight();
		Block b = p.getLocation().getBlock();
		Material m = b.getType();
		double hor_velocity = lcp.getLastMove().setY(0).length();
		//System.out.print(hor_velocity);
		
		if (armor_weight == ArmorWeight.ULTRA_HEAVY) {
			multiplyMovementSpeed(p, 0.7);
			setAttackSpeed(p, 3.3);
			addEffect(p, PotionEffectType.SLOW_DIGGING, 0);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 1);
			addEffect(p, PotionEffectType.INCREASE_DAMAGE, 2);

			if (Utils.isWeakPlant(m) || Utils.isWeakestPlant(m) || Utils.isStrongPlant(m))
				destroyWeakPlant(p, hor_velocity);
			trampleFarmland(p, 3 * hor_velocity * 0.03 / 0.2);
			stampGrass(p, hor_velocity);
			destroyIce(p);
			sink(lcp, -0.045);
			
			//System.out.println("Y:"+Y[i]+" VY:"+p.getVelocity().getY());
			/*if(Y[i]- p.getLocation().getY() > 0.3 && p.getVelocity().getY() < -0.07 && p.getVelocity().getY() > -0.08)
			{
				//p.damage(1);
				for(Entity e : p.getNearbyEntities(1.5,1.5,1.5))
					if(e instanceof CraftLivingEntity && e != (Entity)p)
						((CraftLivingEntity) e).damage(2);
			}*/
		}
		else if (armor_weight == ArmorWeight.HEAVY) {
			multiplyMovementSpeed(p, 0.76);
			setAttackSpeed(p, 3.4);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 1);
			addEffect(p, PotionEffectType.INCREASE_DAMAGE, 1);

			if (Utils.isWeakPlant(m) || Utils.isWeakestPlant(m))
				destroyWeakPlant(p, hor_velocity);
			if (Utils.isStrongPlant(m))
				destroyStrongPlant(p, hor_velocity);
			trampleFarmland(p, 2 * hor_velocity * 0.03 / 0.2);
			destroyIce(p);
			sink(lcp, -0.03);
			/*if(Y[i]- p.getLocation().getY() > 0.3 && p.getVelocity().getY() < -0.07 && p.getVelocity().getY() > -0.08)
			{
				//p.damage(0.01);
				for(Entity e : p.getNearbyEntities(1,1,1))
					if(e instanceof CraftLivingEntity && e != (Entity)p)
						((CraftLivingEntity) e).damage(1);
			}*/
		}
		else if (armor_weight == ArmorWeight.MEDIUM) {
			multiplyMovementSpeed(p, 0.84);
			setAttackSpeed(p, 3.7);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
			addEffect(p, PotionEffectType.INCREASE_DAMAGE, 0);
			
			if (Utils.isWeakestPlant(m))
				destroyWeakPlant(p, hor_velocity);
			if (Utils.isWeakPlant(m))
				destroyStrongPlant(p, hor_velocity);
			trampleFarmland(p, hor_velocity * 0.03 / 0.2);
			sink(lcp, -0.02);
		}
		else if (armor_weight == ArmorWeight.LIGHT) {
			multiplyMovementSpeed(p, 0.92);
			setAttackSpeed(p, 4.0);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
		}
		
		int lvl = armor_weight.getLvl();
		if (lvl < 1)
			removeEffect(p, PotionEffectType.DAMAGE_RESISTANCE);
		if (lvl < 1)
			setAttackSpeed(p, 4.0);
		if (lvl < 2)
			removeEffect(p, PotionEffectType.INCREASE_DAMAGE);
		if (lvl < 4)
			removeEffect(p, PotionEffectType.SLOW_DIGGING);
		lcp.updateMove();
	}
	
	
	
	public static void addEffect(Player p, PotionEffectType effect, int lvl) {
		if (p.getPotionEffect(effect) == null
				|| p.getPotionEffect(effect).getAmplifier() <= lvl
				|| p.getPotionEffect(effect).getDuration() > 6000 && p.getPotionEffect(effect).getAmplifier() > lvl) {
			p.removePotionEffect(effect);
			p.addPotionEffect(new PotionEffect(effect, EFFECT_DURATION, lvl, true, false));
		}
	}
	
	public static void removeEffect(Player p, PotionEffectType effect) {
		if (p.getPotionEffect(effect) != null
				&& p.getPotionEffect(effect).getDuration() > 6000) {
			p.removePotionEffect(effect);
		}
	}
	
	public static void killPlayer(Player p) {
		p.damage(p.getHealth() + 1); // to be ressurected by totem
	}
	
	public static void damagePlayer(Player p, double damage) {
		double health = p.getHealth() - damage;
		if (health > 0)
			p.setHealth(health);
		else
			killPlayer(p);
	}

	public static void setAttackSpeed(Player p, double speed) {
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
	}

	public static void setMovementSpeed(Player p, double speed) {
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
	}
	
	public static void multiplyMovementSpeed(Player p, double multiplier) {
		double old_value = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(old_value * multiplier);
	}
	
	
	
	// sneaking 0.05 < 0.06471
	// walking 0.210 < 0.216
	// sprint > 0.28
	// depends on attribute and effects
	public static void destroyWeakPlant(Player p, double hor_velocity) {
		if (hor_velocity > 0.1) {
			if (UtilsRandom.hyperbolicDistribution(0.07, 0.01, 0.1, hor_velocity))
				p.getLocation().getBlock().breakNaturally();
		}
	}
	public static void destroyStrongPlant(Player p, double hor_velocity) {
		// velocity > 0.05
		if (hor_velocity > 0.23) {
			if (UtilsRandom.hyperbolicDistribution(0.06, 0.03, 0.23, hor_velocity))
				p.getLocation().getBlock().breakNaturally();
		}
	}
	
	public static void destroyIce(Player p) {
		Location l = p.getLocation();
		l.setY(l.getY() - 0.1);
		Block b = l.getBlock();
		Material m =b.getType();
		if (m == Material.ICE) {
			if (b.getRelative(BlockFace.DOWN).isEmpty())
				b.breakNaturally();
			else if (b.getRelative(BlockFace.DOWN).getType() == Material.WATER)
				b.setType(Material.WATER);
			// falling
		}
	}
	
	public static void trampleFarmland(Player p, double tick_probability) {
		Location l = p.getLocation();
		l.setY(l.getY() - 0.01);
		Block b = l.getBlock();
		Material m = b.getType();
		if (m == Material.FARMLAND && UtilsRandom.linearRandom(tick_probability)) {
			b.setType(Material.DIRT);
			double new_y = l.getBlockY() + 1;
			l.setY(new_y);
			if (new_y > p.getLocation().getY())
				p.teleport(l);
		}
	}
	
	public static void stampGrass(Player p, double hor_velocity) {
		Location l = p.getLocation();
		l.setY(l.getY() - 0.1);
		Material m = l.getBlock().getType();
		if (m == Material.GRASS_BLOCK && UtilsRandom.parabolicRandom(0.03, 0.06, 0, hor_velocity))
			l.getBlock().setType(Material.DIRT_PATH);
	}
	
	public static void sink(LCPlayer lcp, double force) {
		Player p = lcp.getPlayer();
		Location l = p.getLocation();
		l.setY(l.getY() - 0.1);
		Material m = l.getBlock().getType();
		
		if ( Math.abs(0.415 - p.getVelocity().getY()) < 0.051 )
			lcp.setSwimmingTicks(13);
		
		if (!lcp.canSwim() && m == Material.WATER)
		{
			l = p.getLocation();
			l.setY(l.getY() + 0.5);
			m = l.getBlock().getType(); //ÓÑÊÎÐÈÒÜ ÎÏÓÙÅÍÈÅ, ÎÑËÀÁËßÒÜ ÝÔÔÅÊÒ
			if (m == Material.WATER && lcp.getLastMove().getY() >= -0.015 )
				p.setVelocity(new Vector(p.getVelocity().getX(), force, p.getVelocity().getZ()));
		}
	}
}
