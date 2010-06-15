package com.parse3.storefinder.views;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.StoreFinderApplication;

public class FinderTabsActivity extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v(Program.LOG, "FinderTabsActivity.onCreate()");
		
		setContentView(R.layout.finder_tabs);
		
		Resources res = getResources();
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent().setClass(this, ListStoresActivity.class);
		
		spec = tabHost.newTabSpec("list").setIndicator("Store List", res.getDrawable(android.R.drawable.ic_menu_view)).setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, MapStoresActivity.class);
		spec = tabHost.newTabSpec("map").setIndicator("Map Stores", res.getDrawable(android.R.drawable.ic_menu_mapmode)).setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		StoreFinderApplication.stopLocationService();
	}
}
