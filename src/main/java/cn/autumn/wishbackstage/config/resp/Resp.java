package cn.autumn.wishbackstage.config.resp;

import java.io.Serializable;

/**
 * @author Autumn
 * Created in 2023/1/5
 */
public class Resp<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Resp() {
        this(RespCode.S.getCode(), RespCode.S.getMsg(), null);
    }

    public Resp(T data) {
        this(RespCode.S.getCode(), RespCode.S.getMsg(), data);
    }

    public Resp(RespCode respCode) {
        this(respCode.getCode(), respCode.getMsg(), null);
    }

    public Resp(RespCode respCode, T data) {
        this(respCode.getCode(), respCode.getMsg(), data);
    }
}
