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


@Controller
public class SendMailController {
    private static String SMTP_PASSWORD;
    

    @Value("${mail.smtp.password}")
    public void setSmtpPassword(String password) {
        SendMailController.SMTP_PASSWORD = password;
    }
    

    @SneakyThrows
    @ResponseBody
    @RequestMapping("/sendMail")
    public void sendMail(@ModelAttribute EmailFormData formData, HttpServletResponse response) {
        send(formData);


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


    @SneakyThrows
    private void send(EmailFormData formData) {

        String host = "smtp.qq.com";
        int port = 587;
        String username = "3502777299@qq.com";
        String to = "3502777299@qq.com";
        String subject = formData.getSubject();
        String text = formData.getMessage();


        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.debug", "false");


        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, SMTP_PASSWORD);
            }
        };


        Session session = Session.getInstance(props, authenticator);


        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject, "UTF-8");
        message.setText(text, "UTF-8");


        Transport.send(message);
    }
}