package cn.autumn.wishbackstage.config.safety;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Autumn
 * Created in 2022/12/30
 * Aes crypto util
 */
@SuppressWarnings("restriction")
public final class Aes {

    private static final String AES_ALGORITHM = "AES";

    private static final String SIGNATURE_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES encrypt
     * @param str data
     * @param key secret
     * @return Encrypted data
     * @throws Exception ..
     */
    public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance(SIGNATURE_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM));
        byte[] bytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * AES decrypt
     * @param str Encrypt data
     * @param key secret
     * @return Decrypted data
     * @throws Exception ..
     */
    public static String aesDecrypt(String str, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(SIGNATURE_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM));
        byte[] bytes = Base64.getDecoder().decode(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
