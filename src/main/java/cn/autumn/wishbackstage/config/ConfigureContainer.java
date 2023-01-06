package cn.autumn.wishbackstage.config;

/**
 * @author cf
 * Created in 2022/11/3
 */
public sealed class ConfigureContainer permits Configuration {

    public RedisKey redisKey = new RedisKey();
    public HttpIpParam httpIpParam = new HttpIpParam();
    public FormatMatch formatMatch = new FormatMatch();
    public DateFormat dateFormat = new DateFormat();

     public static class HttpIpParam {
        /**
         * HTTP request ip keys
         */
        public final String ipKeyForwarded = "x-forwarded-for";
        public final String ipKeyProxy = "Proxy-Client-IP";
        public final String ipKeyWlProxy = "WL-Proxy-Client-IP";
        public final String ipKeyHttp = "HTTP_CLIENT_IP";
        public final String ipKeyHttpX = "HTTP_X_FORWARDED_FOR";

        /**
         * Other
         */
        public final String unknown = "unknown";
    }

    /**
     * Redis keys
     */
    public static class RedisKey {
         public final String verifyCodeKey = "verify_code_";
    }

    /**
     * Format match
     */
    public static class FormatMatch {
        public final String rgxEmail = "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";
        public final String rgxPhone = "^1(3|4|5|7|8|9)\\d{9}$";
    }

    /**
     * Date format
     */
    public static class DateFormat {

        public final String ymdHms = "yyyy-MM-dd HH:mm:ss";
    }
}
