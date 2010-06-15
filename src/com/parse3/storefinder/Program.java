package com.parse3.storefinder;


public class Program {
	public static final String LOG = "Store Finder";
	public static final String PREFS = "Store Finder";
	public static double MILES_PER_METER = 0.000621371192;
	
	public static class DatabaseInfo {
		public static final String NAME = "store_finder.db";
		public static final int VERSION = 1;
	}
	
	public static class StoreDownloaderInfo {
		public static final String URL = "http://deceptacle.com/stores_new.json";
		public static final int BUFFER = 1024;
	}
	
	public static class Preferences {
		public static final String SETUP = "setup";
	}
	
	public static double round(double val, int places) {
		double p = Math.pow(10, places);
		val = val * p;
		double tmp = Math.round(val);
		return tmp/p;
	}
}
