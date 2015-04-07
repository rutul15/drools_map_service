package com.ctc.address;

public class StreetAlias {
	
	private String alias;
	
	private String name;
	
	public StreetAlias(String alias, String name) {
		this.alias = alias;
		this.name = name;
		System.out.println(this.toString());
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "StreetAlias [alias=" + alias + ", name=" + name + "]";
	}

}
