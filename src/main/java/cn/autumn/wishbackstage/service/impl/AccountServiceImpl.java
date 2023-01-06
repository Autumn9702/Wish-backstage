package cn.autumn.wishbackstage.service.impl;

import cn.autumn.wishbackstage.api.dto.account.LoginDto;
import cn.autumn.wishbackstage.config.resp.RespCode;
import cn.autumn.wishbackstage.database.entity.user.Account;
import cn.autumn.wishbackstage.ex.LogicException;
import cn.autumn.wishbackstage.ex.RequestFailException;
import cn.autumn.wishbackstage.mapper.AccountMapper;
import cn.autumn.wishbackstage.serve.RedisCacheServe;
import cn.autumn.wishbackstage.service.AccountService;
import cn.autumn.wishbackstage.util.DateUtil;
import cn.autumn.wishbackstage.util.Utils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static cn.autumn.wishbackstage.config.Configuration.*;

/**
 * @author Autumn
 * Created in 2023/1/6
 * Description
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private RedisCacheServe redisCacheServe;

    @Resource
    private AccountMapper accountMapper;

    @Override
    @SuppressWarnings("all")
    public void login(LoginDto loginDto) {
        verifyCode(loginDto.getVerifyCode());
        Account account;
        if (loginDto.getAccount().matches(FORMAT_MATCH.rgxPhone)) {
            account = accountMapper.selectOne(
                    new LambdaQueryWrapper<Account>().eq(Account::getPhone, loginDto.getAccount()));
        } else if (loginDto.getAccount().matches(FORMAT_MATCH.rgxEmail)) {
            account = accountMapper.selectOne(
                    new LambdaQueryWrapper<Account>().eq(Account::getEmail, loginDto.getAccount()));
        } else {
            account = accountMapper.selectOne(
                    new LambdaQueryWrapper<Account>().eq(Account::getUsername, loginDto.getAccount()));
        }

        if (account == null) throw new LogicException(RespCode.LG_ACCOUNT_ERR);

        if (account.isBanned()) throw new LogicException(RespCode.ERR, "Account it has been banned. \nReason for prohibition: " + account.getBanReason() + "\nBanned time: " + DateUtil.secondToStr(account.getBanStartTime()) + " - " + DateUtil.bannedTimeSecondToStr(account.getBanEndTime()));
        if (!account.getPassword().equals(loginDto.getPassword())) {
            // TODO: 2023/1/6 13:54 一小时限制三次错误
            // TODO: 2023/1/6 13:55 一天限制9次 否则封号联系管理员解封
            throw new LogicException(RespCode.LG_PASSWORD_ERR);
        }
        boolean web = account.getType().equals(USER_TYPE);
        // TODO: 2023/1/6 14:24 return login data
    }

    private void verifyCode(String code) {
        String ip = Utils.getIp();
        String value = redisCacheServe.getValue(REDIS_KEY + ip);
        if (value != null && !value.equalsIgnoreCase(code)) {
            System.out.println("refresh code");
            throw new RequestFailException(RespCode.LG_VERIFY_CODE_ERROR);
        }
        if (value != null) {
            redisCacheServe.delete(REDIS_KEY + ip);
        }
    }

}
