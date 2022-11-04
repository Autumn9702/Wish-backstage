package cn.autumn.wishbackstage.config.database;

import cn.autumn.wishbackstage.ex.DatabaseException;
import cn.autumn.wishbackstage.mapper.DatabaseMapper;
import cn.autumn.wishbackstage.model.db.Fields;
import cn.autumn.wishbackstage.model.db.TableField;
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

    public String getDatabaseUrl() {
        return databaseUrl.substring(databaseUrl.lastIndexOf("3306/") + 5, databaseUrl.lastIndexOf("?"));
    }

    @PostConstruct
    public void matchTable() {
        if (matchMode == null) return;
        Set<Class<?>> entities = BeanUtil.getTargetClasses(TableName.class, "cn");
        if (entities.isEmpty()) return;
        List<String> tableList = databaseMapper.getTableList(getDatabaseUrl());
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
        List<TableField> tableEntities = createSql(es);

    }

    /**
     * Create table and update table fields.
     * @param entities The code entity class.
     * @param dbTables The database table's.
     */
    private void updateMode(Set<Class<?>> entities, List<String> dbTables) {}

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
    private void update() {}

    /**
     * Delete table if entity does not exist.
     */
    private void delete() {}

    private List<TableField> createSql(List<Class<?>> es) {
        List<TableField> tfs = new ArrayList<>();
        List<Fields> fds = new ArrayList<>();
        for (Class<?> e : es) {
            String tableName = e.getAnnotation(TableName.class).value();
            Field[] fields = e.getDeclaredFields();
            for (Field field : fields) {
                String fn = field.getName();
                if (fn.equals(FIELD_NAME_ID)) {
                    fds.add(new Fields(fn, Utils.ofFieldType(field), "auto_increment primary key", "id"));
                    continue;
                }
                /* Field do not support serialization. */
                if (field.toString().contains(TRANSIENT)) continue;

                FieldAttribute fan = field.getAnnotation(FieldAttribute.class);
                if (fan != null) {
                    Utils.verifyField(fan.fieldType());
                    String attribute = fan.isNull() ? "null" : "not null";
                    fds.add(new Fields(fn, fan.fieldType(), attribute, fan.comment()));
                }
                System.out.println(field.getType());
                System.out.println(fn);
            }
            System.out.println("ads");
        }
        return null;
    }

}
