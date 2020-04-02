package com.worldyun.vo;

import com.alibaba.fastjson.*;
import com.worldyun.Db.Db;

/**
 * Dir
 */
public class Dir {

    private int did;
    private int uid = -1;
    private int pnode = -1;
    private String dname = "";





    public static Dir getRootDirByUid(int uid) {
        Dir dir = null;
        String sql = "SELECT * FROM dir WHERE uid=" + uid+ " AND pnode is null";
        JSONArray data = Db.query(sql);
        if (!data.isEmpty()) {
            dir = new Dir();
            JSONObject onedata = (JSONObject) data.get(0);
            dir.setDid(Integer.parseInt(onedata.getString("did")));
            dir.setUid(uid);
            dir.setPnode(-1);
            dir.setDname(onedata.getString("dname"));
        }
        return dir;
    }

    public static Dir getDirByDid(int did) {
        Dir dir = null;
        String sql = "SELECT * FROM dir WHERE did=" + did;
        JSONArray data = Db.query(sql);
        if (!data.isEmpty()) {
            dir = new Dir();
            JSONObject onedata = (JSONObject) data.get(0);
            dir.setDid(did);
            dir.setUid(Integer.parseInt(onedata.getString("uid")));
            dir.setPnode(Integer.parseInt(onedata.getString("pnode")));
            dir.setDname(onedata.getString("dname"));
        }
        return dir;
    }

    public static JSONArray getChildDir(int did) {
        JSONArray dirs = null;
        String sql = "SELECT did,dname FROM dir WHERE pnode=" + did + " ORDER BY dname";
        JSONArray data = Db.query(sql);
        if(!data.isEmpty()){
            dirs = data;
        }
        return dirs;
    }

    public static void delByDid(int did) {
        String sql = "DELETE FROM dir WHERE did=" + did;
        Db.query(sql);
    }

    public void insertToDb() {
        if (this.uid > -1 && !this.dname.isEmpty()) {
            String sql = "";
            if (this.pnode < 1) {
                sql = "INSERT INTO dir(uid,dname) VALUES("+ this.uid + ",'" + this.dname +"')";
            }else{
                sql = "INSERT INTO dir(uid,pnode,dname) VALUES("+ this.uid +","+ this.pnode + ",'" + this.dname +"')";
            }
            
            Db.query(sql);
        }
    }

    


    /**
     * @param did the did to set
     */
    public void setDid(int did) {
        this.did = did;
    }
    /**
     * @param uid the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }
    /**
     * @param pnode the pnode to set
     */
    public void setPnode(int pnode) {
        this.pnode = pnode;
    }
    /**
     * @param dname the dname to set
     */
    public void setDname(String dname) {
        this.dname = dname;
    }
    /**
     * @return the did
     */
    public int getDid() {
        return did;
    }
    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }
    /**
     * @return the pnode
     */
    public int getPnode() {
        return pnode;
    }
    /**
     * @return the dname
     */
    public String getDname() {
        return dname;
    }
}