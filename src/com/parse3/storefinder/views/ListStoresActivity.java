package com.parse3.storefinder.views;

import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.controllers.ListStoresController;
import com.parse3.storefinder.models.Store;
import com.parse3.storefinder.views.interfaces.IListStoresView;

public class ListStoresActivity extends ListActivity implements IListStoresView {
	private static final int OPTION_REFRESH = 0;
	private static final int OPTION_SETTINGS = 1;
	
	private static final int LIST = 0;
	private static final int LIST_NAVIGATE = 0;
	private static final int LIST_CALL = 1;
	
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
			tvPhone.setText(PhoneNumberUtils.formatNumber(s.getPhone()));
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
		
		Log.i(Program.LOG, "ListStoresActivity.onCreate()");
		
		setContentView(R.layout.list_stores);
		
		adapter = new StoreListAdapter(this, R.id.name);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
		
		controller = new ListStoresController(this);
		controller.bindData();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(Program.LOG, "ListStoresActivity.onCreateOptionsMenu()");
		
		menu.add(0, OPTION_REFRESH, 0, "Refresh").setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(0, OPTION_SETTINGS, 1, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(Program.LOG, "ListStoresActivity.onActivityResult()");
		
		adapter.clear();
		controller.bindData();
	}
	
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {		
		menu.setHeaderTitle("Store Options");
		menu.add(LIST, LIST_NAVIGATE, LIST_NAVIGATE, "Navigate Here");
		menu.add(LIST, LIST_CALL, LIST_CALL, "Call Store");
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		Store s = controller.getStore((int)info.id);
		
		if (s == null)
			return false;
		
		switch (item.getItemId()) {
			case LIST_NAVIGATE:
				Uri navigateUri = Uri.parse("google.navigation:q=" + Uri.encode(s.getAddress() + " " + s.getCitystate()));
				startActivity(new Intent(Intent.ACTION_VIEW, navigateUri));
				
				return true;
			case LIST_CALL:
				Uri callUri = Uri.parse("tel:" + Uri.encode(s.getPhone()));
				startActivity(new Intent(Intent.ACTION_DIAL, callUri));				
				
				return true;
			default:
				
				return false;
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(Program.LOG, "ListStoresActivity.onOptionsItemSelected()");
		
		switch (item.getItemId()) {
			case OPTION_REFRESH:
				adapter.clear();
				controller.refreshStores();
				return true;
			case OPTION_SETTINGS:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivityForResult(i, 0);
				
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
