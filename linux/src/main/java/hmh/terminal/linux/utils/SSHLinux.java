package hmh.terminal.linux.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import hmh.terminal.linux.controller.WebSocketServer;
import hmh.terminal.linux.service.SSHService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/1/7 19:01
 */
@Slf4j
public class SSHLinux {
    public String state="success";
    private int flag = 0;

    private String username = "";
    private String hostname = "";
    private int port = 22;
    private String ssh_username= "";
    private String password = "";

    private Connection connection;
    private Session session;
    private BufferedReader stdout;
    private BufferedReader stderr;
    public PrintWriter pw; //输入命令


    //线程池
    private ExecutorService service = Executors.newFixedThreadPool(3);

    public SSHLinux()
    {
        log.debug("创建类需要参数uid、主机ip、端口、账户、密码");
        System.out.println("创建类需要参数uid、主机ip、端口、账户、密码");
    }
    //检测是否可以登录
    public SSHLinux(String hostname,int port,String password)
    {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    /*public SSHLinux(Map<String,String> map*//*String hostname, int port, String ssh_username, String password*//*)
    {
        this.username = map.get("username");
        this.hostname = map.get("hostname");
        this.port = Integer.parseInt(map.get("port"));
        this.ssh_username = map.get("server_username");
        this.password = map.get("loginKey");
        String status = map.get("status");

        long t1 = System.currentTimeMillis();
        if ("0".equals(status)){
            loginByPassword();
        }else if("1".equals(status)){
            loginByCharsKey(password.toCharArray(),null);
        }else{
            loginByFileKey(new File(password),null);
        }
        long t2 = System.currentTimeMillis();
        log.info("username="+username+":远程登陆linux，连接耗时："+(t2-t1)/1000.0+"s");
    }*/

    public SSHLinux(Map<String,Object> map/*String hostname, int port, String ssh_username, String password*/)
    {
        this.username = (String)map.get("username");
        this.hostname = (String)map.get("hostname");
        this.port = (Integer) map.get("port");
        this.ssh_username = (String)map.get("server_username");
        this.password = (String)map.get("loginKey");
        String status = (String)map.get("status");

        long t1 = System.currentTimeMillis();
        if ("0".equals(status)){
            loginByPassword();
        }else if("1".equals(status)){
            loginByCharsKey(password.toCharArray(),null);
        }else{
            loginByFileKey(new File(password),null);
        }
        long t2 = System.currentTimeMillis();
        log.info("username="+username+":远程登陆linux，连接耗时："+(t2-t1)/1000.0+"s");
    }

    //初始化连接并建立虚拟终端
    //通过密码登录
    private void loginByPassword(){
        try{
            //根据主机名先获取一个远程连接
            connection = new Connection(hostname,port);
            connection.connect();
            //认证账户密码
            boolean isAuthenticated= connection.authenticateWithPassword("root",password);
            if(!isAuthenticated){
                //connection.close();
                throw new RuntimeException(username+"Authentication failed.验证失败");
            }
            session = connection.openSession();
            //session.requestDumbPTY();
            session.requestPTY("xterm", 90, 30, 0, 0, null);//建立虚拟终端
            session.startShell();//打开一个shell

            stdout = new BufferedReader(new InputStreamReader(
                    new StreamGobbler(session.getStdout()),StandardCharsets.UTF_8
            ));
            stderr = new BufferedReader(new InputStreamReader(
                    new StreamGobbler(session.getStderr()),StandardCharsets.UTF_8
            ));

            pw = new PrintWriter(session.getStdin(),true);// 准备输入命令
            //loginUser();
            this.state="success";
        }catch (IOException e){
            this.state = "fail";
            log.error("username="+username+"：连接超时，连接错误(IO)");
        }
    }

    /**
     *
     * @Title: loginByCharsKey
     * @Description: 秘钥方式  远程登录linux服务器
     *
     * @param keys  一个字符[]，其中包含用户的DSA或RSA私钥(OpenSSH密匙格式，您不能丢失“----- begin DSA私钥-----”或“-----BEGIN RSA PRIVATE KEY-----“标签。char数组可以包含换行符/换行符。
     * @param keyPass 如果秘钥字符数组加密  需要用该字段解密  否则不需要可以为null
     * @return Boolean
     * @throws
     */
    private void loginByCharsKey(char[] keys, String keyPass) {

        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            connection = new Connection(hostname,port);
            connection.connect();
            // 登录认证
            boolean flag = connection.authenticateWithPublicKey("root", keys, keyPass);
            if (flag) {
                log.info(username+"认证成功！");
            } else {
                log.info(username+"认证失败！");
                //connection.close();
                throw new RuntimeException(username+"Authentication failed.验证失败");
            }
            session = connection.openSession();
            //session.requestDumbPTY();
            session.requestPTY("xterm", 90, 30, 0, 0, null);//建立虚拟终端
            session.startShell();//打开一个shell

            stdout = new BufferedReader(new InputStreamReader(
                    new StreamGobbler(session.getStdout()),StandardCharsets.UTF_8
            ));
            stderr = new BufferedReader(new InputStreamReader(
                    new StreamGobbler(session.getStderr()),StandardCharsets.UTF_8
            ));

            pw = new PrintWriter(session.getStdin(),true);// 准备输入命令
            //loginUser();
            this.state="success";
        } catch (Exception e) {
            this.state = "fail";
            log.error("username="+username+"：连接超时，连接错误(IO)");
        }
    }

    /**
     *
     * @Title: loginByKey
     * @Description: 秘钥方式  远程登录linux服务器
     * @param keyFile  一个文件对象指向一个文件，该文件包含OpenSSH密钥格式的用户的DSA或RSA私钥(PEM，不能丢失"-----BEGIN DSA PRIVATE KEY-----" or "-----BEGIN RSA PRIVATE KEY-----"标签
     * @param keyfilePass 如果秘钥文件加密 需要用该参数解密，如果没有加密可以为null
     * @return Boolean
     * @throws
     */
    private void loginByFileKey(File keyFile, String keyfilePass) {

        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            connection = new Connection(hostname,port);
            connection.connect();
            // 登录认证
            boolean isAuthen = connection.authenticateWithPublicKey("root", keyFile, keyfilePass);
            if (!isAuthen) {
                log.info(username+"认证失败！");
                throw new RuntimeException(username+"Authentication failed.验证失败");
            }

            session = connection.openSession();
            //session.requestDumbPTY();//建立虚拟终端
            session.requestPTY("xterm", 95, 30, 0, 0, null);//建立虚拟终端
            session.startShell();//打开一个shell

            stdout = new BufferedReader(new InputStreamReader(
                    new StreamGobbler(session.getStdout()),StandardCharsets.UTF_8
            ));
            stderr = new BufferedReader(new InputStreamReader(
                    new StreamGobbler(session.getStderr()),StandardCharsets.UTF_8
            ));
            pw = new PrintWriter(session.getStdin(),true);// 准备输入命令
            //loginUser();
            this.state="success";
        } catch (Exception e) {
            this.state = "fail";
            log.error("username="+username+"：连接超时，连接错误(IO)");
        }
    }

    /*close SSH*/
    public void close()
    {
        try
        {
            stdout.close();
            stderr.close();
            pw.close();
            session.close();
            connection.close();
        }
        catch (Exception e)
        {
            log.error("username="+username+"：关闭ssh连接错误");
        }
    }

    public boolean enable(int status){
        try {
            connection = new Connection(hostname, port);
            connection.connect();
            // 登录认证
            boolean isAuthen=false;
            switch (status){
                case 0:
                    isAuthen = connection.authenticateWithPassword("root",password);
                    break;
                case 1:
                    isAuthen = connection.authenticateWithPublicKey("root", password.toCharArray(), null);
                    break;
                case 2:
                    isAuthen = connection.authenticateWithPublicKey("root", new File((password)), null);
                    break;
            }
            connection.close();
            return isAuthen;
        }catch (IOException e){
            log.error("验证服务错误："+e);
            return false;
        }finally {
            connection.close();
        }
    }


    public void loginUser(){
        pw.println("su - "+ssh_username);// 输入待执行命令
    }


    /*get commends*/
    public List<String> execute(String cmd){
        log.info("username="+username+"：发送命令 "+cmd);
        List<String> result = new ArrayList<>();
        try {
            if ("exit".equals(cmd)) {
                pw.println(cmd);//输入待执行命令
                close();
                result.add("用户退出，关闭连接!");
                return result;
            }
            pw.println(cmd);// 输入待执行命令

            while (true) {
                String line = stdout.readLine();
                if (line==null || line.trim().endsWith("#"))
                    break;

                System.out.println(line);
                result.add(line);
            }
        }catch (IOException e){
            log.error("username="+username+":连接已关闭或命令出错(IO)");
        }
        return result;
    }

    /*get commends :WebSoket版本*/
    public void execCommand(final WebSocketServer webSocketServer) throws IOException {
        //执行命令方法，使用线程池来执行
        service.submit(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    //持续获取服务器标准输出
                    while ((line = stdout.readLine()) != null) {
                        //通过对应的webSocket服务端将内容输出到前台
                        if (flag<5){
                            flag++;
                            System.out.println(line);
                            //webSocketServer.sendMessage(line);
                            //webSocketServer.sendMessage("[" + username + "@LinuxTerminal ?]$ ");
                        }else {
                            System.out.println(line);
                            if((line.contains("]#") && line.contains("su - "))||(line.contains("]$") && line.contains("su - "))){
                                line="登录"+ssh_username+"成功";
                            }
                            if (line.contains("]#") && line.contains(" "))
                                line = "[" + ssh_username + "@LinuxTerminal" + line.substring(line.indexOf(" "));
                            if (line.contains("]$") && line.contains(" "))
                                line = "[" + ssh_username + "@LinuxTerminal" + line.substring(line.indexOf(" "));
                            webSocketServer.sendMessage(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
