package cn.autumn.wishbackstage.service.impl;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.model.db.UpTyCl;
import cn.autumn.wishbackstage.service.DatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static cn.autumn.wishbackstage.config.Configuration.DELETE_SQL;

/**
 * @author cf
 * Created in 2022/11/2
 */
@Service
public final class DatabaseServiceImpl implements DatabaseService {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    @SuppressWarnings("deprecation")
    public String generateSql(UpTyCl u, String tp) {
        StringBuilder sql = new StringBuilder("alter table " + u.getTableName() + tp + " column " + u.getFieldName());
        if (tp.equals(DELETE_SQL)) return sql.toString();
        if (!StringUtils.isEmpty(u.getFieldType())) {
            sql.append(" ").append(u.getFieldType());
        }
        if (!StringUtils.isEmpty(u.getIsNull())) {
            sql.append(" ").append(u.getIsNull());
        }
        if (!StringUtils.isEmpty(u.getComment())) {
            sql.append(" comment '").append(u.getComment()).append("'");
        }
        return sql.toString();
    }

    @Override
    public void exec(String sql) {

        try (Connection c = connection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            WishBackstageApplication.getLogger().error(e.getMessage());
        }

    }

    /**
     * private
     */

    private Connection connection() throws ClassNotFoundException, SQLException {
        /* Exec driver */
        Class.forName(driver);
        /* Connection */
        return DriverManager.getConnection(url, username, password);
    }
}
