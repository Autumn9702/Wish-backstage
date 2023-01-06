package cn.autumn.wishbackstage.ex;

import cn.autumn.wishbackstage.config.resp.RespCode;

/**
 * @author Autumn
 * Created in 2023/1/6
 * Description Request error
 */
public sealed class RequestFailException extends RuntimeException permits LogicException {

    private final int code;

    public int getCode() {
        return code;
    }

    public RequestFailException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public RequestFailException(RespCode respCode) {
        super(respCode.getMsg());
        this.code = respCode.getCode();
    }

    public RequestFailException(RespCode respCode, String msg) {
        super(msg);
        this.code = respCode.getCode();
    }
}
