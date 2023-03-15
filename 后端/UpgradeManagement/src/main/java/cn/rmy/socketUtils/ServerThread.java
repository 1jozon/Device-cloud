package cn.rmy.socketUtils;


import cn.rmy.SpringUtils.SpringContexUtil;
import cn.rmy.beans.UpgradePermission;
import cn.rmy.mqttUtils.MQTTConnect;
import cn.rmy.service.Impl.UpgradePermissionServiceImpl;
import cn.rmy.service.UpgradePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ServerThread implements Runnable{

    public Socket socket;

    private String filePath;


    private UpgradePermissionService upgradePermissionServiceImpl = (UpgradePermissionService) SpringContexUtil.getApplicationContext().getBean(UpgradePermissionService.class);
   // private MQTTConnect mqttConnect = (MQTTConnect) SpringContexUtil.getBean("MQTTConnect");


    public ServerThread(Socket socket,String filePath){
        this.socket = socket;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {

            //BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream ism = socket.getInputStream();
            byte[] bt = new byte[1024];
            //int len=bf.read(bt);
            int len = ism.read(bt);
            log.info("读取到的流长度为"+len);
            //System.out.println("读取到的流长度为"+len);
            byte[] temp = new byte[len-2];
            for(int i=0;i<len-2;i++) temp[i] = bt[i];
            String data = new String(temp,0,len-2,"utf8");
            String[] ss = data.split(" ");//第一个是仪器id,第二个是包名
            log.info("获取数据成功： "+data);
            //System.out.println(ss[0]);
            //System.out.println(ss[1]);
            String code="200";
            //System.out.println(mqttConnect.filePath);
            UpgradePermission upp = upgradePermissionServiceImpl.getByCondition(ss[1],ss[0]);
            System.out.println(upp.getAllowed());
            if(upp==null) code="300";
            if(upp!=null&&upp.getAllowed()!=1) code="300";

            log.info("校验（测试）");

            sendFile(ss[1],code);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String fileName,String code) throws Exception {
        System.out.println(filePath+fileName);
        File file = new File(filePath+fileName);
        //PrintWriter pw = new PrintWriter(socket.getOutputStream());
        String s = code;
        if(!file.exists()) s="500";
        //pw.println(s);
        //pw.flush();
        DataOutputStream ddos = new DataOutputStream(socket.getOutputStream());
        ddos.writeUTF(s);
        ddos.flush();
        if(code.equals("300")){
            ddos.close();
            //socket.close();
            log.info("未查询到授权记录或未授权更新");
            return;
        }

        if(file.exists()) {
            FileInputStream fis = null;

            fis = new FileInputStream(file);

            BufferedOutputStream dos = new BufferedOutputStream(socket.getOutputStream());

            //文件名和长度
            /*
            dos.writeUTF(fileName);
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();*/

            BufferedInputStream bis = new BufferedInputStream(fis);

            System.out.println("==========开始传输文件==========");
            byte[] bytes = new byte[1024];
            int len = 0;
            long progress = 0;
            while ((len = bis.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
                progress += len;
                System.out.println("| " + (100 * progress / file.length()) + "% | ");
            }
            dos.flush();
            socket.shutdownOutput();
            System.out.println("==========文件传输成功==========");
            fis.close();
            dos.close();
            bis.close();
        }
        else{
            log.info("文件不存在");
        }

    }


}
