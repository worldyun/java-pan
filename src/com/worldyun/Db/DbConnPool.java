package com.worldyun.Db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * DbConnPool
 */
public class DbConnPool {

    	//使用单利模式创建数据库连接池
        private static DbConnPool instance;
        private static ComboPooledDataSource dataSource;
     
        private DbConnPool() throws SQLException, PropertyVetoException {
            dataSource = new ComboPooledDataSource();
     
            dataSource.setUser("xxx");		//用户名
            dataSource.setPassword("xxx"); //密码
            dataSource.setJdbcUrl("jdbc:mysql://xxxxxxxx.xxx/pan?serverTimezone=GMT");//数据库地址
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setInitialPoolSize(100); //初始化连接数
            dataSource.setMinPoolSize(50);//最小连接数
            dataSource.setMaxPoolSize(300);//最大连接数
            dataSource.setMaxStatements(50);//最长等待时间
            dataSource.setMaxIdleTime(60);//最大空闲时间，单位毫秒
        }
     
        public static final DbConnPool getInstance() {
            if (instance == null) {
                try {
                    instance = new DbConnPool();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return instance;
        }
     
        public synchronized final Connection getConnection() {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

}