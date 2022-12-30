package cn.autumn.wishbackstage.config.safety;

/**
 * @author Autumn
 * Created in 2022/12/29
 */
public final class RsaEncryptResp {

    private String publicKey;
    private String publicKeyId;

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPublicKeyId(String publicKeyId) {
        this.publicKeyId = publicKeyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyId() {
        return publicKeyId;
    }
}
