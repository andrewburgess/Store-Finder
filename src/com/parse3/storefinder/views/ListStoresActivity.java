package com.parse3.storefinder.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.controllers.ListStoresController;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.views.interfaces.IListStoresView;

public class ListStoresActivity extends ListActivity implements IListStoresView {
	private ListStoresController controller;
	
	/*protected class StoreListAdapter extends CursorAdapter {
		public StoreListAdapter(Context context, Cursor c) {
			super(context, c);
		}
		
		@Override
		public void bindView(View view, Context context, Cursor c) {
			String name = c.getString(1);
			String address = c.getString(2);
			String citystate = c.getString(3) + ", " + c.getString(4) + " " + c.getString(5);
			String phone = c.getString(6);
			double latitude = c.getDouble(7);
			double longitude = c.getDouble(8);
			
			TextView tvDistance = (TextView)view.findViewById(R.id.distance);
			TextView tvName = (TextView)view.findViewById(R.id.name);
			TextView tvAddress = (TextView)view.findViewById(R.id.address);
			TextView tvPhone = (TextView)view.findViewById(R.id.phone);
			TextView tvCity = (TextView)view.findViewById(R.id.citystate);
			
			float[] results = new float[3];
			Location.distanceBetween(controller.getLocation().getLatitude(), controller.getLocation().getLongitude(), 
									 latitude, longitude, results);
			double distance = results[0] * Program.MILES_PER_METER;
			
			tvDistance.setText(Program.round(distance, 2) + " miles");
			tvName.setText(name);
			tvAddress.setText(address);
			tvPhone.setText(phone);
			tvCity.setText(citystate);
		}
		
		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.store_list_row, parent, false);
			
			view.setId(c.getInt(0));
			
			return view;
		}
	}*/
	
	protected class StoreListAdapter extends ArrayAdapter<Store> {
		public StoreListAdapter(Context context, int textViewResourceId){
			super(context, textViewResourceId);
		}
		
		public StoreListAdapter(Context context, int textViewResourceId, List<Store> objects){
			super(context, textViewResourceId, objects);
		}
	}
	
	private StoreListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v(Program.LOG, "ListStoresActivity.onCreate()");
		
		setContentView(R.layout.list_stores);
		
		controller = new ListStoresController(this);
		
		controller.bindData();
	}

	@Override
	public Context getContext() {
		Log.v(Program.LOG, "ListStoresActivity.getContext()");
		
		return this;
	}

	/*@Override
	public void setCursor(Cursor c) {
		setListAdapter(new StoreListAdapter(this, c));
	}*/
	
	@Override
	public void setArray(ArrayList<Store> stores) {
		adapter = new StoreListAdapter(this, R.layout.temp, stores);
		setListAdapter(adapter);
	}

	@Override
	public void initializeList() {
		adapter = new StoreListAdapter(this, R.layout.temp);
		setListAdapter(adapter);	
	}
	
	@Override
	public void addStoreToList(Store store) {
		adapter.add(store);
		adapter.sort(store);
	}
}
