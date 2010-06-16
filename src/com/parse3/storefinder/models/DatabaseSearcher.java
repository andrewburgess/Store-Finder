package com.parse3.storefinder.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.StoreFinderApplication;

public class DatabaseSearcher implements Runnable {
	public static final int WHAT_SEARCHDB = 0;
	public static final int WHAT_FINISHEDSEARCH = 2;
	
	private static final double MILES_PER_LATLONG = 50;	//Extremely not even close, but should give us a rough box to work within
	
	private Database database;
	private SQLiteDatabase db;
	private Context context;
	private Handler handler;
	
	public DatabaseSearcher(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		
		database = new Database(context);
	}

	@Override
	public void run() {
		double radius = PreferenceManager.getDefaultSharedPreferences(context).getInt(context.getResources().getString(R.string.radius), 20);
		
		Location l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (l == null) {
			l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		double lat = l.getLatitude();
		double lon = l.getLongitude();
		
		database = database.open();
		db = database.getDatabase();
		
		Cursor c = db.query("store", new String[] {"_id", "name", "address", "city", "state", "zip", "phone", "latitude", "longitude"}, 
							"latitude > " + (lat - radius / MILES_PER_LATLONG) + " AND latitude < " + (lat + radius / MILES_PER_LATLONG) + 
							" AND longitude > " + (lon - radius / MILES_PER_LATLONG) + " AND longitude < " + (lon + radius / MILES_PER_LATLONG), 
							null, null, null, null);
		
		double latitude, longitude;
		
		c.moveToFirst();
		if (c.getCount() > 0) {
			do {
				latitude = c.getDouble(7);
				longitude = c.getDouble(8);
				float[] results = new float[3];
				Location.distanceBetween(lat, lon, latitude, longitude, results);
				double distance = results[0] * Program.MILES_PER_METER;
				
				if (distance > radius)				//Make user customizable search distance
					continue;
				
				Store store = new Store();
				store.setId(c.getInt(0));
				store.setName(c.getString(1));
				store.setAddress(c.getString(2));
				store.setCitystate(c.getString(3) + ", " + c.getString(4) + " " + c.getString(5));
				store.setPhone(c.getString(6));
				store.setDistance(distance);
				
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putSerializable("store", store);
				msg.setData(b);
				msg.what = WHAT_SEARCHDB;
				handler.sendMessage(msg);
			} while (c.moveToNext());
		}
		
		c.close();
		database.close();
		
		Message finish = new Message();
		finish.what = WHAT_FINISHEDSEARCH;
		handler.sendMessage(finish);
	}
}