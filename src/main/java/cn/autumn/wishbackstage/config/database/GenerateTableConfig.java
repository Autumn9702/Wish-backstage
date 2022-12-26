package cn.autumn.wishbackstage.config.database;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.ex.DatabaseException;
import cn.autumn.wishbackstage.mapper.DatabaseMapper;
import cn.autumn.wishbackstage.model.db.*;
import cn.autumn.wishbackstage.service.DatabaseService;
import cn.autumn.wishbackstage.util.BeanUtil;
import cn.autumn.wishbackstage.util.Utils;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.autumn.wishbackstage.config.Configuration.*;

/**
 * @author cf
 * Created in 2022/11/2
 */
@Configuration
public class GenerateTableConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${database-management.match-mode}")
    private String matchMode;

    @Resource
    private DatabaseMapper databaseMapper;

    @Resource
    private DatabaseService databaseService;

    public String getDatabase() {
        return url.substring(url.lastIndexOf("3306/") + 5, url.lastIndexOf("?"));
    }

    @PostConstruct
    public void matchTable() {
        if (matchMode == null) return;
        Set<Class<?>> entities = BeanUtil.getTargetClasses(TableName.class, "cn");
        if (entities.isEmpty()) return;
        List<String> tableList = databaseMapper.getTableList(getDatabase());
        logicMatch(entities, tableList);
    }

    @SuppressWarnings("all")
    private void logicMatch(Set<Class<?>> entities, List<String> dbTables) {
        switch (matchMode) {
            case CREATE -> createMode(entities, dbTables);
            case UPDATE -> updateMode(entities, dbTables);
            case DELETE -> deleteMode(entities, dbTables);
            default ->  throw new DatabaseException("Pattern matching failed: " + matchMode);
        }
    }

    /**
     * Create table.
     * @param entities The code entity class.
     * @param dbTables The database table's.
     */
    private void createMode(Set<Class<?>> entities, List<String> dbTables) {
        List<Class<?>> es = create(entities, dbTables);
        List<TableField> tableEntities = createTableInfo(es);
        for (TableField tableEntity : tableEntities) {
            databaseMapper.createTable(tableEntity.tableName(), tableEntity.fields());
        }
    }

    /**
     * Create table and update table fields.
     * @param entities The code entity class.
     * @param dbTables The database table's.
     */
    private void updateMode(Set<Class<?>> entities, List<String> dbTables) {
        createMode(entities, dbTables);
        Set<Class<?>> same = update(entities, dbTables);
        updateTableInfo(same);
    }

    /**
     * Create + Update + Delete
     * @param entities The code entity class.
     * @param dbTables The database table's.
     */
    private void deleteMode(Set<Class<?>> entities, List<String> dbTables) {
        createMode(entities, dbTables);
        updateMode(entities, dbTables);
        delete(entities, dbTables);
    }

    /**
     * A table is added if the table does not exist.
     */
    private List<Class<?>> create(Set<Class<?>> entities, List<String> dbTables) {
        return entities.stream().filter(e -> !dbTables.contains(e.getAnnotation(TableName.class).value())).toList();
    }

    /**
     * Added and deleted when fields change.
     */
    private Set<Class<?>> update(Set<Class<?>> entities, List<String> dbTables) {
        return entities.stream().filter(e -> dbTables.contains(e.getAnnotation(TableName.class).value())).collect(Collectors.toSet());
    }

    /**
     * Delete table if entity does not exist.
     */
    private void delete(Set<Class<?>> entities, List<String> dbTables) {
        List<String> ent = new ArrayList<>();
        entities.forEach(e -> ent.add(e.getAnnotation(TableName.class).value()));
        List<String> del = dbTables.stream().filter(d -> !ent.contains(d)).toList();
        if (del != null && !del.isEmpty()) {
            try {
                del.forEach(t -> databaseMapper.dropTable(t));
            } catch (Exception e) {
                WishBackstageApplication.getLogger().error(e.getMessage());
            }
        }
    }

    /**
     * Build table information
     * @param es The information about the table to be created.
     */
    private List<TableField> createTableInfo(List<Class<?>> es) {
        List<TableField> tfs = new ArrayList<>();
        for (Class<?> e : es) {
            List<Fields> fds = new ArrayList<>();
            String tableName = e.getAnnotation(TableName.class).value();
            Field[] fields = e.getDeclaredFields();
            for (Field field : fields) {
                String fn = Utils.upperCharToUnderLine(field.getName());
                if (fn.equals(FIELD_NAME_ID)) {
                    fds.add(new Fields(fn, Utils.ofFieldType(field), "NOT NULL AUTO_INCREMENT", "id"));
                    continue;
                }
                /* Field do not support serialization. */
                if (field.toString().contains(TRANSIENT)) continue;
                /* Read the annotation or field information. */
                FieldAttribute fan = field.getAnnotation(FieldAttribute.class);
                if (fan != null) {
                    Utils.verifyField(fan.fieldType());
                    String attribute = fan.isNull() ? "NULL" : "NOT NULL";
                    fds.add(new Fields(fn, fan.fieldType(), attribute, fan.comment().isEmpty() ? fn : fan.comment()));
                } else {
                    fds.add(new Fields(fn, Utils.ofFieldType(field), PARAM_NULL, fn));
                }
            }
            tfs.add(new TableField(tableName, fds));
        }
        return tfs;
    }

    private void updateTableInfo(Set<Class<?>> es) {
        for (Class<?> e : es) {
            String tableName = e.getAnnotation(TableName.class).value();
            List<TableStruct> entityStruct = classToTableStruct(e);
            List<TableStruct> tableStruct = databaseMapper.getTableStruct(getDatabase(), e.getAnnotation(TableName.class).value());
            UAD uad = determinesFieldUpdated(tableName, entityStruct, tableStruct);
            if (uad != null) {
                processUpdateDB(uad);
            }
        }
    }

    /**
     * Process table field change.
     * @param uad Update create delete.
     */
    private void processUpdateDB(UAD uad) {

        if (uad.getUpdate() != null) {
            List<UpTyCl> updateFields = uad.getUpdate();

            for (UpTyCl utc : updateFields) {
                databaseService.exec(databaseService.generateSql(utc, UPDATE_SQL));
            }
        }

        if (uad.getCreate() != null) {
            List<UpTyCl> createFields = uad.getCreate();
            for (UpTyCl c : createFields) {
                databaseService.exec(databaseService.generateSql(c, ADD_SQL));
            }
        }

        if (uad.getDelete() != null) {
            List<UpTyCl> deleteFields = uad.getDelete();

            for (UpTyCl d : deleteFields) {
                databaseService.exec(databaseService.generateSql(d, DELETE_SQL));
            }
        }

    }

    /**
     * Update field or add field or delete field.
     */
    private UAD determinesFieldUpdated(String tableName, List<TableStruct> ent, List<TableStruct> dbt) {

        List<UpTyCl> updateField = new ArrayList<>();
        List<UpTyCl> addField = null;
        List<UpTyCl> delField = null;

        for (TableStruct ef : ent) {
            boolean sameField = false;
            for (TableStruct tf : dbt) {
                if (ef.getCOLUMN_NAME().equals(tf.getCOLUMN_NAME())) {
                    UpTyCl u = matchEfToDf(tableName, ef, tf);
                    if (u != null) {
                        updateField.add(u);
                    }
                    sameField = true;
                }
            }
            /* Entity has a new field. */
            if (!sameField) {
                if (addField == null) {
                    addField = new ArrayList<>();
                }
                String isNull = ef.getIS_NULLABLE().equals(DB_FIELD_IS_NULL) ? PARAM_NULL : PARAM_NOT_NULL;
                addField.add(new UpTyCl(FIELD_CREATE, tableName, ef.getCOLUMN_NAME(), ef.getDATA_TYPE() + "(" + ef.getCHARACTER_MAXIMUM_LENGTH() + ")", isNull, ef.getCOLUMN_COMMENT()));
            }
        }

        if (addField != null) {
            List<TableStruct> del = new ArrayList<>();
            for (TableStruct d : dbt) {
                boolean sf = false;
                for (TableStruct e : ent) {
                    if (e.getCOLUMN_NAME().equals(d.getCOLUMN_NAME())) {
                        sf = true;
                        break;
                    }
                }
                if (sf) continue;
                del.add(d);
            }

            if (!del.isEmpty()) {
                delField = new ArrayList<>();
                for (TableStruct t : del) {
                    String isNull = t.getIS_NULLABLE().equals(DB_FIELD_IS_NULL) ? PARAM_NULL : PARAM_NOT_NULL;
                    delField.add(new UpTyCl(t.getCOLUMN_NAME(), tableName, t.getCOLUMN_NAME(), t.getDATA_TYPE(), isNull, t.getCOLUMN_COMMENT()));
                }
            }
        }

        UAD uad = new UAD();
        boolean process = false;

        if (!updateField.isEmpty()) {
            process = true;
            uad.setUpdate(updateField);
        }

        if (addField != null && !addField.isEmpty()) {
            process = true;
            uad.setCreate(addField);
        }

        if (delField != null) {
            process = true;
            uad.setDelete(delField);
        }

        if (process) return uad;

        return null;
    }

    /**
     * Verify field changes.
     */
    private UpTyCl matchEfToDf(String tableName, TableStruct ef, TableStruct df) {
        UpTyCl u = null;
        String fieldType = ef.getCHARACTER_MAXIMUM_LENGTH() == null ?
                Utils.ofDefaultFieldLen(ef.getDATA_TYPE()) : ef.getDATA_TYPE() + "(" + ef.getCHARACTER_MAXIMUM_LENGTH() + ")";
        /* If the field type and length change. */
        if (!ef.getDATA_TYPE().equals(df.getDATA_TYPE()) || ef.getCHARACTER_MAXIMUM_LENGTH() != null &&
                df.getCHARACTER_MAXIMUM_LENGTH() != null && !ef.getCHARACTER_MAXIMUM_LENGTH().equals(df.getCHARACTER_MAXIMUM_LENGTH())) {
            if (ef.getCHARACTER_MAXIMUM_LENGTH() != null && !ef.getCHARACTER_MAXIMUM_LENGTH().isEmpty()) {
                ef.setDATA_TYPE(ef.getDATA_TYPE() + "(" + ef.getCHARACTER_MAXIMUM_LENGTH() + ")");
            }
            u = new UpTyCl(FIELD_TYPE, tableName, ef.getCOLUMN_NAME(), fieldType);
        }
        /* Field changes to null by default. */
        if (!ef.getIS_NULLABLE().equals(df.getIS_NULLABLE())) {
            String isNull = ef.getIS_NULLABLE().equals(DB_FIELD_IS_NULL) ? PARAM_NULL : PARAM_NOT_NULL;
            if (u == null) {
                u = new UpTyCl(FIELD_NULL, tableName, ef.getCOLUMN_NAME(), fieldType,  isNull);
            }else {
                u.setIsNull(isNull);
            }
        }
        /* Comment change. */
        if (!ef.getCOLUMN_COMMENT().equals(df.getCOLUMN_COMMENT())) {
            String isNull = ef.getIS_NULLABLE().equals(DB_FIELD_IS_NULL) ? PARAM_NULL : PARAM_NOT_NULL;
            if (u == null) {
                u = new UpTyCl(FIELD_COMMENT, tableName, ef.getCOLUMN_NAME(), fieldType, isNull, ef.getCOLUMN_COMMENT());
            }else {
                u.setComment(ef.getCOLUMN_COMMENT());
            }
        }

        return u;
    }


    /**
     * Convert an entity class to a TableStruct.
     * @param e The entity class.
     * @return The TableStruct.
     */
    private List<TableStruct> classToTableStruct(Class<?> e) {
        List<TableStruct> structs = new ArrayList<>();
        Field[] fields = e.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = Utils.upperCharToUnderLine(field.getName());
            if (fieldName.equals(FIELD_NAME_ID)) {
                String fieldType = Utils.ofFieldType(field);
                structs.add(new TableStruct(fieldName, DB_FIELD_NOT_NULL, ofFieldType(fieldType), null, FIELD_NAME_ID));
                continue;
            }
            FieldAttribute fa = field.getAnnotation(FieldAttribute.class);
            if (fa != null) {
                structs.add(new TableStruct(fieldName, fa.isNull() ? DB_FIELD_IS_NULL : DB_FIELD_NOT_NULL, ofFieldType(fa.fieldType()), ofFieldLen(fa.fieldType()), fa.comment().isEmpty() ? PARAM_EMPTY : fa.comment()));
                continue;
            }
            String fieldType = Utils.ofFieldType(field);
            structs.add(new TableStruct(fieldName, DB_FIELD_IS_NULL, ofFieldType(fieldType), ofFieldLen(fieldType), fieldName));
        }
        return structs;
    }

    /**
     * The type of the intercepted field.
     * @param field The field information.
     * @return The field type.
     */
    private String ofFieldType(String field) {

        if (!field.contains("(")) {
            return field;
        }

        return field.substring(0, field.lastIndexOf("("));
    }

    /**
     * The field length of the intercepted field.
     * @param field The field information.
     * @return The field length.
     */
    private String ofFieldLen(String field) {

        if (!field.contains("(")) {
            return "";
        }

        return field.substring(field.lastIndexOf("(") + 1, field.lastIndexOf(")"));
    }

    public List<String> getTableList() {
        return databaseMapper.getTableList(getDatabase());
    }

}
