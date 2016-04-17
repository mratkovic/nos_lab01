package hr.fer.zemris.nos.crypto.ciphers;

import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.crypto.util.CryptoUtil;

public class SHA1 {
	public static void hashFile(final String inPath, final String outPath) throws Exception {
		CryptoConfig out = new CryptoConfig();

		byte[] data = CryptoUtil.readAllBytes(inPath);
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(data);

		out.addProperty("Description", "SHA checksum");
		out.addProperty("File name", Paths.get(inPath).getFileName().toString());
		out.addProperty("Method", "SHA-1");
		out.addProperty("Signature", CryptoUtil.toHex(md.digest()));

		out.write(outPath);
	}

	public static byte[] hashData(final byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(data);
		return md.digest();

	}
}
