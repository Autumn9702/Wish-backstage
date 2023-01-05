package cn.autumn.wishbackstage.util;

import cn.autumn.wishbackstage.config.resp.Resp;
import cn.autumn.wishbackstage.config.resp.RespCode;

/**
 * @author Autumn
 * Created in 2023/1/5
 */
public final class RespUtil {

    public static <T> Resp<T> ok() {
        return new Resp<>();
    }

    public static <T> Resp<T> ok(T data) {
        return new Resp<>(data);
    }

    public static <T> Resp<T> err() {
        return new Resp<>(RespCode.ERR);
    }

    public static <T> Resp<T> err(RespCode respCode) {
        return new Resp<>(respCode);
    }

    public static <T> Resp<T> err(RespCode respCode, T data) {
        return new Resp<>(respCode, data);
    }
}
