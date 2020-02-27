package com.lc.utils;

public enum TempDebuffLevel {
	FROSTBITE_3, FROSTBITE_2, FROSTBITE_1, NONE, HEATSTROKE_1, HEATSTROKE_2, HEATSTROKE_3;
	
	public int getDebuffLvl() {
		return Math.abs(getLvl());
	}
	
	public int getLvl() {
		switch (this) {
		case FROSTBITE_3:
			return -3;
		case FROSTBITE_2:
			return -2;
		case FROSTBITE_1:
			return -1;
		case NONE:
			return 0;
		case HEATSTROKE_1:
			return 1;
		case HEATSTROKE_2:
			return 2;
		case HEATSTROKE_3:
			return 3;
		}
		return 0;
	}
}
