package com.mieuxcoder.rainbowtable;

import java.io.IOException;
import java.security.SecureRandom;

public class RainbowTableGenerator {

	private SecureRandom randomNumberGenerator;
	
	public RainbowTableGenerator() {
		randomNumberGenerator = new SecureRandom();
	}

	public RainbowTable create(HashReduce hashReduce, int passwordLength, String characterSet, int chainLength, int rowCount) throws IOException {
		RainbowTable rainbowTable = new RainbowTable();
		rainbowTable.setChainLength(chainLength);
		rainbowTable.setCharacterSet(characterSet);
		rainbowTable.setPasswordLength(passwordLength);
		rainbowTable.setHashAlgorithm(hashReduce.getHashAlgorithName());

		// Generate rainbow table rows
		RainbowTableRow[] rows = new RainbowTableRow[rowCount];
		for (int i = 0; i < rowCount; i++) {
			// Pick a password (a random one for example)
			String password = randomPassword(passwordLength, characterSet);
			
			// Hash and reduce it a number of times and retrieve the final hash.
			byte[] finalHash = computeFinalHash(rainbowTable, hashReduce, password);
			
			// Store the initial password, and the final hash.
			rows[i] = new RainbowTableRow(finalHash, password);
		}
		rainbowTable.setRows(rows);

		return rainbowTable;
	}
	
	private static byte[] computeFinalHash(RainbowTable rainbowTable, HashReduce hashReduce, String password) {
		int passwordLength = rainbowTable.getPasswordLength();
		String characterSet = rainbowTable.getCharacterSet();
		byte[] hash = hashReduce.hash(password);
		
		int loopCount = rainbowTable.getChainLength() - 1;
		for (int i = 0; i < loopCount; i++) {
			// Compute an intermediate password by reducing the hash using the reduce
			// function for the current column.
			String intermediatePassword = hashReduce.reduce(hash, i, characterSet, passwordLength);
			
			// Compute a new hash based on the intermediate password
			hash = hashReduce.hash(intermediatePassword);
			
			// And do it again and again...
		}

		// Return the hash of the last password computed.
		return hash;
	}

	/**
	 * Picks a random password.
	 */
	public String randomPassword(int passwordLength, String characterSet) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < passwordLength; i++) {
			int characterIndex = randomNumberGenerator.nextInt(characterSet.length());
			result.append(characterSet.charAt(characterIndex));
		}
		return result.toString();
	}
}
