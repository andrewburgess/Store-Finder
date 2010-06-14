package com.parse3.storefinder.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.models.Database;
import com.parse3.storefinder.models.StoreDownloader;
import com.parse3.storefinder.views.interfaces.IHomeView;

public class SplashScreenController {
	private IHomeView view;
	private Database db;
	private LocationManager locationManager;
	private Location lastKnown;
	
	private Thread downloadStoresThread = new Thread() {
		public void run() {
			Log.i(Program.LOG, "downloadStoresThread.run()");
			
			Message m = new Message();
			
			StoreDownloader downloader = new StoreDownloader(view.getContext());
			downloader.downloadStores();
			
			
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.v(Program.LOG, "handler.handleMessage()");
			
			
		}
	};
	
	public SplashScreenController(IHomeView view) {
		Log.v(Program.LOG, "SplashScreenController._constructor()");
		
		this.view = view;
		
		db = new Database(view.getContext());
		
		//Set up database
		db.open();
		
		locationManager = (LocationManager)view.getContext().getSystemService(Context.LOCATION_SERVICE);
		lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		SharedPreferences prefs = view.getContext().getSharedPreferences(Program.PREFERENCES, 0);
		if (!prefs.contains(Program.Preferences.SETUP)) {
			downloadStoresThread.start();
			view.showDialog();
		}
	}
	
	public void cleanUp() {
		db.close();
	}
}
