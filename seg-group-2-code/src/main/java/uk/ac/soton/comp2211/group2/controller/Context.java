package uk.ac.soton.comp2211.group2.controller;

public enum Context {
	NEWS,
	SHOPPING,
	SOCIAL_MEDIA,
	BLOG,
	TRAVEL,
	HOBBIES;
	
	public static Context fromString(String s) {
		switch (s) {
		case "SOCIAL MEDIA":
			return Context.SOCIAL_MEDIA;
		default:
			return Context.valueOf(s);
		}
	}
	
}
