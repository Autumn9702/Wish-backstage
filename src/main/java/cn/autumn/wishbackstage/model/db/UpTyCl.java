package cn.autumn.wishbackstage.model.db;

import lombok.Setter;

/**
 * @author cf
 * Created in 2022/11/14
 */
public class UpTyCl {
    @Setter String type;
    @Setter String tableName;
    @Setter String fieldType;
    @Setter String isNull;
    @Setter String comment;

    public UpTyCl() {}

    public UpTyCl(String type, String tableName, String fieldType) {
        this.type = type;
        this.tableName = tableName;
        this.fieldType = fieldType;
    }

    public UpTyCl(String type, String tableName, String fieldType, String isNull) {
        this.type = type;
        this.tableName = tableName;
        this.fieldType = fieldType;
        this.isNull = isNull;
    }

    public UpTyCl(String type, String tableName, String fieldType, String isNull, String comment) {
        this.type = type;
        this.tableName = tableName;
        this.fieldType = fieldType;
        this.isNull = isNull;
        this.comment = comment;
    }
}
