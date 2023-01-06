package cn.autumn.wishbackstage.api;

import cn.autumn.wishbackstage.api.dto.account.LoginDto;
import cn.autumn.wishbackstage.config.resp.Resp;
import cn.autumn.wishbackstage.service.AccountService;
import cn.autumn.wishbackstage.util.RespUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Autumn
 * Created in 2023/1/5
 */
@RestController
@RequestMapping(value = "/wish/login")
public class AuthorizationController {

    @Resource
    private AccountService accountService;

    @PostMapping("")
    public Resp<Object> login(@RequestBody LoginDto loginDto) {

        return RespUtil.ok();
    }
}
