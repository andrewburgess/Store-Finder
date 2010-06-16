package com.parse3.storefinder.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.parse3.storefinder.LocationFinder;
import com.parse3.storefinder.Program;
import com.parse3.storefinder.StoreFinderApplication;
import com.parse3.storefinder.StoreOverlayItem;
import com.parse3.storefinder.models.Database;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.models.StoreDownloader;
import com.parse3.storefinder.views.interfaces.IMapStoresView;

public class MapStoresController {
	private static final int WHAT_SEARCHDB = 0;
	private static final int WHAT_DOWNLOAD = 1;
	private static final int WHAT_FINISHEDSEARCH = 2;
	private static final int MAP_SCALE = 1000000;
	
	private IMapStoresView view;
	private Database database;
	private SQLiteDatabase db;
	private LocationFinder userLocation;
	
	private Runnable locationFixed = new Runnable() {

		@Override
		public void run() {
			Log.v(Program.LOG, "MapStoresController.locationFixed.run()");
			
			view.getMapController().animateTo(userLocation.getMyLocation());
			view.getMapController().setZoom(15);
		}
		
	};
	
	private class DatabaseSearcher implements Runnable {
		@Override
		public void run() {
			Location l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if (l == null) {
				l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			
			double lat = l.getLatitude();
			double lon = l.getLongitude();
			
			database = database.open();
			db = database.getDatabase();
			
			Cursor c = db.query("store", new String[] {"_id", "name", "address", "city", "state", "zip", "phone", "latitude", "longitude"}, 
								"latitude > " + (lat - 0.5) + " AND latitude < " + (lat + 0.5) + 
								" AND longitude > " + (lon - 0.5) + " AND longitude < " + (lon + 0.5), 
								null, null, null, null);
			
			double latitude, longitude;
			
			c.moveToFirst();
			do {
				latitude = c.getDouble(7);
				longitude = c.getDouble(8);
				float[] results = new float[3];
				Location.distanceBetween(lat, lon, latitude, longitude, results);
				double distance = results[0] * Program.MILES_PER_METER;
				
				if (distance > 15)				//Make user customizable search distance
					continue;
				
				Store store = new Store();
				store.setId(c.getInt(0));
				store.setName(c.getString(1));
				store.setAddress(c.getString(2));
				store.setCitystate(c.getString(3) + ", " + c.getString(4) + " " + c.getString(5));
				store.setPhone(c.getString(6));
				
				Location loc = new Location("");
				loc.setLatitude(latitude);
				loc.setLongitude(longitude);
				store.setLocation(loc);
				
				store.setDistance(distance);
				
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putSerializable("store", store);
				msg.setData(b);
				msg.what = WHAT_SEARCHDB;
				handler.sendMessage(msg);
			} while (c.moveToNext());
			
			c.close();
			
			database.close();
			
			Message finish = new Message();
			finish.what = WHAT_FINISHEDSEARCH;
			handler.sendMessage(finish);
		}
	}
	
	
	private class StoreRefresher implements Runnable {
		public void run() {
			Log.i(Program.LOG, "downloadStoresThread.run()");
			
			Message m = new Message();
			
			StoreDownloader downloader = new StoreDownloader(view.getContext());
			downloader.downloadStores();
			
			m.what = WHAT_DOWNLOAD;
					
			handler.sendMessage(m);
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_SEARCHDB:
					Store s = (Store) msg.getData().getSerializable("store");
					GeoPoint gp = new GeoPoint((int)(s.getLocation().getLatitude() * MAP_SCALE), (int)(s.getLocation().getLongitude() * MAP_SCALE));
					StoreOverlayItem storeOverlay = new StoreOverlayItem(gp, s.getName(), s.getId() + "", s);
					
					view.addOverlay(storeOverlay);
					
					break;
				case WHAT_DOWNLOAD:
					bindData();
					view.hideDialog();
					break;
				case WHAT_FINISHEDSEARCH:
					view.displayOverlays();
					break;
			}
		}
	};
	
	public MapStoresController(IMapStoresView view) {
		Log.v(Program.LOG, "MapStoresController._construct()");
		
		this.view = view;
		
		userLocation = new LocationFinder(view.getContext(), view.getMapView());
		userLocation.enableMyLocation();
		userLocation.enableCompass();
		userLocation.runOnFirstFix(locationFixed);
		
		view.addOverlay(userLocation);
		
		database = new Database(view.getContext());
	}

	public void cleanup() {
		Log.v(Program.LOG, "MapStoresController.cleanup()");
		
		userLocation.disableCompass();
		userLocation.disableMyLocation();
	}
	
	public void bindData() {
		Log.v(Program.LOG, "MapStoresController.bindData()");
		
		new Thread(new DatabaseSearcher()).start();
	}
	
	public void refreshStores() {
		Log.v(Program.LOG, "MapStoresController.refreshStores()");
		
		view.showDialog();
		new Thread(new StoreRefresher()).start();
	}

	public void setUserOverlay() {
		view.addOverlay(userLocation);
	}

	public void centerMap() {
		view.getMapController().animateTo(userLocation.getMyLocation());
	}
}
