package cn.autumn.wishbackstage.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author cf
 * Created in 2022/11/2
 */
public interface DatabaseMapper {

    /**
     * Get all table
     * @param databaseName The database name.
     * @return The table Name's
     */
    List<String> getTableList(@Param("databaseName") String databaseName);

    /**
     * Get Table all struct information.
     * @param databaseName The database name.
     * @param tableName The table name.
     * @return The table struct.
     */
    @MapKey("COLUMN_NAME")
    List<Map<String, String>> getTableStruct(@Param("databaseName") String databaseName, @Param("tableName") String tableName);

    /**
     * Create table
     * @param tableName The table name.
     * @param fields The field information.
     */
    void createTable(@Param("tableName") String tableName, @Param("fields") List<Object> fields);

}
