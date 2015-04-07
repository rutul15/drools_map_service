package com.ctc.address;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;



public class Building {
	
	private String displayAddress;
	
	private String placeId;

	private List<String> tokens;
	
	public String getStreetName() {
		String streetPart = StringUtils.join(tokens.subList(1, tokens.size()), " ");
		return streetPart;
	}
	public String getNumberPart() {
		return tokens.get(0);
	}
	public List<String> getTokens() {
		return tokens;
	}
	public String getToken(int i) {
		return i < tokens.size() ? tokens.get(i) : "";
	}
	public String getDisplayAddress() {
		return displayAddress;
	}

	public void setDisplayAddress(String displayAddress) {
		this.displayAddress = displayAddress.trim().toUpperCase();
		this.tokens = Arrays.asList(this.getDisplayAddress().split(" "));
		
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	@Override
	public String toString() {
		return "Building [displayAddress=" + displayAddress + ", placeId="
				+ placeId + "]";
	}
}
