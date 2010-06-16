package com.parse3.storefinder.controllers;

import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.StoreFinderApplication;
import com.parse3.storefinder.models.DatabaseSearcher;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.models.StoreRefresher;
import com.parse3.storefinder.views.interfaces.IListStoresView;

public class ListStoresController {
	private IListStoresView view;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DatabaseSearcher.WHAT_SEARCHDB:
					Store s = (Store) msg.getData().getSerializable("store");
					view.addStoreToList(s);
					
					break;
				case StoreRefresher.WHAT_DOWNLOAD:
					bindData();
					view.hideDialog();
					break;
				default:
					break;
			}
		}
	};
	
	public ListStoresController(IListStoresView view) {
		Log.v(Program.LOG, "ListStoresController._construct()");
		
		this.view = view;
	}

	public void bindData() {
		Log.v(Program.LOG, "ListStoresController.bindData()");
		
		new Thread(new DatabaseSearcher(view.getContext(), handler)).start();
	}
	
	public Location getLocation() {
		Log.v(Program.LOG, "ListStoresController.getLocation()");
		
		Location l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (l == null) {
			l = StoreFinderApplication.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		return l;
	}

	public void refreshStores() {
		Log.v(Program.LOG, "ListStoresController.refreshStores()");
		
		view.showDialog();
		new Thread(new StoreRefresher(view.getContext(), handler)).start();
	}
}
