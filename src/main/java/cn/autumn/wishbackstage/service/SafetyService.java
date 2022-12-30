package cn.autumn.wishbackstage.service;

import cn.autumn.wishbackstage.config.safety.RsaEncryptResp;

/**
 * @author Autumn
 * Created in 2022/12/29
 */
public interface SafetyService {

    /**
     * Get Rsa public key
     * @return The public key and id.
     */
    RsaEncryptResp getRsaPublicKey();
}
