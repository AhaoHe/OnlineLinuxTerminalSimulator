package hmh.terminal.linux.config;

import hmh.terminal.linux.controller.WebSocketServer;
import hmh.terminal.linux.service.SSHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/2/4 13:51
 */
@Configuration
@EnableWebSocket
@ConditionalOnWebApplication
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public MySpringConfigurator mySpringConfigurator() {
        return new MySpringConfigurator();
    }

    @Autowired
    public void setSSHService(SSHService sshService){
        WebSocketServer.sshService = sshService;
    }

}
