package com.parse3.storefinder.views.interfaces;

import android.content.Context;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.parse3.storefinder.StoreOverlayItem;

public interface IMapStoresView {

	Context getContext();
	MapView getMapView();
	MapController getMapController();
	void addOverlay(Overlay overlay);
	void addOverlay(StoreOverlayItem overlayItem);
	void hideDialog();
	void showDialog();
	void displayOverlays();
	void mapClick();
	void centerOverlays();

}
