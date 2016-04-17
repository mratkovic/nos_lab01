package hr.fer.zemris.nos.crypto.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

public class CryptoUtil {

	public static byte[] readAllBytes(final String path) throws IOException {
		return Files.readAllBytes(Paths.get(path));
	}

	public static byte[] fromHex(String hexStr) {
		if (hexStr.length() % 2 == 1) {
			hexStr = "0" + hexStr;
		}
		return DatatypeConverter.parseHexBinary(hexStr);
	}

	public static String toHex(final byte[] data) {
		return DatatypeConverter.printHexBinary(data);
	}

	public static String toBase64(final byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	public static byte[] fromBase64(final String data) {
		return Base64.getDecoder().decode(data);
	}

	public static void dumpToFile(final byte[] data, final String path) throws IOException {
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(data);
		fos.close();
	}
}
