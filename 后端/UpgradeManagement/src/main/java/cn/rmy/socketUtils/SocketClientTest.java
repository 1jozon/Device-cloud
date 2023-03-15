package cn.rmy.socketUtils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketClientTest {

    private FileOutputStream fos;

    private BufferedInputStream dis;

    public void test(int num,String s) throws IOException {
        Socket socket = new Socket("127.0.0.1", 5208);
        System.out.println(num+"号客户端连接成功");
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println(s);
        pw.flush();

        String filePath = "C:\\Users\\Administrator\\Desktop\\";

        dis = new BufferedInputStream(socket.getInputStream());
        DataInputStream ddis = new DataInputStream(socket.getInputStream());
        byte[] codes = new byte[3];
        String cd = ddis.readUTF();
        /*if(dis.read(codes,0,3)!=-1){
            cd = new String(codes);
        }
        else{
            log.info("传输错误");
        }*/

        File file = new File(filePath+s);
        fos = new FileOutputStream(file);
        //BufferedOutputStream bos = new BufferedOutputStream(fos);

        if(cd.equals("500")){
            log.info("文件不存在");
        }
        else if(cd.equals("200")){
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=dis.read(bytes,0,bytes.length))!=-1){
                fos.write(bytes,0,len);
                fos.flush();
            }
            socket.shutdownInput();
            log.info("===================文件接收成功 [File Name: "+s+"]===================");
        }
        else if(cd.equals("300")){
            log.info("未查询到授权记录或未被授权");
        }

        fos.close();
        dis.close();
        socket.close();
    }
}
