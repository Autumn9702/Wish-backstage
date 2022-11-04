package cn.autumn.wishbackstage.model.db;

/**
 * @author cf
 * Created in 2022/11/2
 */
public record TableStruct(String COLUMN_NAME, String IS_NULLABLE, String DATA_TYPE,
                          String CHARACTER_MAXIMUM_LENGTH, String COLUMN_COMMENT) {
}
