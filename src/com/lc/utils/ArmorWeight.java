package com.lc.utils;

public enum ArmorWeight {
	NONE, LIGHT, MEDIUM, HEAVY, ULTRA_HEAVY;
	
	public int getLvl() {
		switch (this) {
		case NONE:
			return 0;
		case LIGHT:
			return 1;
		case MEDIUM:
			return 2;
		case HEAVY:
			return 3;
		case ULTRA_HEAVY:
			return 4;
		}
		return 0;
	}
}