package com.worldyun.vo;

import com.alibaba.fastjson.*;
import com.worldyun.Db.Db;

/**
 * File
 */
public class File {

    private int fid;
    private String fname = "";
    private String type = "";
    private String md5 = "";
    private int size = -1;
    private int did = -1;

    public static File getFileByFid(int fid) {
        File file = null;
        JSONArray data = Db.query("SELECT * FROM files WHERE fid=" + fid);
        if (!data.isEmpty()) {
            file = new File();
            JSONObject onedata = (JSONObject) data.get(0);
            file.setFid(fid);
            file.setFname(onedata.getString("fname"));
            file.setType(onedata.getString("type"));
            file.setMd5(onedata.getString("md5"));
            file.setSize(Integer.parseInt(onedata.getString("size")));
            file.setFid(Integer.parseInt(onedata.getString("did")));
        }
        return file;
    }
    
    public static File getFileByMd5(String md5) {
        File file = null;
        JSONArray data = Db.query("SELECT * FROM files WHERE md5='" + md5 + "'");
        if (!data.isEmpty()) {
            file = new File();
            JSONObject onedata = (JSONObject) data.get(0);
            file.setFid(Integer.parseInt(onedata.getString("fid")));
            file.setFname(onedata.getString("fname"));
            file.setType(onedata.getString("type"));
            file.setMd5(onedata.getString("md5"));
            file.setSize(Integer.parseInt(onedata.getString("size")));
            file.setFid(Integer.parseInt(onedata.getString("did")));
        }
        return file;
    }

    public static Boolean isInDb(String md5) {
        String sql = "SELECT * FROM files WHERE md5='" + md5 + "'";
        JSONArray data = Db.query(sql);
        if(!data.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void insertToDb() {
        String sql = "";
        if(!this.fname.isEmpty() && !this.md5.isEmpty() && (this.size > -1) && (this.did > -1) && this.type.length() > 0){
            sql = "INSERT INTO files(fname,type,md5,size,did) VALUES('"+ this.fname +"','"+ this.type +"','"+ this.md5 +"',"+ this.size +","+ this.did +")";
        }else if (!this.fname.isEmpty() && !this.md5.isEmpty() && (this.size > -1) && (this.did > -1)) {
            sql = "INSERT INTO files(fname,md5,size,did) VALUES('"+ this.fname  +"','"+ this.md5 +"',"+ this.size +","+ this.did +")";
        }
        Db.query(sql);
    }

    public static JSONArray getFilesByDid(int did) {
        JSONArray files = null;
        String sql = "SELECT fid,fname,type FROM files WHERE did=" + did + " ORDER BY fname";
        JSONArray data = Db.query(sql);
        if(!data.isEmpty()){
            files = data;
        }
        return files;
    }


    public static void delByFid(int fid) {
        String sql = "DELETE FROM files WHERE fid=" + fid;
        Db.query(sql);
    }

    /**
     * @param fid the fid to set
     */
    public void setFid(int fid) {
        this.fid = fid;
    }
    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @param md5 the md5 to set
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    /**
     * @param did the did to set
     */
    public void setDid(int did) {
        this.did = did;
    }
    /**
     * @return the fid
     */
    public int getFid() {
        return fid;
    }
    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @return the md5
     */
    public String getMd5() {
        return md5;
    }
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    /**
     * @return the did
     */
    public int getDid() {
        return did;
    }

}