package cn.autumn.wishbackstage.model.db;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cf
 * Created in 2022/11/2
 */
public final class TableStruct {
    @Getter @Setter String COLUMN_NAME;
    @Getter @Setter String IS_NULLABLE;
    @Getter @Setter String DATA_TYPE;
    @Getter @Setter String CHARACTER_MAXIMUM_LENGTH;
    @Getter @Setter String COLUMN_COMMENT;

    public TableStruct() {}

    public TableStruct(String COLUMN_NAME, String IS_NULLABLE, String DATA_TYPE, String CHARACTER_MAXIMUM_LENGTH, String COLUMN_COMMENT) {
        this.COLUMN_NAME = COLUMN_NAME;
        this.IS_NULLABLE = IS_NULLABLE;
        this.DATA_TYPE = DATA_TYPE;
        this.CHARACTER_MAXIMUM_LENGTH = CHARACTER_MAXIMUM_LENGTH;
        this.COLUMN_COMMENT = COLUMN_COMMENT;
    }
}
