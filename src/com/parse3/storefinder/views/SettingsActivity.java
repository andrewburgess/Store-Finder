package com.parse3.storefinder.views;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;

public class SettingsActivity extends PreferenceActivity {	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(Program.LOG, "SettingsActivity.onCreate()");
		
		addPreferencesFromResource(R.xml.preferences);
	}
}
