package com.worldyun.vo;

import com.alibaba.fastjson.*;
import com.worldyun.Db.Db;
/**
 * User
 */
public class User {

    private int uid;
    private String uname = "";
    private String passwd = "";

    public static User getUserByUid(int uid) {
        User res = null;
        JSONArray data = Db.query("SELECT * FROM users WHERE uid=" + uid);
        if(!data.isEmpty()){
            res = new User();
            JSONObject onedata = (JSONObject) data.get(0);
            res.setUid(uid);
            res.setUname(onedata.getString("uname"));
            res.setPasswd(onedata.getString("passwd"));
        }
        return res;
    }

    public static User getUserByUname(String uname) {
        User res = null;
        String sql = "SELECT * FROM users WHERE uname='" + uname+ "'";
        JSONArray data = Db.query(sql);
        if(!data.isEmpty()){
            res = new User();
            JSONObject onedata = (JSONObject) data.get(0);
            res.setUname(uname);
            res.setUid(Integer.parseInt(onedata.getString("uid")));
            res.setPasswd(onedata.getString("passwd"));
        }
        return res;
    }


    public static Boolean isInDb(String uname){
        String sql = "SELECT * FROM users WHERE uname='" + uname + "'";
        JSONArray data = Db.query(sql);
        if(!data.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void insertToDb(){
        if((!this.uname.isEmpty()) && (!this.passwd.isEmpty()) ){
            if(!User.isInDb(this.uname)){
                String sql = "INSERT INTO users(uname,passwd) VALUES('"+ this.uname +"','"+ this.passwd +"')";
                Db.query(sql);
            }
        }
    }

    public int getUid() {
        return this.uid;
    }
    /**
     * @return the uname
     */
    public String getUname() {
        return uname;
    }
    /**
     * @return the passwd
     */
    public String getPasswd() {
        return passwd;
    }
    public void setUid(int uid){
        this.uid = uid;
    }
    /**
     * @param uname the uname to set
     */
    public void setUname(String uname) {
        this.uname = uname;
    }/**
     * @param passwd the passwd to set
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}