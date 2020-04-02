package com.worldyun.api;

// import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.codec.digest.DigestUtils;

import com.worldyun.vo.Dir;
import com.worldyun.vo.File;
import com.worldyun.vo.User;

@WebServlet(
    name = "upload",
    urlPatterns = {"/upload"}
)
public class Upload extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("plz use POST");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject res = new JSONObject();
        JSONObject resData = new JSONObject();
        int did = Integer.parseInt(req.getParameter("did"));
        String md5 = req.getParameter("md5");
        User user = (User) req.getSession().getAttribute("user");
        if (did < 1) {
            Dir dir = Dir.getRootDirByUid(user.getUid());
            did = dir.getDid();
        }
        
        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        createDir(savePath);
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
             //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8"); 
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(req)){
                //按照传统方式获取数据
                return;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(req);
            for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件

                    //得到上传的文件名称，
                    String filename = item.getName();
                
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                    String[] filenamedata = filename.split("\\.");
                    if (filenamedata.length > 2 ) {
                        String[] tempname = new String[2];
                        String first = filenamedata[0];
                        for (int i = 1; i < filenamedata.length -1 ; i++) {
                            first += ".";
                            first += filenamedata[i];
                        }
                        tempname[0] = first;
                        tempname[1] = filenamedata[filenamedata.length -1];
                        filenamedata = tempname;
                    }else if(filenamedata.length == 1){
                        String[] tempname = new String[2];
                        tempname[0] = filenamedata[0];
                        tempname[1] = "";
                        filenamedata = tempname;
                    }

                    if (File.isInDb(md5)) {                             //判断文件是否存在,如已存在则不用再次上传
                        File file = File.getFileByMd5(md5);
                        file.setDid(did);
                        file.setFname(filenamedata[0]);
                        file.setType(filenamedata[1]);
                        file.insertToDb();
                    }else{
                        //获取item中的上传文件的输入流
                        InputStream in = item.getInputStream();
                        //创建一个文件输出流
                        FileOutputStream out = new FileOutputStream(savePath + "\\" + md5);
                        //创建一个缓冲区
                        byte buffer[] = new byte[1024];
                        //判断输入流中的数据是否已经读完的标识
                        int len = 0;
                        int leg = 0;
                        //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                        while((len=in.read(buffer))>0){
                            //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                            leg += len;
                            out.write(buffer, 0, len);
                        }
                        //关闭输入流
                        in.close();
                        //关闭输出流
                        out.close();
                        //删除处理文件上传时生成的临时文件
                        item.delete();

                        File file = new File();
                        file.setMd5(md5);
                        file.setDid(did);
                        file.setFname(filenamedata[0]);
                        file.setSize(leg);
                        file.setType(filenamedata[1]);
                        file.insertToDb();
                    }

        
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        
        }
        resData.put("code", 2210);
        res.put("upload", true);
        res.put("data", resData);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getOutputStream().write(res.toJSONString().getBytes("UTF-8"));
    }




    public void createDir(String path){
        java.io.File dir=new java.io.File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
    }
}