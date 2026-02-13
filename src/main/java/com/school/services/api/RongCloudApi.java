package com.school.services.api;


import com.school.utils.FeignAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//IM的请求接口
@FeignClient(name = "RongCloudApi", url = "https://api.rong-api.com",configuration = FeignAuthInterceptor.class) // 绑定拦截器
public interface RongCloudApi {
    /**
     * 获取 Token (注册用户)
     * 对应：POST /user/getToken.json
     */
    @PostMapping(value = "/user/getToken.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String getToken(
            @RequestParam("userId") String userId,
            @RequestParam("name") String name,
            @RequestParam("portraitUri") String portraitUri
    );

}
