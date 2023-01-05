package cn.autumn.wishbackstage.serve;

import cn.autumn.wishbackstage.service.RedisCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static cn.autumn.wishbackstage.config.Configuration.*;

/**
 * @author Autumn
 * Created in 2023/1/5
 * Description
 */
@Service("redisCacheServe")
public class RedisCacheServe {

    @Resource
    private RedisCacheService redisCacheService;

    public void online(String k, String v) {
        redisCacheService.srSet(ACCOUNT_ONLINE + k, v, ONLINE_TIME, TimeUnit.SECONDS);
    }

    public void onlineRefresh(String k) {
        redisCacheService.srSet(ACCOUNT_ONLINE_RSH + k, k, ONLINE_TIME_RSH, TimeUnit.SECONDS);
    }

    public String getOnlineUser(String k) {
        return redisCacheService.srGet(ACCOUNT_ONLINE + k);
    }

    public String getOnlineRefresh(String k) {
        return redisCacheService.srGet(ACCOUNT_ONLINE_RSH + k);
    }
}
