package hr.fer.zemris.nos.crypto.ciphers;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.crypto.util.CryptoUtil;
import hr.fer.zemris.nos.util.Pair;
import hr.fer.zemris.nos.util.UtilMethods;

public class AES {
	public static void encrypt(final String inPath, final String outPath, final String configFile)
		throws Exception {
		CryptoConfig inConfig = new CryptoConfig(configFile);
		CryptoConfig outConfig = new CryptoConfig();

		Pair<byte[], byte[]> cryptoData = encrypt(CryptoUtil.readAllBytes(inPath),
			CryptoUtil.fromHex(inConfig.get("Secret key")));

		String base64encryptedData = CryptoUtil.toBase64(cryptoData.first);
		String ivHex = CryptoUtil.toHex(cryptoData.second);

		outConfig.addProperty("Description", "Crypted file");
		outConfig.addProperty("Method", "AES");
		outConfig.addProperty("File name", UtilMethods.getFileName(inPath));
		outConfig.addProperty("Initialization vector", ivHex);
		outConfig.addProperty("Data", base64encryptedData);

		outConfig.write(outPath);
	}

	public static Pair<byte[], byte[]> encrypt(final byte[] data, final byte[] key)
		throws Exception {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecretKeySpec k = new SecretKeySpec(key, "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		return new Pair<byte[], byte[]>(c.doFinal(data), c.getIV());
	}

	public static void decrypt(final String inPath, final String outPath, final String configFile)
		throws Exception {
		CryptoConfig keyConfig = new CryptoConfig(configFile);
		CryptoConfig in = new CryptoConfig(inPath);

		byte[] keyBytes = CryptoUtil.fromHex(keyConfig.get("Secret key"));
		byte[] encryptedData = CryptoUtil.fromBase64(in.get("Data"));
		byte[] iv = CryptoUtil.fromHex(in.get("Initialization vector"));

		CryptoUtil.dumpToFile(decrypt(encryptedData, iv, keyBytes), outPath);
	}

	public static byte[] decrypt(final byte[] encryptedData, final byte[] iv, final byte[] keyBytes)
		throws Exception {
		SecretKeySpec k = new SecretKeySpec(keyBytes, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));

		return c.doFinal(encryptedData);
	}

	public static byte[] generateKey(final int len) {
		Key key;
		SecureRandom rand = new SecureRandom();
		KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance("AES");
			generator.init(rand);
			generator.init(len);
			key = generator.generateKey();
			return key.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No instance AES");
			e.printStackTrace();
		}
		return null;
	}

	public static void generateKeyFile(final String keyPath, final int len) throws IOException {
		String hex = CryptoUtil.toHex(generateKey(len));
		CryptoConfig conf = new CryptoConfig();
		conf.addProperty("Description", "Secret key");
		conf.addProperty("Method", "AES");
		conf.addProperty("Key length", Integer.toHexString(len).toUpperCase());
		conf.addProperty("Secret key", hex);
		conf.write(keyPath);
	}

}
