package hr.fer.zemris.nos.crypto.ciphers;

import java.util.Arrays;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.crypto.util.CryptoUtil;
import hr.fer.zemris.nos.util.UtilMethods;

public class Signature {
	public static void generateSignature(final String inPath, final String privKeyPath, final String outPath)
		throws Exception {
		CryptoConfig out = new CryptoConfig();
		byte[] hash = SHA1.hashData(CryptoUtil.readAllBytes(inPath));
		byte[] cryptHash = RSA.encrypt(hash, privKeyPath);

		// data

		out.addProperty("Description", "Signatrure");
		out.addProperty("Method", "SHA1");
		out.addProperty("Method", "RSA");

		out.addProperty("Key length", Integer.toHexString(512));
		out.addProperty("Key length", new CryptoConfig(privKeyPath).get("Key length"));
		out.addProperty("File name", UtilMethods.getFileName(inPath));
		out.addProperty("Signature", CryptoUtil.toHex(cryptHash));
		out.write(outPath);
	}

	public static boolean checkSignature(final String signaturePath, final String pubKeyPath,
		final String filePath)
			throws Exception {
		byte[] hash = SHA1.hashData(CryptoUtil.readAllBytes(filePath));

		byte[] cryptHash = CryptoUtil.fromHex(new CryptoConfig(signaturePath).get("Signature"));
		byte[] recvHash = RSA.decrypt(cryptHash, pubKeyPath);

		return Arrays.equals(hash, recvHash);

	}
}
