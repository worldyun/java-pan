package com.worldyun.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.*;
import com.worldyun.vo.Dir;
import com.worldyun.vo.File;
import com.worldyun.vo.User;

@WebServlet(
    name = "getlist",
    urlPatterns = {"/getlist"}
)
public class GetList extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject res = new JSONObject();
        JSONObject resData = new JSONObject();
        JSONArray dirlist = null;
        JSONArray fileslist = null;
        int did = Integer.parseInt(req.getParameter("did"));
        User user = (User) req.getSession().getAttribute("user");
        if (did < 1) {
            Dir dir = Dir.getRootDirByUid(user.getUid());
            did = dir.getDid();
        }
        dirlist = Dir.getChildDir(did);
        fileslist = File.getFilesByDid(did);
        if (dirlist != null) {
            resData.put("dirlist", dirlist);
        }else{
            resData.put("dirlist", "");
        }
        if (fileslist != null) {
            resData.put("fileslist", fileslist);
        }else{
            resData.put("fileslist", "");
        }
        resData.put("code", 2230);
        res.put("getlist", true);
        res.put("data", resData);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getOutputStream().write(res.toJSONString().getBytes("UTF-8"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("plz use GET");
    }
}