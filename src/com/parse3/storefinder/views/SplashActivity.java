package com.parse3.storefinder.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;
import com.parse3.storefinder.controllers.SplashScreenController;
import com.parse3.storefinder.views.interfaces.IHomeView;

public class SplashActivity extends Activity implements IHomeView {
	private SplashScreenController controller;	
	private Dialog progressDialog;
	
    /**
     * Called when the activity is first started
     * 
     * @param savedInstanceState	Any data stored from a previous launch
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        Log.v(Program.LOG, "SplashActivity.onCreate()");
        
        controller = new SplashScreenController(this);
    }
    
	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public void hideDialog() {
		progressDialog.dismiss();		
	}

	@Override
	public void showDialog() {
		progressDialog = ProgressDialog.show(this, "", "Initializing application...", true);		
	}
}