package com.parse3.storefinder;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.parse3.storefinder.models.Store;

public class StoreOverlayItem extends OverlayItem {
	private Store store;
	
	public StoreOverlayItem(GeoPoint point, String title, String snippet, Store store) {
		super(point, title, snippet);
		
		Log.v(Program.LOG, "StoreOverlayItem._construct()");
		
		this.store = store;
	}
	
	public Store getStore() {
		Log.v(Program.LOG, "StoreOverlayItem.getStore()");
		
		return store;
	}

}
