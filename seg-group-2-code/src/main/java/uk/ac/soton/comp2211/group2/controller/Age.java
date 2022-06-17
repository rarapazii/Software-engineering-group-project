package uk.ac.soton.comp2211.group2.controller;

public enum Age {
	UNDER_25,
	x25_TO_34,
	x35_TO_44,
	x45_TO_54,
	OVER_55;
	
	public static Age fromString(String s) {
		switch (s) {
		case "<25":
			return Age.UNDER_25;
		case "25-34":
			return Age.x25_TO_34;
		case "35-44":
			return Age.x35_TO_44;
		case "45-54":
			return Age.x45_TO_54;
		case ">54":
			return Age.OVER_55;
		default:
			throw new IllegalArgumentException(String.format("%s isn't a valid Age", s));
		}
	}
	
}
