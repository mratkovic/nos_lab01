package hr.fer.zemris.nos.crypto.test;

import java.io.IOException;

import hr.fer.zemris.nos.crypto.util.CryptoConfig;
import hr.fer.zemris.nos.util.UtilMethods;

public class Test {
	public static void main(final String[] args) throws IOException {
		CryptoConfig cryptoConfig = new CryptoConfig("./demo_config");
		cryptoConfig.write("log.txt");

		String[] ans = UtilMethods.wrapText("Ovojetests amodavidimkoli kodobroovoradi", 15);
		for(String str : ans) {
			System.out.println(str);
		}

		int W1 = 128;
		int W2 = 128;
		byte b = (byte) 0x80;

		W1 |= b;
		W2 |= (b & 0xFF);
		System.out.println(Integer.toHexString(W1));
		System.out.println(Integer.toHexString(W2));
	}
}
