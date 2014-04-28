package com.mieuxcoder.rainbowtable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTableLoader {
	private static final String INVALID_RAINBOW_TABLE_FILE = "Invalid Rainbow Table file.";
	private static final String PROPERTIES_SECTION = "[properties]";

	public static RainbowTable load(String file) throws IOException {
		// Load the Rainbow table
		BufferedReader in = new BufferedReader(new FileReader(file));

		try {
			// Read header line and check it is a Rainbow Table file.
			readHeader(in);
	
			// Read rainbow table properties (character set, chain length, ...).
			Map<String, String> properties = readProperties(in);
			int tableRowCount = Integer.parseInt(properties.get("row-count"));
	
			// Read rainbow table content.
			RainbowTableRow[] rows = readContent(in, tableRowCount);
			
			// Initialize the rainbow table 
			RainbowTable rainbowTable = new RainbowTable();
			rainbowTable.setChainLength(Integer.parseInt(properties.get("chain-length")));
			rainbowTable.setCharacterSet(properties.get("charset"));
			rainbowTable.setHashAlgorithm(properties.get("hash-algorithm"));
			rainbowTable.setPasswordLength(Integer.parseInt(properties.get("password-length")));
			rainbowTable.setRows(rows);
			return rainbowTable;
		} finally {
			in.close();
		}
	}
	
	public static void save(RainbowTable rainbowTable, String file) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		try {
			writeHeader(writer);
			writeProperties(writer, rainbowTable);
			writeContent(writer, rainbowTable.getRows());
		} finally {
			writer.close();
		}
	}

	private static void writeHeader(BufferedWriter writer) throws IOException {
		writer.write("== RainbowTable ==");
		writer.newLine();
		writer.newLine();
	}

	private static void readHeader(BufferedReader in) throws IOException {
		String header = in.readLine();
		if (!header.equals("== RainbowTable ==")) {
			throw new IllegalStateException(INVALID_RAINBOW_TABLE_FILE);
		}
	}

	private static void writeProperties(BufferedWriter writer, RainbowTable rainbowTable) throws IOException {
		writer.write("[properties]");
		writer.newLine();
		writer.write("password-length=");
		writer.write(Integer.toString(rainbowTable.getPasswordLength()));
		writer.newLine();
		writer.write("charset=");
		writer.write(rainbowTable.getCharacterSet());
		writer.newLine();
		writer.write("hash-algorithm=");
		writer.write(rainbowTable.getHashAlgorithm());
		writer.newLine();
		writer.write("chain-length=");
		writer.write(Integer.toString(rainbowTable.getChainLength()));
		writer.newLine();
		writer.write("row-count=");
		writer.write(Integer.toString(rainbowTable.getRows().length));
		writer.newLine();
		writer.newLine();
	}

	private static Map<String, String> readProperties(BufferedReader in) throws IOException {
		Map<String, String> properties = new HashMap<String, String>();

		// Skip blank lines.
		String line = in.readLine();
		while (line != null && line.trim().length() == 0) {
			line = in.readLine();
		}
		if (!PROPERTIES_SECTION.equals(line)) {
			throw new IllegalStateException(INVALID_RAINBOW_TABLE_FILE);
		}
		while ((line = in.readLine()) != null && !"[content]".equals(line)) {
			// Skip blank line
			if (line.trim().length() == 0) {
				continue; 
			}
			
			int equalSignIndex = line.indexOf('=');
			if (equalSignIndex < 0) {
				throw new IllegalStateException(INVALID_RAINBOW_TABLE_FILE);
			}
			String key = line.substring(0, equalSignIndex);
			String value = line.substring(1 + equalSignIndex);
			properties.put(key, value);
		}
		
		return properties;
	}

	private static void writeContent(BufferedWriter writer, RainbowTableRow[] rows) throws IOException {
		writer.write("[content]");
		writer.newLine();

		int rowCount = rows.length;
		for (int i = 0; i < rowCount; i++) {
			RainbowTableRow row = rows[i];
			writer.write(row.getPassword());
			writer.write(',');
			writer.write(HexUtils.toHexString(row.getHash()));
			writer.newLine();
		}
	}

	private static RainbowTableRow[] readContent(BufferedReader in, int rowCount) throws IOException {
		RainbowTableRow[] rows = new RainbowTableRow[rowCount];
		for (int i = 0; i < rowCount; i++) {
			String line = in.readLine();
			if (line == null) {
				throw new IllegalStateException(INVALID_RAINBOW_TABLE_FILE);
			}

			int separatorIndex = line.indexOf(',');
			if (separatorIndex < 0) {
				throw new IllegalStateException(INVALID_RAINBOW_TABLE_FILE);
			}
			String initialPassword = line.substring(0, separatorIndex);
			String finalHashStr = line.substring(1 + separatorIndex);
			rows[i] = new RainbowTableRow(HexUtils.fromHexString(finalHashStr), initialPassword);
		}
		return rows;
	}
}
