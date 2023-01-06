package cn.autumn.wishbackstage.service;

import cn.autumn.wishbackstage.api.dto.account.LoginDto;

/**
 * @author Autumn
 * Created in 2023/1/6
 * Description
 */
public interface AccountService {

    /**
     * Login
     * @param loginDto login data
     */
    void login(LoginDto loginDto);
}
