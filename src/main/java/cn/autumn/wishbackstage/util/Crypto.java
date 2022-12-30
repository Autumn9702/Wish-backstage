package cn.autumn.wishbackstage.util;

import org.springframework.util.DigestUtils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author cf
 * Created in 2022/10/31
 */
public final class Crypto {

    private static final String SECRET = "AKIWISH1230";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static byte[] createSessionKey(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    public static String generateSecret() {
        String uid = UUID.randomUUID().toString().replace("-", "").substring(10);
        return DigestUtils.md5DigestAsHex((uid + SECRET).getBytes());
    }
}
