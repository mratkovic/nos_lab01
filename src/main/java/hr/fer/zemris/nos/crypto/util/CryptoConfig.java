package hr.fer.zemris.nos.crypto.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import hr.fer.zemris.nos.util.Pair;
import hr.fer.zemris.nos.util.UtilMethods;

public class CryptoConfig {
	private static final String HEADER = "---BEGIN OS2 CRYPTO DATA---";
	private static final String FOOTER = "---END OS2 CRYPTO DATA---";
	private static final String PROP_START = "    ";
	private static final int MAX_LINE_LEN = 60;

	private Map<String, List<String>> config;

	public CryptoConfig() {
		config = new LinkedHashMap<>();
	}

	public void addProperty(final String key, final String value) {
		String[] lines = UtilMethods.wrapText(value, MAX_LINE_LEN);

		List<String> val = new ArrayList<>();
		if (config.containsKey(key)) {
			val = config.get(key);
		}

		for (String ln : lines) {
			val.add(ln);
		}
		config.put(key, val);
	}

	public void setProperty(final String key, final String value) {
		String[] lines = UtilMethods.wrapText(value, MAX_LINE_LEN);

		List<String> val = new ArrayList<>();
		for (String ln : lines) {
			val.add(ln);
		}
		config.put(key, val);
	}
	public void removeProperty(final String key) {
		if (config.containsKey(key)) {
			config.remove(key);
		}
	}

	public void renameProperty(final String key, final String newKey) {
		if(config.containsKey(key)) {
			config.put(newKey, config.get(key));
			config.remove(key);
		} else {
			System.err.println("Key not found");
		}
	}

	public CryptoConfig(final String path) throws IOException {
		this();
		parseFromFile(path);
	}

	public void write(final String path) throws IOException {
		FileWriter out = new FileWriter(path);
		out.write(HEADER);
		out.write("\n");

		for (Entry<String, List<String>> entry : config.entrySet()) {
			out.write("\n" + entry.getKey() + ":\n");
			for (String line : entry.getValue()) {
				out.write(PROP_START + line + "\n");
			}
		}

		out.write(FOOTER);
		out.write("\n");
		out.flush();
		out.close();
	}

	private Pair<Integer, Integer> getRange(final List<String> lines) {
		int start = -1;
		int end = -1;
		for (int i = 0; i < lines.size(); ++i) {
			if (lines.get(i).trim().equals(HEADER)) {
				if (start != -1) {
					throw new InvalidCryptoConfigFileException("Multiple headers");
				} else {
					start = i;
				}
			}

			if (lines.get(i).trim().equals(FOOTER)) {
				if (end != -1) {
					throw new InvalidCryptoConfigFileException("Multiple footers");
				} else {
					end = i;
				}
			}
		}

		return new Pair<Integer, Integer>(start, end);

	}

	private void parseFromFile(final String path) throws IOException {
		List<String> lines = Files.readAllLines(new File(path).toPath());
		lines = lines.stream().map(s -> UtilMethods.rtrim(s)).filter(s -> !s.isEmpty())
			.collect(Collectors.toList());

		Pair<Integer, Integer> range = getRange(lines);
		int start = range.first;
		int end = range.second;

		if (start == -1 || end == -1) {
			throw new InvalidCryptoConfigFileException("No header or footer");
		}

		int i = start + 1;
		while (i < end) {
			String line = lines.get(i++);

			if (!line.startsWith(PROP_START)) {
				if (!line.matches("^.*:")) {
					throw new InvalidCryptoConfigFileException("Invalid property " + line);
				}
				String title = line.substring(0, line.length() - 1);
				List<String> data = new ArrayList<>();
				while (lines.get(i).startsWith(PROP_START)) {
					data.add(lines.get(i++).trim());
				}

				if (config.containsKey(title)) {
					throw new InvalidCryptoConfigFileException("Redefinition of " + title);
				}
				config.put(title, data);
			}

		}

	}

	public String get(final String key) {
		if(config.containsKey(key)) {
			return String.join("", config.get(key));
		} else {
			return null;
		}
	}

}
