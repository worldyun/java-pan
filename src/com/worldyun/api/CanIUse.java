package com.worldyun.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.worldyun.vo.User;


@WebServlet(
    name = "CanIUse",
    urlPatterns = {"/caniuse"}
)
public class CanIUse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject res = new JSONObject();
        res.put("type", "uname");
        JSONObject resData = new JSONObject();
        String uname = req.getParameter("uname");
        resData.put("uname",uname);
        if (User.isInDb(uname)) {
            resData.put("caniuse", false);
        }else{
            resData.put("caniuse", true);
        }
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