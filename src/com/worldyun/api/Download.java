package com.worldyun.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.worldyun.vo.File;

@WebServlet(
    name = "download",
    urlPatterns = {"/download"}
)
/**
 * Download
 */
public class Download extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int fid = Integer.parseInt(req.getParameter("fid"));
        File file = File.getFileByFid(fid);
        String filename = file.getFname();
        if (file.getType() != null) {
            filename += ".";
            filename += file.getType();
        }
        filename = URLEncoder.encode(filename, "UTF-8");
        String path = this.getServletContext().getRealPath("/WEB-INF/upload/" + file.getMd5());
        FileInputStream fis = new FileInputStream(path);
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-Disposition", "attachment; filename="+filename);
        ServletOutputStream out = resp.getOutputStream();
        byte[] bt = new byte[1024];
        int length = 0;
        while((length=fis.read(bt))!=-1){
            out.write(bt,0,length);
        }
        out.close();

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("plz use GET");
    }
}