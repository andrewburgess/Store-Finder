package com.parse3.storefinder.views.interfaces;

import java.util.ArrayList;

import android.content.Context;

import com.parse3.storefinder.models.Store;

public interface IListStoresView {

	Context getContext();

	//void setCursor(Cursor c);
	
	void setArray(ArrayList<Store> stores);

	void initializeList();

	void addStoreToList(Store store);

}
