package com.mieuxcoder.rainbowtable;

public interface HashReduce {
	
	/**
	 * Gets the name of the algorithm used to hash passwords.
	 * 
	 * @return A string containing the name of the algorithm (ex: "SHA-1")
	 */
	public String getHashAlgorithName();

	/**
	 * Hash function for the rainbow table.
	 */
	public byte[] hash(String password);

	/**
	 * Reduce function for the rainbow table.
	 * 
	 * <p>It computes a password in the specified range (character set + password length) from a hash.
	 * The computed password depends on the supplied column index.</p> 
	 */
	public String reduce(byte[] hash, int columnIndex, String characterSet, int passwordLength);
}
