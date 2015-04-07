package com.ctc.address;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class UserAddress {
	
	private String originalAddress;

	private String resolvedAddress;
	
	private List<String> tokens;
	
	public String getToken(int k) {
		if(k < tokens.size())
			return tokens.get(k);
		else
			return "";
	}
	public List<String> getTokens() {
		return tokens;
	}
	public String getStreetPart() {
		if(tokens.size() >= 2) {
			String streetPart = StringUtils.join(tokens.subList(1, tokens.size()), " ");
			return streetPart;
		} else {
			return "";
		}
	}
	public String getStreetWithoutSuffix() {
		if(tokens.size() >= 3) {
			String streetPart = StringUtils.join(tokens.subList(1, tokens.size()-1), " ");
			return streetPart;
		} else {
			return "";
		}
	}
	
	public String getNumberPart() {
		return tokens.get(0);
	}
	
	public String getOriginalAddress() {
		return originalAddress;
	}

	public void setOriginalAddress(String originalAddress) {
		this.originalAddress = originalAddress.trim().toUpperCase();
		this.tokens = Arrays.asList(this.originalAddress.split("[\\s\\.\\,]+"));
	}

	public String getResolvedAddress() {
		return resolvedAddress;
	}

	public void setResolvedAddress(String resolvedAddress) {
		System.out.println("RESOLVED: " + originalAddress + " => " + resolvedAddress);
		this.resolvedAddress = resolvedAddress;
	}

	@Override
	public String toString() {
		return "UserAddress [originalAddress=" + originalAddress
				+ ", resolvedAddress=" + resolvedAddress + "] " + this.hashCode();
	}
	
	

}
