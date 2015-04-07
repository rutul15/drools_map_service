package com.ctc.dao;

import java.util.HashSet;
import java.util.Set;

import com.ctc.address.Building;
import com.ctc.address.UserAddress;

public class FactBook {
	
	private Set<Building> buildings = new HashSet<Building>();
	
	private Set<UserAddress> userAddresses = new HashSet<UserAddress>();
	
	public Set<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(Set<Building> buildings) {
		this.buildings = buildings;
	}

	public Set<UserAddress> getUserAddresses() {
		return userAddresses;
	}

	public void setUserAddresses(Set<UserAddress> userAddresses) {
		this.userAddresses = userAddresses;
	}

}
