package com.parse3.storefinder;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.parse3.storefinder.models.Store;

public class StoreOverlayItem extends OverlayItem {
	private Store store;
	
	public StoreOverlayItem(GeoPoint point, String title, String snippet, Store store) {
		super(point, title, snippet);
		this.store = store;
	}
	
	public Store getStore() {
		return store;
	}

}
