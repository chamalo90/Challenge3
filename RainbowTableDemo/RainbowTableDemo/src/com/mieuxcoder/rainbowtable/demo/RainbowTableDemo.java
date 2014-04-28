package com.mieuxcoder.rainbowtable.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mieuxcoder.rainbowtable.HashReduce;
import com.mieuxcoder.rainbowtable.HexUtils;
import com.mieuxcoder.rainbowtable.RainbowTable;
import com.mieuxcoder.rainbowtable.RainbowTableCracker;
import com.mieuxcoder.rainbowtable.RainbowTableGenerator;
import com.mieuxcoder.rainbowtable.RainbowTableLoader;

public class RainbowTableDemo {
	
	private HashReduce hashReduceFunctions = new SimpleHashReduce("SHA-1");

	/**
	 * Main class and entry point of the Rainbow table demo.
	 */
	public static void main(String[] args) throws IOException {

		if (args.length == 4 && "generate".equals(args[0])) {
			String rainbowTableFile = args[1];
			int chainLength = Integer.parseInt(args[2]);
			int rowCount = Integer.parseInt(args[3]);
			new RainbowTableDemo().generate(rainbowTableFile, chainLength, rowCount);
		} else if (args.length == 3 && "crack".equals(args[0])) {
			String rainbowTableFile = args[1];
			String hashListFile = args[2];
			new RainbowTableDemo().crack(rainbowTableFile, hashListFile);
		} else if (args.length == 3 && "hash".equals(args[0])) {
			String passwordListFile = args[1];
			String hashListFile = args[2];
			new RainbowTableDemo().hash(passwordListFile, hashListFile);
		} else {
			System.out.println("Usage: RainbowTableDemo crack <rainbow table file> <hash list file>");
			System.out.println("       RainbowTableDemo generate <rainbow table file> <chain-length> <row-count>");
			System.out.println("       RainbowTableDemo hash <password list file> <hash list file>");
		}
	}

	/**
	 * Constructor
	 */
	public RainbowTableDemo() {
		// Pick a hash-reduce implementation
		hashReduceFunctions = new SimpleHashReduce("SHA-1");
	}
	
	private void generate(String filename, int chainLength, int rowCount) throws IOException {
		System.out.println("Generating table...");
		RainbowTableGenerator generator = new RainbowTableGenerator();
		RainbowTable rainbowTable = generator.create(hashReduceFunctions, 4, "abcdefghijklmnopqrstuvwxyz0123456789", chainLength, rowCount);

		System.out.println("Saving table...");
		RainbowTableLoader.save(rainbowTable, filename);

		System.out.println("Done.");
	}

	private void crack(String filename, String hashListFilename) throws IOException, FileNotFoundException {
		System.out.println("Loading table...");
		RainbowTable rainbowTable = RainbowTableLoader.load(filename);
		RainbowTableCracker cracker = new RainbowTableCracker(rainbowTable, hashReduceFunctions);
		
		List<String> hashList = FileUtils.loadStringList(hashListFilename);
		for (String hash: hashList) {
			System.out.print("Cracking hash " + hash + "... ");

			long before = System.currentTimeMillis();
			String crackedPassword = cracker.crack(hash);
			long after = System.currentTimeMillis();
			
			System.out.print(crackedPassword == null ? "Not found" : crackedPassword);
			System.out.println(" [" + (after - before) + " msec]");
		}

		System.out.println("Done.");
	}

	private void hash(String passwordListFile, String hashListFile) throws IOException {
		System.out.println("Loading password list...");
		List<String> passwordList = FileUtils.loadStringList(passwordListFile);

		List<String> hashList = new ArrayList<String>();
		for (String password: passwordList) {
			byte[] hash = hashReduceFunctions.hash(password);
			hashList.add(HexUtils.toHexString(hash));
		}

		System.out.println("Saving hash list...");
		FileUtils.saveStringList(hashListFile, hashList);

		System.out.println("Done.");
	}

/*		
	System.out.println("Loading table...");
	RainbowTable rainbowTable = RainbowTableLoader.load("C:\\rainbow-table-100-25000.txt");
	
	RainbowTableCracker cracker = new RainbowTableCracker(rainbowTable, hashReduceFunctions);
	testRTCracker(cracker, "5152");
	testRTCracker(cracker, "pass");
	testRTCracker(cracker, "arno");
	testRTCracker(cracker, "1xy2");
	testRTCracker(cracker, "good");
	testRTCracker(cracker, "gold");
	testRTCracker(cracker, "hu14");
	testRTCracker(cracker, "bad2");
	testRTCracker(cracker, "fish");
*/		
/*
	byte[] hash1 = rainbowTable.hash("ev7d");
	String hash1Str = hash("ev7d");
	byte[] hash2 = rainbowTable.hash("2021");
	String hash2Str = hash("2021");
	String r1 = rainbowTable.reduce(hash1, 54);
	String r2 = rainbowTable.reduce(hash2, 54);
*/
}
