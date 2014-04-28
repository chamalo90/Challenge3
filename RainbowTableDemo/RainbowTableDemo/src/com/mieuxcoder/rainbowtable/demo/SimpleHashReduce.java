package com.mieuxcoder.rainbowtable.demo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mieuxcoder.rainbowtable.HashReduce;

public class SimpleHashReduce implements HashReduce {
	
	// Name of the algorithm used to hash passwords.
	private String hashAlgorithmName;

	// Rainbow Table hash method.
	private MessageDigest messageDigest;

	/**
	 * Initializes a new instance of the {@link SimpleHashReduce} class associated
	 * with the supplied hash algorithm.
	 *  
	 * @param hashAlgorithmName Name of the hash algorithm to use (ex: "SHA-1" or "MD5").
	 */
	public SimpleHashReduce(String hashAlgorithmName) {
		this.hashAlgorithmName = hashAlgorithmName;

		try {
			messageDigest = MessageDigest.getInstance(hashAlgorithmName);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Gets the name of the algorithm used to hash passwords.
	 * 
	 * @return A string containing the name of the algorithm (ex: "SHA-1")
	 */
	public String getHashAlgorithName() {
		return hashAlgorithmName;
	}

	/**
	 * Hash function for the rainbow table.
	 */
	public byte[] hash(String password) {
		try {
			messageDigest.update(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
		return messageDigest.digest();
	}

	/**
	 * Reduce function for the rainbow table.
	 * 
	 * <p>It computes a password in the specified range (character set + password length) from a hash.
	 * The computed password depends on the supplied column index.</p> 
	 */
	public String reduce(byte[] hash, int columnIndex, String characterSet, int passwordLength) {
		int setLength = characterSet.length();
		int hashLength = hash.length;
		char[] result = new char[passwordLength];
		for (int i = 0; i < passwordLength; i++) {
			int index = hash[(i + columnIndex) % hashLength];
			index += 128; // Add 128 to get a positive number because bytes range from -128 to 127.
			result[i] = characterSet.charAt(index % setLength);
		}
		return new String(result);
	}
}
