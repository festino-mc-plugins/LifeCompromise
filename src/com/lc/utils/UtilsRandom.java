package com.lc.utils;

import java.util.Random;

public class UtilsRandom {
	private static Random random = new Random();
	
	public static double nextDouble() {
		return random.nextDouble();
	}
	
	/** P = Pmax - c/x */
	public static boolean hyperbolicDistribution(double max_probability, double c, double x) {
		double rand = UtilsRandom.nextDouble();
		// double border = Math.pow(2, 0.1 - hor_velocity);
		double chance = max_probability - c / x;
		double border = 1 - chance;
		System.out.print("    isActivated_1 chance: " + chance);
		return rand > border;
	}
	
	/** P = Pmax - p_min*x_min/x */
	public static boolean hyperbolicDistribution(double p_max, double p_min, double x_min, double x) {
		return hyperbolicDistribution(p_max, p_min * x_min, x);
	}
	
	public static boolean linearRandom(double probability) {
		double rand = UtilsRandom.nextDouble();
		return rand < probability;
	}
	
	public static boolean parabolicRandom(double max_p, double max_x, double zero_x, double x) {
		double dx = Math.abs(max_x - zero_x);
		double centered_x = max_x - x;
		return linearRandom(max_p - max_p / (dx * dx) * centered_x * centered_x);
	}
}
