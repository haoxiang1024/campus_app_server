package com.school.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

//签名实现
public class FeignAuthInterceptor implements RequestInterceptor {
    private  static String secret ;
    @Value("${im.app.secret}")
    public void setSecret(String secret){
        FeignAuthInterceptor.secret = secret;
    }
    private  static String key ;
    @Value("${im.app.key}")
    public void setKey(String key){
        FeignAuthInterceptor.key = key;
    }
    @Override
    public void apply(RequestTemplate template) {
        String nonce = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = DigestUtils.sha1Hex(secret + nonce + timestamp);
        template.header("App-Key", key);
        template.header("Nonce", nonce);
        template.header("Timestamp", timestamp);
        template.header("Signature", signature);
        template.header("Content-Type", "application/x-www-form-urlencoded");
    }
}
