package com.school.controller;

import com.school.entity.EmailFormData;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * 邮件发送控制器类
 * 处理邮件发送相关的请求，通过QQ邮箱SMTP服务发送邮件
 */
@Controller
public class SendMailController {
    private static String SMTP_PASSWORD; // QQ邮箱授权码
    
    /**
     * 设置SMTP密码
     * @param password 从配置文件注入的QQ邮箱授权码
     */
    @Value("${mail.smtp.password}")
    public void setSmtpPassword(String password) {
        // 将注入的值赋给本类的静态变量
        SendMailController.SMTP_PASSWORD = password;
    }
    
    /**
     * 发送邮件接口
     * @param formData 邮件表单数据，包含主题和消息内容
     * @param response HTTP响应对象，用于返回HTML页面
     */
    @SneakyThrows
    @ResponseBody
    @RequestMapping("/sendMail")
    public void sendMail(@ModelAttribute EmailFormData formData, HttpServletResponse response) {
        send(formData); // 发送邮件

        // 设置响应头，避免乱码，且保证流关闭
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><meta charset='UTF-8'><title>提示</title></head>");
            out.println("<body>");
            out.println("<script type='text/javascript'>");
            out.println("alert('发送成功!');window.history.back();");
            out.println("</script>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
        }
    }

    /**
     * 执行邮件发送逻辑
     * @param formData 邮件表单数据，包含邮件主题和内容
     */
    @SneakyThrows
    private void send(EmailFormData formData) {
        // 邮件配置（QQ邮箱SMTP）
        String host = "smtp.qq.com";
        int port = 587;
        String username = "3502777299@qq.com";
        String to = "3502777299@qq.com";
        String subject = formData.getSubject();
        String text = formData.getMessage();

        // 初始化邮件属性
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true"); // 开启认证
        props.put("mail.smtp.starttls.enable", "true"); // 开启TLS加密（适配QQ邮箱）
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 强制TLS 1.2，避免JDK 17加密协议兼容问题
        props.put("mail.smtp.ssl.trust", host); // 信任SMTP服务器
        props.put("mail.debug", "false"); // 关闭调试（生产环境建议false）

        // 创建认证器
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, SMTP_PASSWORD);
            }
        };

        // 获取邮件会话
        Session session = Session.getInstance(props, authenticator);

        // 构建邮件消息
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username)); // 发件人
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // 收件人
        message.setSubject(subject, "UTF-8"); // 主题指定UTF-8，避免乱码
        message.setText(text, "UTF-8"); // 内容指定UTF-8

        // 发送邮件
        Transport.send(message);
    }
}