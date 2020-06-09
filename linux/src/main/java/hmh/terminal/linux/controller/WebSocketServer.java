package hmh.terminal.linux.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import hmh.terminal.linux.service.SSHService;
import hmh.terminal.linux.utils.JwtTokenUtil;
import hmh.terminal.linux.utils.SSHLinux;
import hmh.terminal.linux.utils.WebsocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/2/2 16:09
 */
@ServerEndpoint(value = "/linuxterminal/{sid}/{token}")
@Slf4j
@Component
public class WebSocketServer {

    //lombok的注解已经帮忙实现了
    //static Logger logger= LoggerFactory.getLogger(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    public static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    public static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String username;
    /**接收userId*/
    private Integer sid;
    /**接收token*/
    private String token;
    private JwtTokenUtil jwtTokenUtil;
    /*ssh工具类*/
    private SSHLinux sshLinux = null;
    /*数据库调取连接的ip、端口等数据*/

    //private static ApplicationContext applicationContext;
    public static SSHService sshService;

    /*public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }*/
    /*@Autowired
    public void setSSHService(SSHService sshService){
        WebSocketServer.sshService = sshService;
    }*/
    @OnOpen
    public void OnOpen(Session session, @PathParam("token")String token
            , @PathParam("sid")Integer sid) throws Exception {
        this.jwtTokenUtil = new JwtTokenUtil();

        this.session = session;
        this.username = jwtTokenUtil.getUsernameFromToken(token);
        //this.username = username;
        this.sid = sid;
        this.token = token;

        if(username !=null) {

            if (webSocketMap.containsKey(username)) {
                //加入set中
                webSocketMap.remove(username);
                webSocketMap.put(username, this);
            } else {
                //加入set中
                webSocketMap.put(username, this);
                //在线数加1
                addOnlineCount();
            }
            log.info("用户" + username + "连接:,当前在线人数为:" + getOnlineCount());

            try {
                if (jwtTokenUtil.isTokenExpired(token)|| !sshService.checkToken(username,WebsocketUtil.getRemoteAddress(session).getHostName(),token)) {
                    sendMessage("认证失败!请重新登录!");
                    onClose();
                }else {

                    //SSHService sshService = (SSHService) applicationContext.getBean(SSHService.class);
                    Map<String, Object> us = sshService.getUserAndServer(username, sid);
                    if (us == null) {
                        try {
                            sendMessage("连接失败，缺少相关参数！");
                        } catch (IOException e) {
                            log.debug("连接失败，缺少相关参数！查找不到sever数据");
                        }
                    } else {
                        try {
                            log.info(WebsocketUtil.getRemoteAddress(session).getHostName());
                            sendMessage("连接中，请稍后！loading……提示：长期加载可能是服务器连接失败");
                        } catch (IOException e) {
                            log.error("用户:" + username + ",网络异常!!!!!!");
                        }
                        String serverMsg = us.get("hostname") + ":" + us.get("port");
                        log.info("用户尝试连接:" + username + ",服务器：" + serverMsg);
                        //this.sshLinux = new SSHLinux(username, us.get("hostname"), Integer.parseInt(us.get("port"))
                        //        , us.get("server_username"), us.get("loginKey"));
                        this.sshLinux = new SSHLinux(us);
                        this.sshLinux.loginUser();


                        try {
                            this.sshLinux.execCommand(this);
                            if ("success".equals(this.sshLinux.state)) {
                                sendMessage("连接成功");
                            } else {
                                sendMessage("连接超时，连接失败");
                            }
                        } catch (IOException e) {
                            log.error("用户:" + username + ",网络异常!!!!!!");
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            sendMessage("认证失败！请重新登录！");
            onClose();
        }



    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (username!=null){
            if(webSocketMap.containsKey(username)){
                webSocketMap.remove(username);
                //从set中删除
                subOnlineCount();
            }
            if(sshLinux!=null)
                sshLinux.close();
            log.info("用户退出:"+username+",当前在线人数为:" + getOnlineCount());
        }
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        //可以群发消息
        //消息保存到数据库、redis
        System.out.println(message);
        if(jwtTokenUtil.isTokenExpired(token)||!sshService.checkToken(username,WebsocketUtil.getRemoteAddress(session).getHostName(),token)){
            sendMessage("认证已经失败！可能认证过期或在其他地方登录！");
            onClose();
        }else {
            if(StringUtils.isNotBlank(message)){
                log.info("来自客户端的消息:" + message);
                //群发消息
                try {
                    if("key_enter".equals(message)){
                        this.sshLinux.pw.write( "\r");
                        this.sshLinux.pw.flush();
                    }else {
                        //通过工具类的标准输入网远程服务器中写内容
                        this.sshLinux.pw.write(message/*+ "\r\n"*/);
                        this.sshLinux.pw.flush();
                        //this.sshLinux.pw.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.username+",原因:"+error.getMessage());
        error.printStackTrace();
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("username") String username) throws IOException {
        log.info("发送消息到:"+username+"，报文:"+message);
        if(username!=null&&webSocketMap.containsKey(username)){
            webSocketMap.get(username).sendMessage(message);
        }else{
            log.info("用户"+username+",不在线！");
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public void setSessionNull(){
        this.session=null;
    }
}
