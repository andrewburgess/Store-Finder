package com.parse3.storefinder.controllers;

import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.views.interfaces.IMapStoresView;

public class MapStoresController {
	private IMapStoresView view;
	
	public MapStoresController(IMapStoresView view) {
		Log.v(Program.LOG, "MapStoresController._construct()");
		
		this.view = view;
	}
}
