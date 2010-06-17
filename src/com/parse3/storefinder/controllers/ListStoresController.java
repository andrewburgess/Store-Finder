package com.parse3.storefinder.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.StoreFinderApplication;
import com.parse3.storefinder.models.Database;
import com.parse3.storefinder.models.DatabaseSearcher;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.models.StoreRefresher;
import com.parse3.storefinder.views.interfaces.IListStoresView;

public class ListStoresController {
	private IListStoresView view;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.v(Program.LOG, "ListStoresController.handler.handleMessage()");
			
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

	public Store getStore(int id) {
		SQLiteDatabase db = new Database(view.getContext()).open().getDatabase();
		
		Cursor c = db.query("store", new String[] {"_id", "name", "address", "city", "state", "zip", "phone"}, 
							"_id = " + id, 
							null, null, null, null);
		
		c.moveToFirst();
		if (c.getCount() == 0)
			return null;
		
		Store store = new Store();
		store.setId(c.getInt(0));
		store.setName(c.getString(1));
		store.setAddress(c.getString(2));
		store.setCitystate(c.getString(3) + ", " + c.getString(4) + " " + c.getString(5));
		store.setPhone(c.getString(6));
		
		return store;
	}
}
