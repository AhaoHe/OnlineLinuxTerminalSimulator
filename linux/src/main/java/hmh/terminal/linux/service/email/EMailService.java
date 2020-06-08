package hmh.terminal.linux.service.email;

import hmh.terminal.linux.result.JsonResult;
import hmh.terminal.linux.result.ResultFactory;
import hmh.terminal.linux.utils.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author heminghao
 * @version 0.1 beta
 * @date 2020/5/11 23:14
 */
@Service
public class EMailService {
    @Autowired
    JavaMailSenderImpl mailSender;

    /**
     * 从配置文件中获取发件人
     */
    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 验证码
     */
    private String code;
    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 邮件发送
     * @param receiver 收件人
     * @throws MailSendException 邮件发送错误
     */
    @Async
    public void sendEmailVerCode(String receiver) throws MailSendException {
        SimpleMailMessage message = new SimpleMailMessage();
        code = VerCodeGenerateUtil.generateVerCode();
        sendTime = new Date();
        message.setSubject("验证码");	//设置邮件标题
        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");	//设置邮件正文
        message.setTo(receiver);	//设置收件人
        message.setFrom(sender);	//设置发件人
        mailSender.send(message);	//发送邮件
    }

    public JsonResult validateMailVerCode(String verCode) throws Exception{
        Date date = new Date();
        //判断验证码
        if (VerCodeGenerateUtil.getMinute(sendTime, date) > 5) {
            throw new Exception("验证码已经失效！！！");
            //return ResultFactory.FailResult("验证码已经失效！！！");
        }
        if (!verCode.equals(code)) {
            throw new Exception("验证码不正确！！！");
            //return ResultFactory.FailResult("验证码不正确！！！");
        }
        code = null;
        return ResultFactory.SuccessResult("成功");
    }
}
