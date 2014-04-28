package com.mieuxcoder.rainbowtable.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static void saveStringList(String hashListFile, List<String> hashList) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(hashListFile));
		for (String hash: hashList) {
			writer.write(hash);
			writer.newLine();
		}
		writer.close();
	}

	public static List<String> loadStringList(String hashListFilename) throws IOException {
		List<String> result = new ArrayList<String>();

		// Load the Rainbow table
		BufferedReader in = new BufferedReader(new FileReader(hashListFilename));
		try {
			String line = in.readLine();
			while (line != null) {
				result.add(line);
				line = in.readLine();
			}
		} finally {
			in.close();
		}
		return result;
	}
}
