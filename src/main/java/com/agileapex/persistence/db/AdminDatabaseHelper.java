package com.agileapex.persistence.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.agileapex.AgileApexException;
import com.agileapex.common.properties.PropertyHelper;

public class AdminDatabaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(AdminDatabaseHelper.class);
    private static BasicDataSource dataSource;
    private static String databaseClass;
    private static String databaseUrl;
    private static String databaseUser;

    public static void initialize() {
        databaseClass = PropertyHelper.getPublicProp("db.admin_user.class", String.class);
        databaseUrl = PropertyHelper.getPublicProp("db.admin_user.url", String.class);
        databaseUser = PropertyHelper.getPublicProp("db.admin_user.user", String.class);
        String databasePassword = PropertyHelper.getPublicProp("db.admin_user.password", String.class);
        logger.info("Creating new admin DB data source. Database class: '{}' user: {} and url '{}'", databaseClass, databaseUser, databaseUrl);
        dataSource = new org.apache.commons.dbcp.BasicDataSource();
        dataSource.setDriverClassName(databaseClass);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);
        dataSource.setInitialSize(PropertyHelper.getPublicProp("db.admin_user.pool.dbcp.initialSize", Integer.class));
        dataSource.setMaxActive(PropertyHelper.getPublicProp("db.admin_user.pool.dbcp.maxActive", Integer.class));
        dataSource.setMaxIdle(PropertyHelper.getPublicProp("db.admin_user.pool.dbcp.maxIdle", Integer.class));
        dataSource.setMinIdle(PropertyHelper.getPublicProp("db.admin_user.pool.dbcp.minIdle", Integer.class));
        dataSource.setMaxWait(PropertyHelper.getPublicProp("db.admin_user.pool.dbcp.maxWait", Long.class));
    }

    public static DatabaseType getDatabaseType() {
        if (databaseClass.contains("com.mysql")) {
            return DatabaseType.MYSQL;
        } else if (databaseClass.contains("org.postgresql")) {
            return DatabaseType.POSTGRES;
        } else if (databaseClass.contains("TODO")) {
            return DatabaseType.ORACLE;
        } else if (databaseClass.contains("TODO")) {
            return DatabaseType.SQL_SERVER;
        }
        throw new AgileApexException("Unknown database type.");
    }

    public static String escapeReservedWord(String reservedWord) {
        if (getDatabaseType() == DatabaseType.POSTGRES) {
            return "\"" + reservedWord + "\"";
        }
        return reservedWord;
    }

    public static void close() {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (SQLException e) {
                logger.error("Datasource closing failed.", e);
            }
        }
    }

    public static BasicDataSource getDatasource() {
        if (dataSource == null) {
            initialize();
        }
        return dataSource;
    }
}
