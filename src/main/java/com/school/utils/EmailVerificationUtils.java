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
    // SMTP配置（QQ邮箱）
    private static final String SMTP_HOST = "smtp.qq.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USERNAME = "3502777299@qq.com";
    private static String SMTP_PASSWORD ; // QQ邮箱授权码
    @Value("${mail.smtp.password}")
    public void setSmtpPassword(String password) {
        // 将注入的值赋给本类的静态变量
        EmailVerificationUtils.SMTP_PASSWORD = password;
    }
    // 验证码存储（线程安全）：key=邮箱，value=验证码+过期时间
    private static final Map<String, VerificationCode> CODE_STORE = new ConcurrentHashMap<>();
    // 验证码有效期（默认5分钟）
    private static final long CODE_EXPIRE_MINUTES = 5;

    /**
     * 验证码实体类
     */
    private static class VerificationCode {
        private final String code;
        private final long expireTime; // 过期时间戳（毫秒）

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

        // 判断是否过期
        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * 生成6位随机数字验证码
     * @return 6位验证码字符串
     */
    public static String generateVerificationCode() {
        Random random = new Random();
        // 生成100000~999999之间的随机数
        int codeNum = 100000 + random.nextInt(900000);
        return String.valueOf(codeNum);
    }

    /**
     * 发送验证码邮件到指定邮箱
     * @param toEmail 用户输入的邮箱地址
     * @return 生成的验证码（用于存储验证）
     * @throws MessagingException 邮件发送异常
     */
    public static String sendVerificationCodeEmail(String toEmail) throws MessagingException {
        // 1. 生成验证码
        String verificationCode = generateVerificationCode();

        // 2. 计算过期时间（当前时间 + 5分钟）
        long expireTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(CODE_EXPIRE_MINUTES);

        // 3. 存储验证码
        CODE_STORE.put(toEmail, new VerificationCode(verificationCode, expireTime));

        // 4. 初始化邮件配置
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.debug", "false");

        // 5. 创建认证器
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        };

        // 6. 获取Session
        Session session = Session.getInstance(props, authenticator);

        // 7. 构建邮件消息
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USERNAME)); // 发件人
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // 收件人

        // 邮件主题
        message.setSubject("【验证码】您的邮箱验证请求", "UTF-8");

        // 邮件内容（HTML格式，更友好）
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

        // 8. 发送邮件
        Transport.send(message);

        return verificationCode;
    }

    /**
     * 验证用户提交的邮箱和验证码是否有效
     * @param email 用户提交的邮箱
     * @param inputCode 用户提交的验证码
     * @return 验证结果：true=有效，false=无效（过期/错误）
     */
    public static boolean verifyCode(String email, String inputCode) {
        // 1. 检查邮箱是否存在
        if (!CODE_STORE.containsKey(email)) {
            return false;
        }

        VerificationCode storedCode = CODE_STORE.get(email);

        // 2. 检查是否过期
        if (storedCode.isExpired()) {
            CODE_STORE.remove(email); // 移除过期验证码
            return false;
        }

        // 3. 校验验证码是否匹配
        boolean isMatch = storedCode.getCode().equals(inputCode);

        // 4. 验证成功后移除验证码（防止重复使用）
        if (isMatch) {
            CODE_STORE.remove(email);
        }

        return isMatch;
    }

    /**
     * 清理过期的验证码（建议定时执行）
     */
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
