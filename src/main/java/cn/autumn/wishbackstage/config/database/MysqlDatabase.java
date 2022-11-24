package cn.autumn.wishbackstage.config.database;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.model.db.UpTyCl;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author cf
 * Created in 2022/11/18
 */
public final class MysqlDatabase {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection connection() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public void updateField(UpTyCl u) {
        String updateSql = "ALTER TABLE %s MODIFY COLUMN %s ";
        updateSql.formatted(u.getTableName(), u.getFieldName());
        try (Connection c = connection(); Statement s = c.createStatement()) {


        } catch (Exception e) {
            WishBackstageApplication.getLogger().error("Update field error. table name: " + u.getTableName() + " field: " + u.getFieldName());
        }
    }

    public static void main(String[] args) {
        String updateSql = "ALTER TABLE %s MODIFY COLUMN %s ";
        updateSql = updateSql.formatted("user", "username");
        System.out.println(updateSql);
    }

}
