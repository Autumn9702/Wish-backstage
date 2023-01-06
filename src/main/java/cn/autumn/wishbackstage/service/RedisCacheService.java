package cn.autumn.wishbackstage.service;

import java.util.concurrent.TimeUnit;

/**
 * @author Autumn
 * Created in 2023/1/5
 * Description
 */
public interface RedisCacheService {

    /**
     * Store in redis (Time limit)
     * @param k Key
     * @param v Value
     * @param timeout Time limit
     * @param unit Time limit type
     */
    void set(String k, Object v, long timeout, TimeUnit unit);

    /**
     * Store in redis (Forever)
     * @param k Key
     * @param v Value
     */
    void set(String k, Object v);

    /**
     * Store in redis >/value is string type (Forever)
     * @param k Key
     * @param v Value
     */
    void srSet(String k, String v);

    /**
     * Store in redis >/value is string type (Time limit)
     * @param k Key
     * @param v Value
     * @param timeout Time limit
     * @param unit Time limit type
     */
    void srSet(String k, String v, long timeout, TimeUnit unit);

    /**
     * Get data from redis (Value is object type)
     * @param k Key
     * @return Value (Object)
     */
    Object get(String k);

    /**
     * Get data from redis (Value is string type)
     * @param k Key
     * @return Value (String)
     */
    String srGet(String k);

    /**
     * Delete value by key
     * @param k Key
     */
    void delete(String k);
}
