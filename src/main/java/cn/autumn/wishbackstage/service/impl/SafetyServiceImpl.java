package cn.autumn.wishbackstage.service.impl;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.config.safety.Rsa;
import cn.autumn.wishbackstage.config.safety.RsaEncryptResp;
import cn.autumn.wishbackstage.ex.CryptoException;
import cn.autumn.wishbackstage.service.RedisCacheService;
import cn.autumn.wishbackstage.service.SafetyService;
import cn.autumn.wishbackstage.util.Crypto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Autumn
 * Created in 2022/12/29
 */
@Service("safetyService")
public class SafetyServiceImpl implements SafetyService {

    private final static long RSA_PRIVATE_KEY_REDIS_TIMEOUT = 36000L;

    @Resource
    private RedisCacheService redisCacheService;

    @Override
    public RsaEncryptResp getRsaPublicKey() {
        RsaEncryptResp resp = new RsaEncryptResp();
        try {
            Map<String, Object> stringObjectMap = Rsa.generateKeyPair();
            String publicKey = Rsa.getRsaPublicKey(stringObjectMap);
            String privateKey = Rsa.getRsaPrivateKey(stringObjectMap);

            String redisKey = Crypto.generateSecret();
            redisCacheService.srSet(redisKey, privateKey, RSA_PRIVATE_KEY_REDIS_TIMEOUT, TimeUnit.SECONDS);

            resp.setPublicKey(publicKey);
            resp.setPublicKeyId(redisKey);
        } catch (Exception e) {
            WishBackstageApplication.getLogger().error("Get rsa public key error!");
            throw new CryptoException("Get rsa public key error!");
        }
        return resp;
    }
}
