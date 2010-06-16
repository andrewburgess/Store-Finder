package com.parse3.storefinder.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.maps.MapView;
import com.parse3.storefinder.Program;
import com.parse3.storefinder.R;

public class FinderMapView extends MapView {
	private long lastTouchTime = -1;
	private int lastAction = 0;
	private boolean panned = false;
	
	public FinderMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d(Program.LOG, ev.getAction() + "");
		
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			long thisTime = System.currentTimeMillis();
			if (thisTime - lastTouchTime < 250) {
				this.getController().zoomInFixing((int)ev.getX(), (int)ev.getY());
				lastTouchTime = -1;
			} else {
				lastTouchTime = thisTime;
			}
			
			lastAction = MotionEvent.ACTION_DOWN;
		}
		
		
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d(Program.LOG, ev.getAction() + "");
		
		if (ev.getAction() == MotionEvent.ACTION_MOVE && lastAction == MotionEvent.ACTION_MOVE) {
			panned = true;
		}
		
		if (ev.getAction() == MotionEvent.ACTION_UP){
			if (!panned)
				((View)getParent()).findViewById(R.id.popup).setVisibility(View.GONE);
			else
				panned = false;
		}
		
		lastAction = ev.getAction();
		
		return super.onTouchEvent(ev);
	}
}
