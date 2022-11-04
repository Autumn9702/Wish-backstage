package cn.autumn.wishbackstage.model.db;

import java.io.Serializable;

/**
 * @author cf
 * Created in 2022/11/3
 */
public record Fields(String field, String fieldType, String attribute, String comment) implements Serializable {
}
