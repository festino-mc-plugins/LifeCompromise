package com.lc.utils;

public enum ThirstDebuffLevel {
	NONE, DEBUFF_1, DEBUFF_2, DEBUFF_3;
	
	public int getDebuffLvl() {
		return getLvl();
	}
	
	public int getLvl() {
		switch (this) {
		case NONE:
			return 0;
		case DEBUFF_1:
			return 1;
		case DEBUFF_2:
			return 2;
		case DEBUFF_3:
			return 3;
		}
		return 0;
	}
}
