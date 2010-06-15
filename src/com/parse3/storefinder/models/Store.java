package com.parse3.storefinder.models;

import java.io.Serializable;
import java.util.Comparator;

import com.parse3.storefinder.Program;

import android.location.Location;

public class Store implements Comparator<Store>, Serializable {
	private static final long serialVersionUID = -2836075365060996971L;
	private String name;
	private String address;
	private String citystate;
	private String phone;
	private Location location;
	private double distance;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCitystate() {
		return citystate;
	}
	public void setCitystate(String citystate) {
		this.citystate = citystate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return name + " (" + Program.round(distance, 2) + " miles)";
	}
	@Override
	public int compare(Store object1, Store object2) {
		if (object1.distance > object2.distance)
			return 1;
		else if (object1.distance < object2.distance)
			return -1;
		else
			return 0;
	}
}
