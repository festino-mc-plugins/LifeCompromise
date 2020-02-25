package com.lc;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.lc.config.Config;
import com.lc.config.Config.Key;

public class PlayerHandler implements Runnable {
	
	private final LCPlayerList lcp_list;
	private final Config config;
	
	public PlayerHandler(LCPlayerList lcp_list, Config config) {
		this.lcp_list = lcp_list;
		this.config = config;
	}
	
	/** Thirst, temperature, armor, debuff, buff, output */
	@Override
	public void run() {
		boolean temp = config.get(Key.MODULE_TEMPERATURE);
		boolean thirst = config.get(Key.MODULE_TEMPERATURE);
		boolean armor = config.get(Key.MODULE_ARMOR_WEIGHT);
		boolean buff = config.get(Key.IS_BUFFING);
		boolean debuff = config.get(Key.IS_DEBUFFING);
		
		for (LCPlayer lcp : lcp_list.getSnapshot()) {
			if (lcp.isLC()) {
			
			if (temp) {
				UtilsTemperature.tick(lcp);
			}

			if (thirst) {
				UtilsThirst.tick(lcp);
			}
			

			if (armor) {
				UtilsArmor.calc(lcp);
			}
			
			if (buff) {
				
			}
			
			if (debuff) {
				
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
}
