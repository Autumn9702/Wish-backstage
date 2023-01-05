package cn.autumn.wishbackstage.api.dto.account;

import lombok.Data;

/**
 * @author Autumn
 * Created in 2023/1/5
 * Description
 */
@Data
public class LoginDto {

    private String account;
    private String password;
    private String verifyCode;

}
