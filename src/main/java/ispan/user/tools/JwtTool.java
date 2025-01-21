package ispan.user.tools;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtTool {

	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("DoNotGoGentleIntoThatGoodSecurityKey".getBytes());
	private static final int EXPIRATION_IN_SECONDS = 60 * 60;

	/**
	 * 產生 JWT Token。
	 *
	 * @param memberId 使用者的唯一識別碼，將作為 JWT Token 的 subject。
	 * @return 生成的 JWT Token 字串。
	 */
	public static String generateToken(String userID, String userRole) {
		return Jwts.builder() // 使用 builder 模式設定 token
				.subject(userID) // 設定 token 主題 (Subject)，用以標識使用者
				.claim("userRole", userRole)
				.issuedAt(new Date()) // 設定 token 發行時間
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_SECONDS * 1000)) // 設定 token 到期日期
				.signWith(SECRET_KEY) // 使用密鑰對 token 進行簽名
				.compact(); // 生成 JWT token
	}

	private static Claims getClaims(String token) {
		return Jwts.parser()// 使用 parser() 取得解析器
				.verifyWith(SECRET_KEY) // 設定解密用密鑰
				.build() // 建立解析器
				.parseSignedClaims(token) // 解析 token
				.getPayload(); // 取得解析後結果
	}
	
	public static String getClaim(String token, String claimKey) {
        Claims claims = getClaims(token);
        return claims.get(claimKey, String.class);
    }
	

	public static String getSubject(String token) {
		return getClaims(token).getSubject();
	}

	public static String getValue(String token, String key) {
		return (String) getClaims(token).get(key);
	}

	public static Boolean isTokenValid(String token) {
		getSubject(token); // 若 token 有任何異常，則由 jjwt 套件直接拋出錯誤。

		return true; // 能走到回傳表示驗證通過，token 合法
	}
}
