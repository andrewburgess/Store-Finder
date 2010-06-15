package com.parse3.storefinder.models;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parse3.storefinder.Program;

public class Database {
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, Program.DatabaseInfo.NAME, null, Program.DatabaseInfo.VERSION);
			
			Log.v(Program.LOG, "DatabaseHelper._construct()");
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v(Program.LOG, "DatabaseHelper.onCreate()");
			
			Log.i(Program.LOG, "Creating database");
			String sql = "create table store (" +
								"_id integer primary key autoincrement, " +
								"storeid text, " +
								"name text, " +
								"address text," +
								"city text, " +
								"state text, " +
								"zip text, " +
								"phone text, " +
								"latitude real, " +
								"longitude real); " +
						"create table product (" + 
								"id integer primary key autoincrememnt, " +
								"name text, " +
								"description text); " +
						"create table store_products (" +
								"store_id int, " +
								"product_id); ";
			
			db.execSQL(sql);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(Program.LOG, "Upgrading database");
			
		}
	}
	
	private DatabaseHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;
	
	public Database(Context context) {
		Log.v(Program.LOG, "Database._construct()");
		
		this.context = context;
	}
	
	public Database open() {
		Log.v(Program.LOG, "Database.open()");
		
		if (db != null && db.isOpen()) {
			close();
		}
		
		try {
			dbHelper = new DatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			return this;
		} catch (SQLException e) {
			Log.e(Program.LOG, "ERROR: Database - " + e.getMessage());
			return null;
		}
	}
	
	public void close() {
		Log.v(Program.LOG, "Database.close()");
		
		dbHelper.close();
	}
	
	public SQLiteDatabase getDatabase() {
		Log.v(Program.LOG, "Database.getDatabase()");
		
		if (db.isOpen())
			return db;
		else {
			open();
			return db;
		}
	}
}
