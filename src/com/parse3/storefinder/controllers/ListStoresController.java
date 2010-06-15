package com.parse3.storefinder.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.StoreFinderApplication;
import com.parse3.storefinder.models.Database;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.views.interfaces.IListStoresView;

public class ListStoresController {
	private IListStoresView view;
	private Database database;
	private SQLiteDatabase db;
	
	private Thread searchStoresThread = new Thread() {
		public void run() {
			Location l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if (l == null) {
				l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			
			double lat = l.getLatitude();
			double lon = l.getLongitude();
			
			Cursor c = db.query("store", new String[] {"_id", "name", "address", "city", "state", "zip", "phone", "latitude", "longitude"}, 
								"latitude > " + (lat - 0.5) + " AND latitude < " + (lat + 0.5) + 
								" AND longitude > " + (lon - 0.5) + " AND longitude < " + (lon + 0.5), 
								null, null, null, null);
			
			//ArrayList<Store> stores = new ArrayList<Store>();
			
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
				store.setName(c.getString(1));
				store.setAddress(c.getString(2));
				store.setCitystate(c.getString(3) + ", " + c.getString(4) + " " + c.getString(5));
				store.setPhone(c.getString(6));
				store.setDistance(distance);
				
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putSerializable("store", store);
				msg.setData(b);
				msg.what = 0;
				handler.sendMessage(msg);
			} while (c.moveToNext());
			
			c.close();
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					Store s = (Store) msg.getData().getSerializable("store");
					view.addStoreToList(s);
					
					break;
			}
		}
	};
	
	public ListStoresController(IListStoresView view) {
		Log.v(Program.LOG, "ListStoresController._construct()");
		
		this.view = view;
		
		database = new Database(view.getContext()).open();
		db = database.getDatabase();
	}

	public void bindData() {
		Log.v(Program.LOG, "ListStoresController.bindData()");
		
		view.initializeList();
		searchStoresThread.run();
		
		/*Location l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (l == null) {
			l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		double lat = l.getLatitude();
		double lon = l.getLongitude();
		
		Cursor c = db.query("store", new String[] {"_id", "name", "address", "city", "state", "zip", "phone", "latitude", "longitude"}, 
							"latitude > " + (lat - 0.5) + " AND latitude < " + (lat + 0.5) + 
							" AND longitude > " + (lon - 0.5) + " AND longitude < " + (lon + 0.5), 
							null, null, null, null);
		
		ArrayList<Store> stores = new ArrayList<Store>();
		
		double latitude, longitude;
		
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			latitude = c.getDouble(7);
			longitude = c.getDouble(8);
			float[] results = new float[3];
			Location.distanceBetween(lat, lon, latitude, longitude, results);
			double distance = results[0] * Program.MILES_PER_METER;
			
			if (distance > 15)				//Make user customizable search distance
				continue;
			
			
		}*/
		
		//view.setCursor(c);
	}
	
	public Location getLocation() {
		Location l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (l == null) {
			l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		return l;
	}
}
