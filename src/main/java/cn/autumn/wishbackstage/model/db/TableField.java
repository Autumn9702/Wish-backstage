package cn.autumn.wishbackstage.model.db;

import java.util.List;

/**
 * @author cf
 * Created in 2022/11/4
 */
public record TableField(String tableName, List<Fields> fields) {
}
