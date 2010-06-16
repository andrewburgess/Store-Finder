package com.parse3.storefinder.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.parse3.storefinder.Program;

public class StoreDownloader {
	private Context context;
	private Database database;
	private SQLiteDatabase db;
	
	public StoreDownloader(Context context) {
		Log.v(Program.LOG, "StoreDownloader._construct()");
		
		this.context = context;
		
		database = new Database(context).open();
		db = database.getDatabase();
	}
	
	public void downloadStores() {
		Log.v(Program.LOG, "StoreDownloader.downloadStores()");
		
		String data = "";
		try {
			URL u = new URL(Program.StoreDownloaderInfo.URL);
			data = readData(openConnection(u));
		} catch (MalformedURLException e) {
			Log.e(Program.LOG, "ERROR: downloadStores - " + e.getMessage());
		}
		
		try {
			ContentValues cv;
			Geocoder gc = new Geocoder(context, Locale.getDefault());
			JSONObject json = new JSONObject(data);
			JSONArray jStores = json.getJSONArray("store");
			for (int i = 0; i < jStores.length(); i++) {
				JSONObject jStore = jStores.getJSONObject(i);
				
				Cursor cursor = db.query("store", new String[] {"storeid"}, "storeid = ?", 
												new String[] {jStore.getString("storeid")}, 
												null, null, null);
				
				cursor.moveToFirst();
				if (cursor.getCount() > 0) {
					cursor.close();
					continue;
				}
				
				cursor.close();
				
				cv = new ContentValues();
				cv.put("storeid", jStore.getString("storeid"));
				cv.put("name", jStore.getString("name"));
				cv.put("address", jStore.getString("address"));
				cv.put("city", jStore.getString("city"));
				cv.put("state", jStore.getString("state"));
				cv.put("zip", jStore.getString("zip"));
				cv.put("phone", jStore.getString("phone"));
				
				try {
					Address address = gc.getFromLocationName(jStore.getString("address") + ", " + 
															 jStore.getString("city") + ", " +
															 jStore.getString("state") + " " + 
															 jStore.getString("zip"), 1).get(0);
					cv.put("latitude", address.getLatitude());
					cv.put("longitude", address.getLongitude());
				} catch (IOException e) {
					Log.e(Program.LOG, "ERROR: geocoder.getFromLocationName() - " + e.getMessage());
				}
				
				db.insert("store", null, cv);
			}
			
			database.close();
		} catch (JSONException e) {
			Log.e(Program.LOG, "ERROR: downloadStores - " + e.getMessage());
		}
	}
	
	private InputStream openConnection(URL url) {
		Log.v(Program.LOG, "StoreDownloader.openConnection()");
		
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			connection.setAllowUserInteraction(false);
			connection.setRequestMethod("GET");
			
			connection.connect();
			
			int response = connection.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				return (InputStream)connection.getInputStream();
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			Log.e(Program.LOG, e.getMessage());
		} catch (IOException e) {
			Log.e(Program.LOG, e.getMessage());
		}
		
		return null;
	}
	
	private String readData(InputStream in) {
		Log.v(Program.LOG, "StoreDownloader.readData()");
		
		InputStreamReader reader = new InputStreamReader(in);
		int charRead;
		String data = "";
		char[] buffer = new char[Program.StoreDownloaderInfo.BUFFER];
		
		try {
			while ((charRead = reader.read(buffer)) > 0) {
				data += String.copyValueOf(buffer, 0, charRead);
				buffer = new char[Program.StoreDownloaderInfo.BUFFER];
			}
		} catch (IOException e) {
			Log.e(Program.LOG, e.getMessage());
		}
		
		return data;
	}
}
