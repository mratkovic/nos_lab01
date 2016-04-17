package hr.fer.zemris.nos.crypto.ciphers;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.crypto.util.CryptoUtil;
import hr.fer.zemris.nos.util.Pair;
import hr.fer.zemris.nos.util.UtilMethods;

public class Envelope {

	public static void generateEnvelope(final String inPath, final String pubKeyPath, final String outPath)
		throws Exception {
		byte[] aesKye = AES.generateKey(128);
		Pair<byte[], byte[]> data = AES.encrypt(CryptoUtil.readAllBytes(inPath), aesKye);
		byte[] encryptedData = data.first;
		byte[] iv = data.second;
		byte[] cryptKey = RSA.encrypt(aesKye, pubKeyPath);

		// data
		CryptoConfig out = new CryptoConfig();
		out.addProperty("Description", "Envelope");
		out.addProperty("Method", "AES");
		out.addProperty("Method", "RSA");
		out.addProperty("Key length", Integer.toHexString(128));
		out.addProperty("Key length", new CryptoConfig(pubKeyPath).get("Key length"));
		out.addProperty("File name", UtilMethods.getFileName(inPath));
		out.addProperty("Initialization vector", CryptoUtil.toHex(iv));
		out.addProperty("Envelope data", CryptoUtil.toBase64(encryptedData));
		out.addProperty("Envelope crypt key", CryptoUtil.toHex(cryptKey));
		out.write(outPath);
	}

	public static void openEnvelope(final String envelopePath, final String privKeyPath, final String outPath)
		throws Exception {
		CryptoConfig env = new CryptoConfig(envelopePath);

		byte[] aesKeyCrypt = CryptoUtil.fromHex(env.get("Envelope crypt key"));
		byte[] encryptedData = CryptoUtil.fromBase64(env.get("Envelope data"));
		byte[] iv = CryptoUtil.fromHex(env.get("Initialization vector"));

		byte[] aesKey = RSA.decrypt(aesKeyCrypt, privKeyPath);
		byte[] data = AES.decrypt(encryptedData, iv, aesKey);
		CryptoUtil.dumpToFile(data, outPath);

	}
}