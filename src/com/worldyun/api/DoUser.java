package com.worldyun.api;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.worldyun.vo.Dir;
import com.worldyun.vo.User;




@WebServlet(
    name = "user",
    urlPatterns = {"/user"}
)
public class DoUser extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("plz use POST");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String body = ReadAsChars(req);
        JSONObject rec = JSONObject.parseObject(body);
        JSONObject res = null;
        switch (rec.getString("type")) {
            case "login":
                res = doLogin(rec, req, resp);
                break;
            case "regist":
                res = doRegist(rec);
                break;
            case "logout":
                res = doLogout(req, resp);
            default:
                break;
        }
        resp.setCharacterEncoding("UTF-8");
        resp.getOutputStream().write(res.toJSONString().getBytes("UTF-8"));

    }


    public JSONObject doLogin(JSONObject rec, HttpServletRequest req, HttpServletResponse resp) {
        JSONObject res = new JSONObject();
        JSONObject recData = rec.getJSONObject("data");
        JSONObject resData = new JSONObject();
        User user = User.getUserByUname(recData.getString("uname"));
        if (user == null) {
            resData.put("uname",recData.getString("uname"));
            resData.put("code", 2132);
            res.put("login", false);
            res.put("data", resData);
        }else{
            resData.put("uname",user.getUname());
            if (recData.getString("passwd").equals(user.getPasswd())) {
                resData.put("code", 2130);
                res.put("login", true);
                HttpSession session=req.getSession();
                session.setMaxInactiveInterval(600);
                session.setAttribute("user", user);
            }else{
                resData.put("code", 2131);
                res.put("login", false);
            }
            res.put("data", resData);
        }
        
        return res;
    }

    public JSONObject doRegist(JSONObject rec) {
        JSONObject recData = rec.getJSONObject("data");
        JSONObject res = new JSONObject();
        JSONObject resData = new JSONObject();
        resData.put("uname", recData.getString("uname"));
        if(User.isInDb(recData.getString("uname")) || recData.getString("uname").isEmpty() || recData.getString("passwd").isEmpty()){
            resData.put("code", 2121);
            res.put("regist", false);
        }else{
            User user = new User();
            user.setUname(recData.getString("uname"));
            user.setPasswd(recData.getString("passwd"));
            user.insertToDb();
            User userb = User.getUserByUname(recData.getString("uname"));
            Dir dir = new Dir();
            dir.setUid(userb.getUid());
            dir.setDname("root");
            dir.insertToDb();
            resData.put("code", 2120);
            res.put("regist", true);
        }
        res.put("data", resData);
        return res;
    }

    public JSONObject doLogout(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject res = new JSONObject();
        HttpSession session=req.getSession();
        session.invalidate();
        return res;
    }



    public static String ReadAsChars(HttpServletRequest request)
    {
 
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
