package com.school.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
@Service
public class EmailVerificationUtils {

    private static final String SMTP_HOST = "smtp.qq.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USERNAME = "3502777299@qq.com";
    private static String SMTP_PASSWORD ;
    @Value("${mail.smtp.password}")
    public void setSmtpPassword(String password) {

        EmailVerificationUtils.SMTP_PASSWORD = password;
    }

    private static final Map<String, VerificationCode> CODE_STORE = new ConcurrentHashMap<>();

    private static final long CODE_EXPIRE_MINUTES = 5;


    private static class VerificationCode {
        private final String code;
        private final long expireTime;

        public VerificationCode(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }

        public String getCode() {
            return code;
        }

        public long getExpireTime() {
            return expireTime;
        }


        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }


    public static String generateVerificationCode() {
        Random random = new Random();

        int codeNum = 100000 + random.nextInt(900000);
        return String.valueOf(codeNum);
    }


    public static String sendVerificationCodeEmail(String toEmail) throws MessagingException {

        String verificationCode = generateVerificationCode();


        long expireTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(CODE_EXPIRE_MINUTES);


        CODE_STORE.put(toEmail, new VerificationCode(verificationCode, expireTime));


        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.debug", "false");


        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        };


        Session session = Session.getInstance(props, authenticator);


        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));


        message.setSubject("【验证码】您的邮箱验证请求", "UTF-8");


        String emailContent = String.format(
                "<div style='font-family: Arial, sans-serif;'>" +
                        "<h3>您好！</h3>" +
                        "<p>您正在进行邮箱验证，本次验证码为：<strong style='color: #0066cc; font-size: 18px;'>%s</strong></p>" +
                        "<p>验证码有效期为%d分钟，请及时使用。</p>" +
                        "<p>如非本人操作，请忽略此邮件。</p>" +
                        "</div>",
                verificationCode, CODE_EXPIRE_MINUTES
        );
        message.setContent(emailContent, "text/html; charset=UTF-8");


        Transport.send(message);

        return verificationCode;
    }


    public static boolean verifyCode(String email, String inputCode) {

        if (!CODE_STORE.containsKey(email)) {
            return false;
        }

        VerificationCode storedCode = CODE_STORE.get(email);


        if (storedCode.isExpired()) {
            CODE_STORE.remove(email);
            return false;
        }


        boolean isMatch = storedCode.getCode().equals(inputCode);


        if (isMatch) {
            CODE_STORE.remove(email);
        }

        return isMatch;
    }


    public static void cleanExpiredCodes() {
        Iterator<Map.Entry<String, VerificationCode>> iterator = CODE_STORE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, VerificationCode> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
            }
        }
    }
}
