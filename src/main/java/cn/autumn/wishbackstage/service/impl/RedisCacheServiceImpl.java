package cn.autumn.wishbackstage.service.impl;

import cn.autumn.wishbackstage.service.RedisCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Autumn
 * Created in 2023/1/5
 * Description Redis service
 */
@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String k, Object v, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(k, v, timeout, unit);
    }

    @Override
    public void set(String k, Object v) {
        redisTemplate.opsForValue().set(k,v);
    }

    @Override
    public void srSet(String k, String v) {
        stringRedisTemplate.opsForValue().set(k, v);
    }

    @Override
    public void srSet(String k, String v, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(k, v, timeout, unit);
    }

    @Override
    public Object get(String k) {
        return redisTemplate.opsForValue().get(k);
    }

    @Override
    public String srGet(String k) {
        return stringRedisTemplate.opsForValue().get(k);
    }

    @Override
    public void delete(String k) {
        redisTemplate.delete(k);
    }

}
