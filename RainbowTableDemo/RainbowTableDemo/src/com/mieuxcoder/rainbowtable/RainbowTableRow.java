package com.mieuxcoder.rainbowtable;

import java.util.Arrays;

public class RainbowTableRow {

	private byte[] hash;
	private String password;

	public RainbowTableRow(byte[] hash, String password) {
		this.hash = hash;
		this.password = password;
	}
	
	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RainbowTableRow) {
			RainbowTableRow other = (RainbowTableRow)obj;
			byte[] data1 = this.hash;
			byte[] data2 = other.hash;
			int length = data1.length;
			if (data2.length == length) {
				return Arrays.equals(data1, data2);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.hash == null)
			return 0;

		int hash = 0;
		int length = this.hash.length;
		for (int i = 0; i < length; i++) {
			// rotate left and xor (very fast in assembler, a bit clumsy in Java)
			hash <<= 1;
			if (hash < 0) {
				hash |= 1;
			}
			hash ^= this.hash[i];
		}
		return hash;
	}
}
