package cn.autumn.wishbackstage.api;

import cn.autumn.wishbackstage.api.dto.account.LoginDto;
import cn.autumn.wishbackstage.config.resp.Resp;
import cn.autumn.wishbackstage.util.RespUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author Autumn
 * Created in 2023/1/5
 */
@RestController
@RequestMapping(value = "/wish/login")
public class AuthorizationController {

    @PostMapping("")
    public Resp<Object> login(@RequestBody LoginDto loginDto) {

        return RespUtil.ok();
    }
}
