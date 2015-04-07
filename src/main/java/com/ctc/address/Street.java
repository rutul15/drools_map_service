package com.ctc.address;

public class Street {

	private String prefixPart;

	private String stemmedPart;

	private String suffixPart;
	
	private String name;

	@SuppressWarnings("unused")
	private Street() {
	}
	
	public Street(String name) {
		this.name = name;
	}

	public String getPrefixPart() {
		return prefixPart;
	}

	public void setPrefixPart(String prefixPart) {
		this.prefixPart = prefixPart;
	}

	public String getStemmedPart() {
		return stemmedPart;
	}

	public void setStemmedPart(String stemmedPart) {
		this.stemmedPart = stemmedPart;
	}

	public String getSuffixPart() {
		return suffixPart;
	}

	public void setSuffixPart(String suffixPart) {
		this.suffixPart = suffixPart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Street [prefixPart=" + prefixPart + ", stemmedPart="
				+ stemmedPart + ", suffixPart=" + suffixPart + ", name=" + name
				+ "]";
	}
}
