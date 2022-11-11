package cn.autumn.wishbackstage.config.database;

import cn.autumn.wishbackstage.ex.DatabaseException;
import cn.autumn.wishbackstage.mapper.DatabaseMapper;
import cn.autumn.wishbackstage.model.db.Fields;
import cn.autumn.wishbackstage.model.db.TableField;
import cn.autumn.wishbackstage.model.db.TableStruct;
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

import static cn.autumn.wishbackstage.config.ConfigureContainer.*;

/**
 * @author cf
 * Created in 2022/11/2
 */
@Configuration
public class GenerateTableConfig {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${database-management.match-mode}")
    private String matchMode;

    @Resource
    private DatabaseMapper databaseMapper;

    public String getDatabaseByUrl() {
        return databaseUrl.substring(databaseUrl.lastIndexOf("3306/") + 5, databaseUrl.lastIndexOf("?"));
    }

    @PostConstruct
    public void matchTable() {
        if (matchMode == null) return;
        Set<Class<?>> entities = BeanUtil.getTargetClasses(TableName.class, "cn");
        if (entities.isEmpty()) return;
        List<String> tableList = databaseMapper.getTableList(getDatabaseByUrl());
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
        Set<Class<?>> same = update(entities, dbTables);
        List<TableField> updateInfo = updateTableInfo(same);


    }

    /**
     * Create + Update + Delete
     * @param entities The code entity class.
     * @param dbTables The database table's.
     */
    private void deleteMode(Set<Class<?>> entities, List<String> dbTables) {}

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
    private void delete() {}

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
                String fn = field.getName();
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

    private List<TableField> updateTableInfo(Set<Class<?>> es) {
        for (Class<?> e : es) {
            List<TableStruct> entityStruct = classToTableStruct(e);
            List<TableStruct> tableStruct = databaseMapper.getTableStruct(getDatabaseByUrl(), e.getAnnotation(TableName.class).value());


        }
        return null;
    }

    private List<TableStruct> classToTableStruct(Class<?> e) {
        List<TableStruct> structs = new ArrayList<>();
        Field[] fields = e.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(FIELD_NAME_ID)) {
                String fieldType = Utils.ofFieldType(field);
                structs.add(new TableStruct(field.getName(), PARAM_NOT_NULL, ofFieldType(fieldType), ofFieldLen(fieldType), FIELD_NAME_ID));
                continue;
            }
            FieldAttribute fa = field.getAnnotation(FieldAttribute.class);
            if (fa != null) {
                structs.add(new TableStruct(field.getName(), fa.isNull() ? PARAM_NULL : PARAM_NOT_NULL, ofFieldType(fa.fieldType()), ofFieldLen(fa.fieldType()), fa.comment().isEmpty() ? field.getName() : fa.comment()));
                continue;
            }
            String fieldType = Utils.ofFieldType(field);
            structs.add(new TableStruct(field.getName(), PARAM_NULL, ofFieldType(fieldType), ofFieldLen(fieldType), PARAM_EMPTY));
        }
        return structs;
    }

    private String ofFieldType(String field) {
        return field.substring(0, field.lastIndexOf("("));
    }

    private String ofFieldLen(String field) {
        return field.substring(field.lastIndexOf("(") + 1, field.lastIndexOf(")"));
    }

    public static void main(String[] args) {
        TableStruct t1 = new TableStruct("1", "1", "1", "1", "1");
        TableStruct t2 = new TableStruct("1", "1", "1", "1", "1");
        System.out.println("t1 = t2: " + t1.c);
    }

}
