package com.parse3.storefinder.models;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse3.storefinder.Program;

public class StoreRefresher implements Runnable {
	public static final int WHAT_DOWNLOAD = 1;
	
	private Context context;
	private Handler handler;
	private Location location;
	
	public StoreRefresher(Context context, Handler handler, Location location) {
		Log.v(Program.LOG, "StoreRefresher._construct()");
		
		this.context = context;
		this.handler = handler;
		this.location = location;
	}
	
	public void run() {
		Log.v(Program.LOG, "StoreRefresher.run()");
		
		Message m = new Message();
		
		StoreDownloader downloader = new StoreDownloader(context);
		downloader.downloadStores(location);
		
		m.what = WHAT_DOWNLOAD;
				
		handler.sendMessage(m);
	}
}