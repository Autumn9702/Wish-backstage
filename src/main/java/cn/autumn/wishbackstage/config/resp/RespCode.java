package cn.autumn.wishbackstage.config.resp;

/**
 * @author Autumn
 * Created in 2023/1/5
 */
public enum RespCode {

    /**
     * Status code
     */
    S(0, "ok"),
    ERR(-1, "处理异常"),

    ;


    private int code;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public final int getCode() {
        return code;
    }

    public final String getMsg() {
        return msg;
    }

    RespCode() {}

    RespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
