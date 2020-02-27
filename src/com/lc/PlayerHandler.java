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
import com.lc.utils.UtilsArmor;
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
	public void run() {
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
					affectWeight(lcp);
					/*if(dTnormal>Config.heatDeathT) this.TempDebuff=3;
					else if(dTnormal<Config.frostDeathT) this.TempDebuff=-3;
					else if(dTnormal>Config.heatstroke2) this.TempDebuff=2;
					else if(dTnormal<Config.frostbite2) this.TempDebuff=-2;
					else if(dTnormal>Config.heatstroke1) this.TempDebuff=1;
					else if(dTnormal<Config.frostbite1) this.TempDebuff=-1;
					else {this.TempDebuff=0; this.unwellTime = 0;}*/
				}
			}

			lcp.output();
		}
		//...
	}
	
	public void normalize(LCPlayer lcp) {
		Player p = lcp.getPlayer();
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(config.get(Key.DEFAULT_SPEED));
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
		if(p.getPotionEffect(PotionEffectType.SLOW_DIGGING) != null && p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getDuration() >= 1000 )
			p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		if(p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) != null && p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() >= 1000)
			p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		if(p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null && (p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() >= 3000))
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		if(p.getPotionEffect(PotionEffectType.HUNGER) != null && p.getPotionEffect(PotionEffectType.HUNGER).getDuration() >= 10000)
			p.removePotionEffect(PotionEffectType.HUNGER);
	}
	
	/** Uses temp and thirst */
	public void affectHealth(LCPlayer lcp) {
		/*
		if(T[i] < frostbite1 || T[i] > heatstroke1)
	    	getServer().getPlayer(Tid[i]).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_SPEED - SpeedTempDecrease);
	    else
	    	getServer().getPlayer(Tid[i]).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_SPEED);
		if(Thirst[i] < minSlownessThirstProcent)	
			getServer().getPlayer(Tid[i]).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getServer().getPlayer(Tid[i]).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue()-SpeedThirstDecrease);
			
			
		if(frostDeathT >= T[i] || T[i] >= heatDeathT)
			getServer().getPlayer(Tid[i]).setHealth(0);
		else if(getServer().getPlayer(Tid[i]).getHealth() > 0 && (T[i] < frostbite1 || T[i] > heatstroke1 || Thirst[i] < minNauseaThirstProcent))	
		{
	    	//if(getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.CONFUSION) == null || getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.CONFUSION).getDuration() < 100)
    		//	getServer().getPlayer(Tid[i]).addPotionEffect(Confusion);
			getServer().getPlayer(Tid[i]).setExhaustion((float) (getServer().getPlayer(Tid[i]).getExhaustion()+hungerPower));;
    		if(getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.HUNGER) == null || getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.HUNGER).getDuration() < 10000)
    			{getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.HUNGER);
    	    	getServer().getPlayer(Tid[i]).addPotionEffect(Hunger);}
	    	if(getServer().getWorld("world").getTime()%20 == 0 && (T[i] < frostbite2 || T[i] > heatstroke2 || Thirst[i] <= 0))
	    		if(getServer().getPlayer(Tid[i]).getHealth()-1>0)
	    			getServer().getPlayer(Tid[i]).setHealth(getServer().getPlayer(Tid[i]).getHealth()-1);
	    		else
	    			getServer().getPlayer(Tid[i]).setHealth(0);
	    		//p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,30000,0,false,false),false);
    		//getServer().getPlayer(Tid[i]).damage(1);
	    	if(getServer().getWorld("world").getTime()%20 == 10 && ((T[i] < frostbite2 || T[i] > heatstroke2) && Thirst[i] <= 0))
	    		if(getServer().getPlayer(Tid[i]).getHealth()-1>0)
	    			getServer().getPlayer(Tid[i]).setHealth(getServer().getPlayer(Tid[i]).getHealth()-1);
    			else
    				getServer().getPlayer(Tid[i]).setHealth(0);
    		//getServer().getPlayer(Tid[i]).damage(1);
	    }
	    else if(getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.HUNGER) != null && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.HUNGER).getDuration() > 10000)
	    {
	    	//getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.CONFUSION);
	    	getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.HUNGER);
	    }
		*/
	}
	
	public void affectWeight(LCPlayer lcp) {
		Player p = lcp.getPlayer();
		ArmorWeight armor_weight = lcp.getArmorWeight();
		if (armor_weight == ArmorWeight.ULTRA_HEAVY) {
			affectMovementSpeed(p, 0.7);
			setAttackSpeed(p, 3.3);
			addEffect(p, PotionEffectType.SLOW_DIGGING, 0);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 1);
			addEffect(p, PotionEffectType.INCREASE_DAMAGE, 2);

			destroyPlants(p);
			trampleFarmland(p);
			stampGrass(p);
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
			affectMovementSpeed(p, 0.76);
			setAttackSpeed(p, 3.4);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 1);
			addEffect(p, PotionEffectType.INCREASE_DAMAGE, 1);

			destroyPlants(p);
			trampleFarmland(p);
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
			affectMovementSpeed(p, 0.84);
			setAttackSpeed(p, 3.7);
			addEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 0);
			addEffect(p, PotionEffectType.INCREASE_DAMAGE, 0);
			
			destroyPlants(p);
			trampleFarmland(p);
			sink(lcp, -0.02);
		}
		else if (armor_weight == ArmorWeight.LIGHT) {
			affectMovementSpeed(p, 0.92);
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
		/*
		if(ArmorWeight <= WeightLimit1 && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) != null && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() > 1000)
			getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		if(ArmorWeight <= WeightLimit2 && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() > 6000)
			getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		if(ArmorWeight <= WeightLimit1 && getServer().getPlayer(Tid[i]).getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 4.0)
			getServer().getPlayer(Tid[i]).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
		if(ArmorWeight < WeightLimit4 && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.SLOW_DIGGING) != null && getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.SLOW_DIGGING).getDuration() > 1000)
			getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.SLOW_DIGGING);*/
		lcp.updateLastY();
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

	public static void setAttackSpeed(Player p, double speed) {
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
	}

	public static void setMovementSpeed(Player p, double speed) {
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
	}
	
	public static void affectMovementSpeed(Player p, double multiplier) {
		double old_value = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(old_value * multiplier);
	}
	
	
	
	public static void destroyPlants(Player p) {
		Material m = p.getLocation().getBlock().getType();
		if (m == Material.GRASS || m == Material.DANDELION || m == Material.POPPY || m == Material.LILY_PAD)
			p.getLocation().getBlock().breakNaturally();
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
	
	public static void trampleFarmland(Player p) {
		Location l = p.getLocation();
		Material m = l.getBlock().getType();
		// TODO add random
		if (m == Material.FARMLAND) {
			p.getLocation().getBlock().setType(Material.DIRT);
			l.setY(l.getY() + 0.2);
			p.teleport(l);
		}
	}
	
	public static void stampGrass(Player p) {
		Location l = p.getLocation();
		l.setY(l.getY() - 0.1);
		Material m = l.getBlock().getType();
		if (m == Material.GRASS_BLOCK)
			l.getBlock().setType(Material.GRASS_PATH);
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
			if (m == Material.WATER && p.getLocation().getY() - lcp.getLastY() >= -0.015 )
				p.setVelocity(new Vector(p.getVelocity().getX(), force, p.getVelocity().getZ()));
		}
	}
}
