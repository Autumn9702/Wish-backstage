package cn.autumn.wishbackstage.api;

import cn.autumn.wishbackstage.config.resp.Resp;
import cn.autumn.wishbackstage.util.RespUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Autumn
 * Created in 2023/1/5
 */
@RestController
@RequestMapping(value = "/wish/login")
public class AuthorizationController {

    @PostMapping("")
    public Resp<Object> login() {
        return RespUtil.ok();
    }
}
