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
import com.parse3.storefinder.views.interfaces.ISplashView;

public class SplashActivity extends Activity implements ISplashView {
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
    public void onPause() {
    	super.onPause();
    	
    	controller.cleanUp();
    }
    
	@Override
	public Context getContext() {
		Log.v(Program.LOG, "SplashActivity.getContext()");
		
		return this;
	}

	@Override
	public void hideDialog() {
		Log.v(Program.LOG, "SplashActivity.hideDialog()");
		
		progressDialog.dismiss();		
	}

	@Override
	public void showDialog() {
		Log.v(Program.LOG, "SplashActivity.showDialog()");
		
		progressDialog = ProgressDialog.show(this, "", "Initializing application...", true);
	}
}