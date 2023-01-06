package cn.autumn.wishbackstage.ex;

import cn.autumn.wishbackstage.config.resp.RespCode;

/**
 * @author Autumn
 * Created in 2023/1/6
 * Description
 */
public final class LogicException extends RequestFailException{

    public LogicException(int code, String msg) {
        super(code, msg);
    }

    public LogicException(RespCode respCode) {
        super(respCode);
    }

    public LogicException(RespCode respCode, String msg) {
        super(respCode, msg);
    }
}
