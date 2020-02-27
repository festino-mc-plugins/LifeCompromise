package com.lc.utils;

import java.util.Random;

public class UtilsRandom {
	private static Random random = new Random();
	
	public static double nextDouble() {
		return random.nextDouble();
	}
}
