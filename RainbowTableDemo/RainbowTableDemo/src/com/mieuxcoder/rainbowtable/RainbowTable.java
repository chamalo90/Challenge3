package com.mieuxcoder.rainbowtable;

public class RainbowTable {

	// Rainbow Table properties
	private int chainLength;
	private int passwordLength;
	private String characterSet;
	private String hashAlgorithm;
	private RainbowTableRow[] rows;

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public int getPasswordLength() {
		return passwordLength;
	}
	
	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public int getChainLength() {
		return chainLength;
	}

	public void setChainLength(int chainLength) {
		this.chainLength = chainLength;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public RainbowTableRow[] getRows() {
		return rows;
	}

	public void setRows(RainbowTableRow[] rows) {
		this.rows = rows;
	}
}
