package com.parse3.storefinder;

import android.content.Context;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class LocationFinder extends MyLocationOverlay {

	public LocationFinder(Context context, MapView mapView) {
		super(context, mapView);
	}

}
