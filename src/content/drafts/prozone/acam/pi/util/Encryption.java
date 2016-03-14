package rs.prozone.acam.pi.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Encryption {
	static Cipher cipher;

	private SecretKey secretKey;

	protected static Logger logger = LoggerFactory.getLogger(Encryption.class);

	public Encryption() {
		secretKey = new SecretKeySpec("45EdoubZ$ùb!eRxx=78dsgtrgtrRRE,".getBytes(), "AES");
		try {
			cipher = Cipher.getInstance("AES");
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) throws Exception {
		Encryption encryption = new Encryption();

		logger.debug("secret key: " + encryption.secretKey.toString());

		String plainText = "dev";
		logger.debug("Plain Text Before Encryption: " + plainText);

		String encryptedText = encryption.encrypt(plainText);
		logger.debug("Encrypted Text After Encryption: " + encryptedText);

		String decryptedText = encryption.decrypt(encryptedText);
		logger.debug("Decrypted Text After Decryption: " + decryptedText);
	}

	public String encrypt(String plainText) throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = new String(Base64.encodeBase64(encryptedByte));
		return encryptedText;
	}

	public String decrypt(String encryptedText) throws Exception {
		Base64 base64 = new Base64();
		byte[] encryptedTextByte = (byte[]) base64.decode(encryptedText.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
}