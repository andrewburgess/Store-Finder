package com.parse3.storefinder.views.controls;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.parse3.storefinder.R;
import com.parse3.storefinder.models.Database;

public class DeleteStoresPreference extends DialogPreference {
	private Context context;
	
	public DeleteStoresPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
	}
	
	@Override
	protected View onCreateDialogView() {
		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_delete_stores, null, false);
		
		this.onDialogClosed(false);
		
		return view;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			SQLiteDatabase db = new Database(context).open().getDatabase();
			db.delete("store", "1=1", null);
			db.close();
		}
	}

}
