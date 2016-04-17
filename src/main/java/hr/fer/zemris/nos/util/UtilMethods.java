package hr.fer.zemris.nos.util;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class UtilMethods {

	public static String ltrim(final String s) {
		int begin;
		for (begin = 0; begin < s.length(); ++begin) {
			if (!Character.isWhitespace(s.charAt(begin))) {
				break;
			}
		}
		return s.substring(begin);
	}

	public static String rtrim(final String s) {
		int end;
		for (end = s.length() - 1; end >= 0; --end) {
			if (!Character.isWhitespace(s.charAt(end))) {
				break;
			}
		}

		if (end == s.length()) {
			return s;
		} else {
			return s.substring(0, end + 1);
		}
	}

	public static String[] wrapText(final String input, final int maxCharInLine) {
		StringTokenizer tok = new StringTokenizer(input, " ");
		StringBuilder output = new StringBuilder(input.length());

		int lineLen = 0;
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken();

			while (word.length() > maxCharInLine) {
				output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
				word = word.substring(maxCharInLine - lineLen);
				lineLen = 0;
			}

			if (lineLen + word.length() > maxCharInLine) {
				output.append("\n");
				lineLen = 0;
			}
			output.append(word + " ");

			lineLen += word.length() + 1;
		}

		return output.toString().split("\n");
	}

	public static String getFileName(final String path) {
		return Paths.get(path).getFileName().toString();
	}

	public static byte[] ltrim(final byte[] bytes) {
		int i = 0;
		while (i < bytes.length && bytes[i] == 0) {
			++i;
		}
		return Arrays.copyOfRange(bytes, i, bytes.length);
	}


	public static List<byte[]> splitArray(final byte[] source, final int chunksize) {
		List<byte[]> result = new ArrayList<byte[]>();
		int start = 0;
		while (start < source.length) {
			int end = Math.min(source.length, start + chunksize);
			result.add(Arrays.copyOfRange(source, start, end));
			start += chunksize;
		}

		return result;
	}

}
