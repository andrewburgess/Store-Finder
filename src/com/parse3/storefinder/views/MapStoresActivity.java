package com.parse3.storefinder.views;

import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.MapActivity;
import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.controllers.MapStoresController;
import com.parse3.storefinder.views.interfaces.IMapStoresView;

public class MapStoresActivity extends MapActivity implements IMapStoresView {
	private MapStoresController controller;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v(Program.LOG, "MapStoresActivity.onCreate()");
		
		setContentView(R.layout.map_stores);
		
		controller = new MapStoresController(this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
