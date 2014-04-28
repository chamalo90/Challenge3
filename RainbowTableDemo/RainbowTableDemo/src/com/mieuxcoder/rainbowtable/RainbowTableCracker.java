package com.mieuxcoder.rainbowtable;

import java.io.IOException;
import java.util.Arrays;

public class RainbowTableCracker {
	private RainbowTable rainbowTable;
	private HashReduce hashReduce;
	
	public RainbowTableCracker(RainbowTable table, HashReduce hashReduce) {
		if (table == null)
			throw new IllegalArgumentException("table");
		if (hashReduce == null)
			throw new IllegalArgumentException("hashReduce");
		if (!hashReduce.getHashAlgorithName().equals(table.getHashAlgorithm())) {
			throw new IllegalArgumentException("Hash algorithm mismatch");
		}
		
		this.rainbowTable = table;
		this.hashReduce = hashReduce;
	}

	public String crack(String strHash) throws IOException {
		byte[] hashToCrack = HexUtils.fromHexString(strHash);

		// Crack
		int chainLength = rainbowTable.getChainLength();
		for (int i = chainLength - 1; i >= 0; i--) {
			byte[] finalHash = computeFinalHash(hashToCrack, i);
			for (RainbowTableRow row : rainbowTable.getRows()) {
				if (Arrays.equals(finalHash, row.getHash())) {
					// The computed final hash matches the final hash of a row, the password is certainly here!
					String password = findPasswordInRow(row, hashToCrack);
					if (password != null) {
						// Yes !! We found it!!!
						return password;
					} else {
						// Too bad, we encountered a hash collision. The password is not here.
						// We must go on...
					}
				}
			}
		}
		// Not found
		return null;
	}
	
	private String findPasswordInRow(RainbowTableRow row, byte[] hashToCrack) {
		int chainLength = rainbowTable.getChainLength();
		int passwordLength = rainbowTable.getPasswordLength();
		String characterSet = rainbowTable.getCharacterSet();

		String password = row.getPassword();
		for (int j = 0; j <= chainLength; j++) {
			byte[] hash = hashReduce.hash(password);
			if (Arrays.equals(hash, hashToCrack)) {
				// Found !!
				return password;
			}
			password = hashReduce.reduce(hash, j, characterSet, passwordLength);
		}
		// Not found
		return null;
	}

	private byte[] computeFinalHash(byte[] hash, int index) {
		int passwordLength = rainbowTable.getPasswordLength();
		String characterSet = rainbowTable.getCharacterSet();

		for (int i = index; i < rainbowTable.getChainLength() - 1; i++) {
			String password = hashReduce.reduce(hash, i, characterSet, passwordLength);
			hash = hashReduce.hash(password);
		}
		return hash;
	}
}
