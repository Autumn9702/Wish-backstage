package cn.autumn.wishbackstage.config.safety;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA Public key/Private key/Signature kit
 * Asymmetric encryption is extremely slow. So. The symmetric encryption key is encrypted here
 * In this way, the security of the key also guarantees the security of the data
 *
 * @author Autumn
 * Created in 2022/12/29
 */
public final class Rsa {

    /**
     * Encryption algorithm RSA
     */
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * Signature algorithm
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * Public key
     */
    private static final String RSA_PUBLIC_KEY = "RsaPublicKey";

    /**
     * Private key
     */
    private static final String RSA_PRIVATE_KEY = "RsaPrivateKey";

    /**
     * RSA encrypt max
     */
    private static final int MAX_ENCRYPT = 117;

    /**
     * RSA encrypt min
     */
    private static final int MIN_ENCRYPT = 128;

    /**
     * Generate key pair (public key and private key)
     * @return The key pair
     * @throws NoSuchAlgorithmException ..
     */
    public static Map<String, Object> generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(RSA_PUBLIC_KEY, publicKey);
        keyMap.put(RSA_PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * Generate a digital signature with the private key pair information
     * @param data Encrypted data
     * @param privateKey The private key(BASE64encode)
     * @return The signature
     * @throws Exception ..
     */
    public static String generateSignature(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encodeToString(signature.sign());
    }

    /**
     * Verify digital signature
     * @param data Encrypted data
     * @param publicKey The public key(BASE64encode)
     * @param sign Digital signature
     * @return verify success ?
     * @throws Exception ..
     */
    public static boolean verifySignature(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decodeFromString(sign));
    }

    /**
     * Private key decrypt data
     * @param encryptedData Encrypted data
     * @param privateKey The private key(BASE64encode)
     * @return Decrypted data
     * @throws Exception ..
     */
    public static byte[] privateKeyDecrypt(byte[] encryptedData, String privateKey, boolean decrypt) throws Exception {
        byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(decrypt ? Cipher.DECRYPT_MODE : Cipher.ENCRYPT_MODE, privateK);
        return decrypt(encryptedData, cipher);
    }

    /**
     * Public key decrypt data
     * @param encryptedData Encrypted data
     * @param publicKey The public key(BASE64encode)
     * @return Decrypt data
     * @throws Exception ..
     */
    public static byte[] publicKeyDecrypt(byte[] encryptedData, String publicKey, boolean decrypt) throws Exception {
        byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(decrypt ? Cipher.DECRYPT_MODE : Cipher.ENCRYPT_MODE, publicK);
        return decrypt(encryptedData, cipher);
    }

    /**
     * Get rsa private key
     * @param keyMap Key pair
     * @return The private key
     * @throws Exception ...
     */
    public static String getRsaPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(RSA_PRIVATE_KEY);
        return Base64Utils.encodeToString(key.getEncoded());
    }

    /**
     * Get rsa public key
     * @param keyMap Key pair
     * @return The public key
     * @throws Exception ...
     */
    public static String getRsaPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(RSA_PUBLIC_KEY);
        return Base64Utils.encodeToString(key.getEncoded());
    }

    public static String publicKeyEncrypt(String data, String publicKey) {
        try {
            data = Base64Utils.encodeToString(publicKeyDecrypt(data.getBytes(), publicKey, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String privateKeyEncrypt(String data, String privateKey) {
        String temp = "";
        try {
            byte[] rs = Base64Utils.decodeFromString(data);
            temp = new String(privateKeyDecrypt(rs, privateKey, true), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * ...
     * @param encryptedData ...
     * @param cipher ...
     * @return ...
     * @throws Exception ...
     */
    private static byte[] decrypt(byte[] encryptedData, Cipher cipher) throws Exception {
        int dataLen = encryptedData.length;
        ByteArrayOutputStream opt = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        /* Decrypt data piecewise */
        while (dataLen - offset > 0) {
            if (dataLen - offset > MAX_ENCRYPT) {
                cache = cipher.doFinal(encryptedData, offset, MAX_ENCRYPT);
            }else {
                cache = cipher.doFinal(encryptedData, offset, dataLen - offset);
            }
            opt.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT;
        }
        byte[] decryptedData = opt.toByteArray();
        opt.close();
        return decryptedData;
    }


}
