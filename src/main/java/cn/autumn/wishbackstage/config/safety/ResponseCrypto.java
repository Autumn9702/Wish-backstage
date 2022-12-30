package cn.autumn.wishbackstage.config.safety;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.ex.CryptoException;
import cn.autumn.wishbackstage.util.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Autumn
 * Created in 2022/12/30
 */
@ControllerAdvice
@SuppressWarnings("all")
public class ResponseCrypto implements ResponseBodyAdvice {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpHeaders headers = request.getHeaders();
        List<String> encryptKeyHeadList = headers.get("encryptKey");
        List<String> publicKeyIdHeadList = headers.get("publicKeyId");
        if (encryptKeyHeadList == null || publicKeyIdHeadList == null) return body;
        String encryptKeyHead = encryptKeyHeadList.get(0);
        String publicKeyHead = publicKeyIdHeadList.get(0);
        String privateKey = stringRedisTemplate.opsForValue().get(publicKeyHead);
        if (StringUtils.isEmpty(privateKey)) {
            WishBackstageApplication.getLogger().error("Private key not exist or expire.");
            throw new CryptoException("RSA private key expire.");
        }
        String aesPassword = Rsa.privateKeyEncrypt(encryptKeyHead, privateKey);
        String responseBodyStr = JsonUtil.getGson().toJson(body);
        String responseBodyDecrypt = null;
        try {
            responseBodyDecrypt = Aes.aesEncrypt(responseBodyStr, aesPassword);
        } catch (Exception e) {
            throw new CryptoException("AES symmetric encryption error.");
        }
        return responseBodyDecrypt;
    }
}
