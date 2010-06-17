package com.parse3.storefinder;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class StoreFinderApplication extends Application implements LocationListener {
	private static LocationManager locationManager;
	private static StoreFinderApplication app;
	private static boolean listening;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.v(Program.LOG, "StoreFinderApplication.onCreate()");
		
		app = this;
		
		if (listening == false) {
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			listening = true;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public static LocationManager getLocationManager() {
		Log.v(Program.LOG, "StoreFinderApplication.getLocationManager()");
		
		return locationManager;
	}
	
	public static void stopLocationService() {
		Log.v(Program.LOG, "StoreFinderApplication.stopLocationService()");
		
		if (listening) {
			locationManager.removeUpdates(app);
			listening = false;
		}
	}

	public static Location getLastKnownLocation() {
		Log.v(Program.LOG, "StoreFinderApplication.getLastKnownLocation()");
		
		Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (l == null) {
			l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		return l;
	}

	public static void startLocationService() {
		Log.v(Program.LOG, "StoreFinderApplication.startLocationService()");
		
		if (listening == false) {
			locationManager = (LocationManager)app.getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, app);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, app);
			listening = true;
		}	
	}
}
