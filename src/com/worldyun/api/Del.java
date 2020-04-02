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

@WebServlet(
    name = "del",
    urlPatterns = {"/del"}
)
public class Del extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject res = new JSONObject();
        JSONObject resData = new JSONObject();
        String type = req.getParameter("type");
        int id = Integer.parseInt(req.getParameter("id"));
        switch (type) {
            case "dir":
                Dir.delByDid(id);    
                break;
            case "file":
                File.delByFid(id);
                break;
            default:
                break;
        }
        resData.put("code", 2250);
        res.put("del", true);
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