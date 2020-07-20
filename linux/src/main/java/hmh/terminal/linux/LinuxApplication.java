package hmh.terminal.linux;

import hmh.terminal.linux.config.WebSocketConfig;
import hmh.terminal.linux.controller.WebSocketServer;
import hmh.terminal.linux.service.SSHService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("hmh.terminal.linux.dao.mapper")
@EnableTransactionManagement //开启事务控制
public class LinuxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinuxApplication.class, args);
        //ConfigurableApplicationContext applicationContext = SpringApplication.run(LinuxApplication.class, args);
        //WebSocketServer.setApplicationContext(applicationContext);
    }

}
