package com.lc;

public class Utils {
	
	public static String truncDouble(Double d, int length) {
		String format = d.toString();
		if (format.indexOf(".") > length) {
			// error
		} else if (format.length() > length) {
			format = format.substring(0, length);
			if (format.endsWith(".")) {
				format = format.substring(0, format.length() - 1);
			}
		} else {
			while (format.length() < length) {
				format += "0";
			}
		}
		
		return format;
	}
}
