package cn.autumn.wishbackstage.service;

import cn.autumn.wishbackstage.model.db.UpTyCl;

/**
 * @author cf
 * Created in 2022/11/2
 */
public interface DatabaseService {

    /**
     * Generate update sql
     * @param u param
     * @param tp type
     * @return sql
     */
    String generateSql(UpTyCl u, String tp);

    /**
     * Exec sql
     * @param sql sql
     */
    void exec(String sql);
}
