package hr.fer.zemris.nos.crypto.ciphers;

import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.crypto.util.CryptoUtil;

public class SHA1Impl {
	final static int[] K = { 0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xCA62C1D6 };

	public static void hashFile(final String inPath, final String outPath) throws Exception {
		byte[] data = addPadding(CryptoUtil.readAllBytes(inPath));
		int len = data.length;
		if (len % 64 != 0) {
			throw new RuntimeException("Nop 64");
		}

		byte[] block = new byte[64];
		int[] H = { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };
		for (int i = 0; i < len; i += 64) {
			System.arraycopy(data, i, block, 0, 64);
			hashBlock(H, block);
		}
		byte[] hash = finalize(H);

		CryptoConfig out = new CryptoConfig();
		out.addProperty("Description", "SHA checksum");
		out.addProperty("File name", Paths.get(inPath).getFileName().toString());
		out.addProperty("Method", "SHA-1");
		out.addProperty("Signature", CryptoUtil.toHex(hash));
		out.write(outPath);
	}

	private static int rol(final int data, final int shift) {
		return (data << shift) | (data >>> (32 - shift));
	}

	private static void hashBlock(final int[] h, final byte[] block) {
		int[] W = new int[80];
		int A, B, C, D, E;
		for (int i = 0; i < 16; ++i) {
			for (int b = i * 4; b < i * 4 + 4; ++b) {
				W[i] <<= 8;
				W[i] |= (block[b] & 0xFF);
			}
		}
		for (int i = 16; i < 80; ++i) {
			W[i] = rol(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1);
		}

		A = h[0];
		B = h[1];
		C = h[2];
		D = h[3];
		E = h[4];
		for (int t = 0; t < 80; ++t) {
			int tmp = rol(A, 5) + f(t, B, C, D) + E + W[t] + K[t / 20];
			E = D;
			D = C;
			C = rol(B, 30);
			B = A;
			A = tmp;
		}

		h[0] += A;
		h[1] += B;
		h[2] += C;
		h[3] += D;
		h[4] += E;
	}

	private static byte[] finalize(final int[] h) {
		byte[] data = new byte[20];
		int n = data.length - 1;
		for (int i = 4; i >= 0; --i) {
			for (int b = 0; b < 4; ++b) {
				data[n--] = (byte) (h[i] & 0xFF);
				h[i] >>= 8;
			}
		}
		return data;
	}

	private static byte[] addPadding(final byte[] data) {
		int dataLen = data.length;
		long dataLenBits = dataLen * 8;

		int lastBlock = dataLen % 64;
		int padLen = 64 - lastBlock;

		if (lastBlock >= 56) { // no place
			padLen += 64;
		}

		byte[] padding = new byte[padLen];
		padding[0] = (byte) (1 << 7);

		// write len to last bytes
		for (int i = 0; i < 8; i++) {
			padding[padLen - 1 - i] = (byte) (dataLenBits & 0xFF);
			dataLenBits >>= 8;
		}

		byte[] padded = new byte[dataLen + padLen];
		System.arraycopy(data, 0, padded, 0, dataLen);
		System.arraycopy(padding, 0, padded, dataLen, padLen);
		return padded;

	}

	private static int f(int t, final int B, final int C, final int D) {
		t /= 20;
		switch (t) {
		case 0:
			return (B & C) | ((~B) & D);
		case 1:
			return B ^ C ^ D;
		case 2:
			return (B & C) | (B & D) | (C & D);
		case 3:
			return B ^ C ^ D;
		default:
			throw new RuntimeException("t paramerat out of bounds (> 80)");

		}
	}

	public static byte[] hashData(final byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(data);
		return md.digest();

	}
}
