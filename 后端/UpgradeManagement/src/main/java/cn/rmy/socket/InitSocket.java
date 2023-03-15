package cn.rmy.socket;


import cn.rmy.SpringUtils.SpringContexUtil;
import cn.rmy.controller.UpgradeController;
import cn.rmy.mqttUtils.MQTTConnect;
import cn.rmy.socketUtils.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InitSocket {

    private MQTTConnect mqttConnect = (MQTTConnect) SpringContexUtil.getBean("MQTTConnect");

    public void startSocketServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5208);
        System.out.println("服务器启动成功");

        while (true) {
            Socket socket= serverSocket.accept();
            System.out.println("上线通知： " + socket.getInetAddress() + ":" +socket.getPort());
            new Thread(new ServerThread(socket, mqttConnect.filePath)).start();
        }
    }
}
