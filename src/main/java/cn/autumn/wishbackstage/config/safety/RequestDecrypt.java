package cn.autumn.wishbackstage.config.safety;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.service.RedisCacheService;
import cn.autumn.wishbackstage.util.JsonUtil;
import com.google.gson.JsonObject;
import io.lettuce.core.RedisException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * @author Autumn
 * Created in 2022/12/30
 */
@ControllerAdvice
public class RequestDecrypt extends RequestBodyAdviceAdapter {

    @Resource
    private RedisCacheService redisCacheService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        HttpHeaders headers = inputMessage.getHeaders();
        List<String> encryptKeyList = headers.get("encryptKey");
        List<String> publicKeyIdList = headers.get("publicKeyId");
        String requestBody = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
        JsonObject bodyJson = JsonUtil.getGson().fromJson(requestBody, JsonObject.class);
        JsonObject dataJson = bodyJson.getAsJsonObject("data");
        String encrypts = dataJson.get("encrypts").getAsString();
        String encryptKey = encryptKeyList == null ? null : encryptKeyList.get(0);
        String publicKeyId = publicKeyIdList == null ? null : publicKeyIdList.get(0);

        if (StringUtils.hasLength(encrypts) && StringUtils.hasLength(encryptKey) && StringUtils.hasLength(publicKeyId)) {
            String aesDecryptData = decryptToString(encrypts, encryptKey, publicKeyId);
            HashMap hashMap = JsonUtil.getGson().fromJson(requestBody, HashMap.class);
            hashMap.put("data", aesDecryptData);
            requestBody = JsonUtil.getGson().toJson(hashMap);
        }
        InputStream inputStream = IOUtils.toInputStream(requestBody, StandardCharsets.UTF_8);
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                return inputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    /**
     * Decrypt the data
     * @param encrypts Encrypted data
     * @param encryptKey AES The key is symmetric encryption
     * @param publicKeyId RSA algorithm private key deposit redis (key)
     * @return Decrypted data
     */
    private String decryptToString(String encrypts, String encryptKey, String publicKeyId) {
        String privateKey = redisCacheService.srGet(publicKeyId);
        if (StringUtils.isEmpty(privateKey)) {
            WishBackstageApplication.getLogger().error("Private key expire.");
            throw new RedisException("RSA encrypt private key expire.");
        }

        String aesPassword = Rsa.privateKeyEncrypt(encryptKey, privateKey);

        String data = null;
        try {
            data = Aes.aesDecrypt(encrypts, aesPassword);
        } catch (Exception e) {
            WishBackstageApplication.getLogger().error("Decrypt data error.");
        }
        return data;
    }
}
