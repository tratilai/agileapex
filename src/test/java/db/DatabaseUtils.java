package db;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import com.agileapex.common.properties.PropertyHelper;

public class DatabaseUtils {
    protected static BasicDataSource basicDataSource;
    protected static String databaseClass;
    protected static String databaseUrl;
    protected static String databaseUser;

    public void initialize() {
        databaseClass = PropertyHelper.getPublicProp("db.class", String.class);
        databaseUrl = PropertyHelper.getPublicProp("db.url", String.class);
        databaseUser = PropertyHelper.getPublicProp("db.user", String.class);
        String databasePassword = PropertyHelper.getPublicProp("db.password", String.class);
        basicDataSource = new org.apache.commons.dbcp.BasicDataSource();
        basicDataSource.setDriverClassName(databaseClass);
        basicDataSource.setUrl(databaseUrl);
        basicDataSource.setUsername(databaseUser);
        basicDataSource.setPassword(databasePassword);
        basicDataSource.setInitialSize(PropertyHelper.getPublicProp("db.pool.dbcp.initialSize", Integer.class));
        basicDataSource.setMaxActive(PropertyHelper.getPublicProp("db.pool.dbcp.maxActive", Integer.class));
        basicDataSource.setMaxIdle(PropertyHelper.getPublicProp("db.pool.dbcp.maxIdle", Integer.class));
        basicDataSource.setMinIdle(PropertyHelper.getPublicProp("db.pool.dbcp.minIdle", Integer.class));
        basicDataSource.setMaxWait(PropertyHelper.getPublicProp("db.pool.dbcp.maxWait", Long.class));
    }

    public static BasicDataSource getDatasource() {
        return basicDataSource;
    }

    public void closeDatabasource() {
        try {
            basicDataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
