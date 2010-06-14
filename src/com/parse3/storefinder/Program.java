package com.parse3.storefinder;


public class Program {
	public static final String LOG = "Store Finder";
	public static final String PREFERENCES = "Store Finder";
	
	public static class DatabaseInfo {
		public static final String NAME = "store_finder.db";
		public static final int VERSION = 1;
	}
	
	public static class StoreDownloaderInfo {
		public static final String URL = "http://deceptacle.com/stores.json";
		public static final int BUFFER = 1024;
	}
	
	public static class Preferences {
		public static final String SETUP = "setup";
	}
}
