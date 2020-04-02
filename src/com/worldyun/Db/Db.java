package com.worldyun.Db;

/**
 * Db
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;;

public class Db {
    private static DbConnPool dbpool = DbConnPool.getInstance();

    public static JSONArray query(String sql) {
        JSONArray res = new JSONArray();
        Connection conn = Db.dbpool.getConnection();
        if (conn == null) {
            return null;
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            if (sql.startsWith("INSERT") || sql.startsWith("UPDATE") || sql.startsWith("DELETE")) {
                res.add(st.execute(sql));
                return res;
            }
            rs = st.executeQuery(sql);
            if (rs.wasNull()) {
                return res;
            }
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            
            while (rs.next()) {
                JSONObject jsonobj = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    jsonobj.put(md.getColumnName(i), rs.getObject(i));
                }
                res.add(jsonobj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
