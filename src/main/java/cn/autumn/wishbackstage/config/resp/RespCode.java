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

    TK_NULL(10010, "凭证缺失"),
    TK_EXPIRE(10011, "凭证超时"),
    TK_PERJURY(10015, "伪证"),
    TK_RE_LOGIN(10012, "请重新登录"),

    LG_VERIFY_CODE_ERROR(10050, "验证码错误"),
    LG_ACCOUNT_ERR(10100, "账号不存在"),
    LG_PASSWORD_ERR(10101, "密码错误"),
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
