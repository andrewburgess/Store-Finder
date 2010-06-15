package com.parse3.storefinder.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.models.Database;
import com.parse3.storefinder.models.StoreDownloader;
import com.parse3.storefinder.views.FinderTabsActivity;
import com.parse3.storefinder.views.interfaces.ISplashView;

public class SplashScreenController {
	private ISplashView view;
	private Database db;
	
	private Thread downloadStoresThread = new Thread() {
		public void run() {
			Log.i(Program.LOG, "downloadStoresThread.run()");
			
			Message m = new Message();
			
			StoreDownloader downloader = new StoreDownloader(view.getContext());
			downloader.downloadStores();
			
			SharedPreferences prefs = view.getContext().getSharedPreferences(Program.PREFS, 0);
			
			//Whether we're getting stores for the first time, or just refreshing the stores in the
			//database
			if (!prefs.contains(Program.Preferences.SETUP))
				m.what = 0;
			else
				m.what = 1;
			
			Editor editor = prefs.edit();
			editor.putBoolean(Program.Preferences.SETUP, true);
			editor.commit();
			
			handler.sendMessage(m);
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.v(Program.LOG, "handler.handleMessage()");
			
			switch (msg.what) {
				case 0:
					view.hideDialog();
					
					//TODO: Start up new activity
					
					Intent intent = new Intent(view.getContext(), FinderTabsActivity.class);
					view.getContext().startActivity(intent);
					
					view.finish();
					
					break;
				case 1:
					//Nothing really to do in this case
					break;
			}
		}
	};
	
	public SplashScreenController(ISplashView view) {
		Log.v(Program.LOG, "SplashScreenController._constructor()");
		
		this.view = view;
		
		db = new Database(view.getContext());
		
		//Set up database
		db.open();
		
		SharedPreferences prefs = view.getContext().getSharedPreferences(Program.PREFS, 0);
		downloadStoresThread.start();
		if (!prefs.contains(Program.Preferences.SETUP)) {
			view.showDialog();
		} else {
			Intent intent = new Intent(view.getContext(), FinderTabsActivity.class);
			view.getContext().startActivity(intent);
			
			view.finish();
		}
	}
	
	public void cleanUp() {
		Log.v(Program.LOG, "SplashScreenController.cleanUp()");
		
		db.close();
	}
}
