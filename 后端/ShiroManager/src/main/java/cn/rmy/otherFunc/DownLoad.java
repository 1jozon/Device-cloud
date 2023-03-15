package cn.rmy.otherFunc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class DownLoad {

    public static void downloadFile(String resource, String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{

        if (fileName != null){
            String realPath = "";

            File file = new File(resource,fileName);
            if (file.exists()){
                //配置文件下载
                response.setHeader("content-type","application/octet-stream");
                response.setContentType("application/octet-stream");
                //下载能显示中文
                response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode((fileName),"UTF-8"));

                //实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try{
                    fis =  new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1){
                        os.write(buffer,0,i);
                        i = bis.read(buffer);
                        //System.out.println("Download the file successfully");
                    }
                }catch (Exception e){
                    System.out.println("Download the file failed");
                }finally {
                    if (bis != null){
                        try{
                            bis.close();
                        }catch (IOException ie){
                            ie.printStackTrace();
                        }
                    }
                    if (fis != null){
                        try{
                            fis.close();
                        }catch (IOException ie){
                            ie.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
