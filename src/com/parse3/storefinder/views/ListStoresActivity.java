package com.parse3.storefinder.views;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.controllers.ListStoresController;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.views.interfaces.IListStoresView;

public class ListStoresActivity extends ListActivity implements IListStoresView {
	private static final int OPTION_REFRESH = 0;
	private static final int OPTION_SETTINGS = 1;
	
	private ListStoresController controller;
	private StoreListAdapter adapter;
	private Dialog dialog;
	
	protected class StoreListAdapter extends ArrayAdapter<Store> {
		public StoreListAdapter(Context context, int textViewResourceId){
			super(context, textViewResourceId);
		}
		
		public StoreListAdapter(Context context, int textViewResourceId, List<Store> objects){
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.store_list_row, parent, false);
			
			TextView tvDistance = (TextView)view.findViewById(R.id.distance);
			TextView tvName = (TextView)view.findViewById(R.id.name);
			TextView tvAddress = (TextView)view.findViewById(R.id.address);
			TextView tvPhone = (TextView)view.findViewById(R.id.phone);
			TextView tvCity = (TextView)view.findViewById(R.id.citystate);
			
			Store s = this.getItem(position);
			
			tvDistance.setText(Program.round(s.getDistance(), 2) + " miles");
			tvName.setText(s.getName());
			tvAddress.setText(s.getAddress());
			tvPhone.setText(s.getPhone());
			tvCity.setText(s.getCitystate());
		
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			Store s = this.getItem(position);
			return s.getId();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v(Program.LOG, "ListStoresActivity.onCreate()");
		
		setContentView(R.layout.list_stores);
		
		adapter = new StoreListAdapter(this, R.id.name);
		setListAdapter(adapter);
		
		controller = new ListStoresController(this);
		
		controller.bindData();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v(Program.LOG, "ListStoresActivity.onCreateOptionsMenu()");
		
		menu.add(0, OPTION_REFRESH, 0, "Refresh").setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(0, OPTION_SETTINGS, 1, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(Program.LOG, "ListStoresActivity.onOptionsItemSelected()");
		
		switch (item.getItemId()) {
			case OPTION_REFRESH:
				adapter.clear();
				controller.refreshStores();
				return true;
			default:
				
				return false;
		}
	}

	@Override
	public Context getContext() {
		Log.v(Program.LOG, "ListStoresActivity.getContext()");
		
		return this;
	}
	
	@Override
	public void addStoreToList(Store store) {
		adapter.add(store);
		adapter.sort(store);
	}

	@Override
	public void showDialog() {
		dialog = ProgressDialog.show(this, "", "Refreshing stores...", true);		
	}
	
	@Override
	public void hideDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}
}
