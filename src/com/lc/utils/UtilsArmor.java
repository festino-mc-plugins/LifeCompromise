package com.lc.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lc.LCPlayer;
import com.lc.config.Config;
import com.lc.config.Config.Key;

public class UtilsArmor {
	
	private static final double BOOTS_IMPACT = 1, LEGGINGS_IMPACT = 2, CHESTPLATE_IMPACT = 3, HELMET_IMPACT = 1;
	private static final double LEATHER = 1, GOLDEN = 1.5, CHAINMAIL = 1.5, IRON = 2, DIAMOND = 3,
			ELYTRA = 2 / CHESTPLATE_IMPACT, TURTLE = 1.5 / HELMET_IMPACT;

	private final Config config;
	
	public UtilsArmor(Config config) {
		this.config = config;
	}
	
	public void update(LCPlayer lcp)
	{
		double armor_weight = calc(lcp.getPlayer());
		ArmorWeight aw = getArmorWeight(armor_weight);
		lcp.setArmorWeight(aw);
	}
	
	public ArmorWeight getArmorWeight(double armor_weight) {
		double w = config.get(Key.WEIGHT_4);
		if (armor_weight >= w)
			return ArmorWeight.ULTRA_HEAVY;
		
		w = config.get(Key.WEIGHT_3);
		if (armor_weight >= w)
			return ArmorWeight.HEAVY;
		
		w = config.get(Key.WEIGHT_2);
		if (armor_weight >= w)
			return ArmorWeight.MEDIUM;
		
		w = config.get(Key.WEIGHT_1);
		if (armor_weight >= w)
			return ArmorWeight.LIGHT;
		
		return ArmorWeight.NONE;
	}
	
	public static double calc(Player p) {
		
		ItemStack helmet = p.getInventory().getHelmet();
		ItemStack chestplate = p.getInventory().getChestplate();
		ItemStack leggings = p.getInventory().getLeggings();
		ItemStack boots = p.getInventory().getBoots();

		double slot_impact[] = {HELMET_IMPACT, CHESTPLATE_IMPACT, LEGGINGS_IMPACT, BOOTS_IMPACT};
		ItemStack armor[] = new ItemStack[4];
		if (isHelmet(helmet))
			armor[0] = helmet;
		if (isChestplate(chestplate))
			armor[1] = chestplate;
		if (isLeggings(leggings))
			armor[2] = leggings;
		if (isBoots(boots))
			armor[3] = boots;
		
		double armor_weight = 0;
		for (int i = 0; i < 4; i++) {
			ItemStack cur_is = armor[i];
			double material_weight = 0;
			if (UtilsArmor.isLeather(cur_is))
				material_weight = LEATHER;
			if (UtilsArmor.isGolden(cur_is))
				material_weight = GOLDEN;
			if (UtilsArmor.isChainmail(cur_is))
				material_weight = CHAINMAIL;
			if (UtilsArmor.isIron(cur_is))
				material_weight = IRON;
			if (UtilsArmor.isDiamond(cur_is))
				material_weight = DIAMOND;
			if (UtilsArmor.isElytra(cur_is))
				material_weight = ELYTRA;
			if (UtilsArmor.isTurtleHelmet(cur_is))
				material_weight = TURTLE;
			armor_weight += material_weight * slot_impact[i];
		}
		
		return armor_weight;
	}

	public static boolean isHelmet(ItemStack is) {
		if (is == null) return false;
		return is.getType().toString().toLowerCase().contains("helmet");
	}

	public static boolean isChestplate(ItemStack is) {
		if (is == null) return false;
		return is.getType().toString().toLowerCase().contains("chestplate") || is.getType() == Material.ELYTRA;
	}

	public static boolean isLeggings(ItemStack is) {
		if (is == null) return false;
		return is.getType().toString().toLowerCase().contains("leggings");
	}

	public static boolean isBoots(ItemStack is) {
		if (is == null) return false;
		return is.getType().toString().toLowerCase().contains("boots");
	}
	
	public static boolean isLeather(ItemStack is) {
		if (is == null) return false;
		switch (is.getType()) {
		case LEATHER_HELMET:
		case LEATHER_CHESTPLATE:
		case LEATHER_LEGGINGS:
		case LEATHER_BOOTS:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isGolden(ItemStack is) {
		if (is == null) return false;
		switch (is.getType()) {
		case GOLDEN_HELMET:
		case GOLDEN_CHESTPLATE:
		case GOLDEN_LEGGINGS:
		case GOLDEN_BOOTS:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isChainmail(ItemStack is) {
		if (is == null) return false;
		switch (is.getType()) {
		case CHAINMAIL_HELMET:
		case CHAINMAIL_CHESTPLATE:
		case CHAINMAIL_LEGGINGS:
		case CHAINMAIL_BOOTS:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isIron(ItemStack is) {
		if (is == null) return false;
		switch (is.getType()) {
		case IRON_HELMET:
		case IRON_CHESTPLATE:
		case IRON_LEGGINGS:
		case IRON_BOOTS:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isDiamond(ItemStack is) {
		if (is == null) return false;
		switch (is.getType()) {
		case DIAMOND_HELMET:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_LEGGINGS:
		case DIAMOND_BOOTS:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isElytra(ItemStack is) {
		if (is == null) return false;
		return is.getType() == Material.ELYTRA;
	}
	
	public static boolean isTurtleHelmet(ItemStack is) {
		if (is == null) return false;
		return is.getType() == Material.TURTLE_HELMET;
	}
}
