package com.parse3.storefinder.views.interfaces;

import android.content.Context;

import com.parse3.storefinder.models.Store;

public interface IListStoresView {
	Context getContext();
	void addStoreToList(Store store);
	void showDialog();
	void hideDialog();
}
