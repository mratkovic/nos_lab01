package hr.fer.zemris.nos.crypto.ciphers;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.crypto.util.CryptoUtil;
import hr.fer.zemris.nos.util.UtilMethods;

public class RSA {

	public static void generateKeys(final String pubKeyPath, final String privKeyPath, final int len)
		throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		SecureRandom rand = new SecureRandom();
		kpg.initialize(len, rand);

		KeyPair kp = kpg.genKeyPair();
		KeyFactory fact = KeyFactory.getInstance("RSA");

		// store pub
		RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
		byte[] modData = pub.getModulus().toByteArray();
		byte[] pubData = pub.getPublicExponent().toByteArray();

		CryptoConfig conf = new CryptoConfig();
		conf.addProperty("Description", "Public key");
		conf.addProperty("Method", "RSA");
		conf.addProperty("Key length", Integer.toHexString(len).toUpperCase());
		conf.addProperty("Modulus", CryptoUtil.toHex(modData));
		conf.addProperty("Public exponent", CryptoUtil.toHex(pubData));
		conf.write(pubKeyPath);

		// store priv
		RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
		byte[] privData = priv.getPrivateExponent().toByteArray();

		conf = new CryptoConfig();

		conf.addProperty("Description", "Private key");
		conf.addProperty("Method", "RSA");
		conf.addProperty("Key length", Integer.toHexString(len).toUpperCase());
		conf.addProperty("Modulus", CryptoUtil.toHex(modData));
		conf.addProperty("Private exponent", CryptoUtil.toHex(privData));
		conf.write(privKeyPath);

	}

	public static byte[] encrypt(final byte[] data, final String pubKeyPath) throws Exception {

		CryptoConfig cfg = new CryptoConfig(pubKeyPath);
		String key = cfg.get("Public exponent");
		if (key == null) {
			key = cfg.get("Private exponent");
		}
		byte[] pubKeyData = CryptoUtil.fromHex(key);
		byte[] modData = CryptoUtil.fromHex(new CryptoConfig(pubKeyPath).get("Modulus"));

		BigInteger exp = new BigInteger(pubKeyData);
		BigInteger mod = new BigInteger(modData);

		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PublicKey pubKey = fact.generatePublic(keySpec);
		return encryptBlocks(data, pubKey);

	}

	private static byte[] encryptBlocks(final byte[] data, final PublicKey pubKey) throws NoSuchAlgorithmException,
		NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

		int block = 64;
		List<byte[]> dataParts = UtilMethods.splitArray(data, block);
		int sz = block*dataParts.size();

		byte[] encrypted = new byte[sz];
		int offset = 0;

		for(byte[] part : dataParts) {
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] crypted = cipher.doFinal(part);
			for(byte b : crypted) {
				encrypted[offset++] = b;
			}
		}
		return encrypted;
	}
	public static void encrypt(final String inPath, final String outPath, final String pubKeyPath)
		throws Exception {
		CryptoConfig out = new CryptoConfig();
		out.addProperty("Description", "Crypted file");
		out.addProperty("Method", "RSA");
		out.addProperty("Data", CryptoUtil.toBase64(encrypt(CryptoUtil.readAllBytes(inPath), pubKeyPath)));
		out.addProperty("File name", UtilMethods.getFileName(inPath));
		out.write(outPath);
	}

	public static byte[] decrypt(final byte[] data,
		final String privKeyPath) throws Exception {
		CryptoConfig cfg = new CryptoConfig(privKeyPath);

		String key = cfg.get("Private exponent");
		if (key == null) {
			key = cfg.get("Public exponent");
		}
		byte[] privKeyData = CryptoUtil.fromHex(key);
		byte[] modData = CryptoUtil.fromHex(new CryptoConfig(privKeyPath).get("Modulus"));

		BigInteger exp = new BigInteger(privKeyData);
		BigInteger mod = new BigInteger(modData);

		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(mod, exp);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PrivateKey privKey = fact.generatePrivate(keySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

		return decryptBlocks(data, privKey, cipher);
	}

	private static byte[] decryptBlocks(final byte[] data, final PrivateKey privKey, final Cipher cipher)
		throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		int block = 64;
		List<byte[]> dataParts = UtilMethods.splitArray(data, block);
		List<Byte> decrypted = new ArrayList<Byte>();

		for(byte[] part : dataParts) {
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] dc = cipher.doFinal(part);
			for(byte b : UtilMethods.ltrim(dc)) {
				decrypted.add(b);
			}
		}
		byte[] rv = new byte[decrypted.size()];
		for(int i = 0; i < rv.length; ++i) {
			rv[i] = decrypted.get(i);
		}
		return rv;
	}

	public static void decrypt(final String inPath, final String outPath,
		final String privKeyPath) throws Exception {
		byte[] data = CryptoUtil.fromBase64(new CryptoConfig(inPath).get("Data"));
		CryptoUtil.dumpToFile(decrypt(data, privKeyPath), outPath);
	}
}