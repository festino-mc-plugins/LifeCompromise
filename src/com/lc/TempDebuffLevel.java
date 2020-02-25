package com.lc;

public enum TempDebuffLevel {
	FROSTBITE_2, FROSTBITE_1, NONE, HEATSTROKE_1, HEATSTROKE_2;
	
	public int getDebuffLvl() {
		return Math.abs(getLvl());
	}
	
	public int getLvl() {
		switch (this) {
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
		}
		return 0;
	}
}
