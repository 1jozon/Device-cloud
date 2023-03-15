package cn.rmy;

import cn.rmy.socket.InitSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@EnableTransactionManagement
public class UpgradeManagementApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(UpgradeManagementApplication.class);
        //InitSocket initSocket = new InitSocket();
        //initSocket.startSocketServer();

    }
}
