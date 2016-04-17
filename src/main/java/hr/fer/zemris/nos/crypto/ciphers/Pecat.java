package hr.fer.zemris.nos.crypto.ciphers;

public class Pecat {


	public static void generatePecat(final String inPath, final String pubKeyBPath, final String privKeyAPath, final String envPath, final String sigPath)
		throws Exception {
		Envelope.generateEnvelope(inPath, pubKeyBPath, envPath);
		Signature.generateSignature(envPath,privKeyAPath, sigPath);
	}

	public static boolean openPecat(final String envelopePath, final String signaturePath, final String privKeyBPath, final String pubKeyAPath, final String outPath)
		throws Exception {

		Envelope.openEnvelope(envelopePath, privKeyBPath, outPath);
		return Signature.checkSignature(signaturePath, pubKeyAPath, envelopePath);
	}
}
