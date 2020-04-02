package com.worldyun.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.*;
import com.worldyun.vo.Dir;
import com.worldyun.vo.User;

@WebServlet(
    name = "newdir",
    urlPatterns = {"/newdir"}
)
public class NewDir extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject res = new JSONObject();
        JSONObject resData = new JSONObject();
        int pnode = Integer.parseInt(req.getParameter("pnode"));
        String dname = req.getParameter("dname");
        User user = (User) req.getSession().getAttribute("user");
        if (pnode < 1) {
            Dir dir = Dir.getRootDirByUid(user.getUid());
            pnode = dir.getDid();
        }
        Dir dir = new Dir();
        dir.setUid(user.getUid());
        dir.setPnode(pnode);
        dir.setDname(dname);
        dir.insertToDb();
        resData.put("code", 2240);
        res.put("newdir", true);
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