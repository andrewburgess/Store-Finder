package com.parse3.storefinder.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;

public class SettingsActivity extends PreferenceActivity {	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
}
