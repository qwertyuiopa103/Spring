package ispan.user.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptEncryptionTool {
	private static final PasswordEncoder encoder = new BCryptPasswordEncoder(10);

	/**
	 * 將傳入的字串進行 BCrypt 加密。
	 */
	public static String encrypt(String rawData) {
		return encoder.encode(rawData);
	}

	/**
	 * 驗證傳入的未加密字串 rawData，是否與已加密字串 encryptedData 相符。
	 */
	public static boolean verify(String rawData, String encryptedData) {
		return encoder.matches(rawData, encryptedData);
	}
}
