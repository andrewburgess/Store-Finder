package com.parse3.storefinder.controllers;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.parse3.storefinder.LocationFinder;
import com.parse3.storefinder.Program;
import com.parse3.storefinder.StoreOverlayItem;
import com.parse3.storefinder.models.DatabaseSearcher;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.models.StoreRefresher;
import com.parse3.storefinder.views.interfaces.IMapStoresView;

public class MapStoresController {
	private static final int MAP_SCALE = 1000000;
	
	private IMapStoresView view;
	private LocationFinder userLocation;
	
	private Runnable locationFixed = new Runnable() {

		@Override
		public void run() {
			Log.v(Program.LOG, "MapStoresController.locationFixed.run()");
			
			view.getMapController().animateTo(userLocation.getMyLocation());
			view.getMapController().setZoom(15);
		}
		
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DatabaseSearcher.WHAT_SEARCHDB:
					Store s = (Store) msg.getData().getSerializable("store");
					GeoPoint gp = new GeoPoint((int)(s.getLocation().getLatitude() * MAP_SCALE), (int)(s.getLocation().getLongitude() * MAP_SCALE));
					StoreOverlayItem storeOverlay = new StoreOverlayItem(gp, s.getName(), s.getId() + "", s);
					
					view.addOverlay(storeOverlay);
					
					break;
				case StoreRefresher.WHAT_DOWNLOAD:
					bindData();
					view.hideDialog();
					break;
				case DatabaseSearcher.WHAT_FINISHEDSEARCH:
					view.displayOverlays();
					break;
				default:
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
	}

	public void cleanup() {
		Log.v(Program.LOG, "MapStoresController.cleanup()");
		
		userLocation.disableCompass();
		userLocation.disableMyLocation();
	}
	
	public void bindData() {
		Log.v(Program.LOG, "MapStoresController.bindData()");
		
		new Thread(new DatabaseSearcher(view.getContext(), handler)).start();
	}
	
	public void refreshStores() {
		Log.v(Program.LOG, "MapStoresController.refreshStores()");
		
		view.showDialog();
		new Thread(new StoreRefresher(view.getContext(), handler)).start();
	}

	public void setUserOverlay() {
		view.addOverlay(userLocation);
	}

	public void centerMap() {
		view.getMapController().animateTo(userLocation.getMyLocation());
	}
}
