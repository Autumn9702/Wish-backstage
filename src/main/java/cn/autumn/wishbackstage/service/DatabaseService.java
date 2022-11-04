package cn.autumn.wishbackstage.service;

import java.util.List;

/**
 * @author cf
 * Created in 2022/11/2
 */
public interface DatabaseService {

    /**
     * Get all table
     * @return The table Name's
     */
    List<String> getTableList();
}
