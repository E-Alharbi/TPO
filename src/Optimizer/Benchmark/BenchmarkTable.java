package Optimizer.Benchmark;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import Optimizer.Util.Txt;

public class BenchmarkTable {
	/*
	 * Create a latex table for the test functions and their parameters types.
	 */

	public static void main(String[] args) throws IOException {
		String table = "";
		// Path to R script test functions
		File[] RScripts = new File("RscriptTestFunctions").listFiles();
		LinkedHashMap<String, String> scriptsnames = new LinkedHashMap<String, String>();
		for (File script : RScripts) {
			if (script.getName().endsWith(".r"))
				scriptsnames.put(script.getName(), script.getAbsolutePath());
		}
		TreeMap<String, String> sorted = new TreeMap<>(scriptsnames);

		// Collections.sort(scriptsnames);
		for (String script : sorted.keySet()) {
			// if (script.getName().endsWith(".r")) {
			String RCode = new Txt().readFileAsString(scriptsnames.get(script));

			// System.out.println(new BenchmarkTable().TableRow(script.getName(),RCode));
			table += new BenchmarkTable().TableRow(script, RCode) + "\n";
			// }
		}
		new Txt().WriteTxtFile("TestFunctionTable", table);
	}

	String TableRow(String function, String txt) {
		String[] lines = txt.split("\n");
		boolean inputs = false;

		// String row=function+"&";
		String row = "";
		long countParameter = 0;
		for (String line : lines) {
			if (line.contains("FUNCTION")) {
				row = line.replaceAll("#", "").replaceAll("FUNCTION", "") + "&";
				row = row.toLowerCase().trim();
				row = row.substring(0, 1).toUpperCase() + row.substring(1);
			}
			if (inputs) {

				if (line.contains("xx")) {
					long count = 0;
					if (line.contains("...")) {

						count = 2;

						row += "&\\checkmark&";

					} else {
						count = line.chars().filter(ch -> ch == ',').count() + 1;

						for (int c = 0; c < count; ++c) {
							// Single value

							row += "\\checkmark&&";

						}
					}

				}

			}
			if (line.contains("INPUT:") || line.contains("INPUTS:"))
				inputs = true;
			if (line.trim().contains("############################"))
				inputs = false;
		}

		countParameter = row.chars().filter(ch -> ch == '&').count() + 1;

		if (countParameter < 12) {
			for (long c = countParameter + 1; c < 12; ++c) {
				row += "&";
			}
		}
		return row + "\\\\";
	}
}
