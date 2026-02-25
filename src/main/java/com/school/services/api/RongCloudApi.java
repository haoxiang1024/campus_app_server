package com.school.services.api;


import com.school.utils.FeignAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 融云IM服务API接口
 * 通过Feign客户端调用融云服务接口
 */
@FeignClient(name = "RongCloudApi", url = "https://api.rong-api.com",configuration = FeignAuthInterceptor.class) // 绑定拦截器
public interface RongCloudApi {
    /**
     * 获取用户Token (注册用户到IM系统)
     * 对应融云API：POST /user/getToken.json
     * @param userId 用户ID
     * @param name 用户昵称
     * @param portraitUri 用户头像URL
     * @return String 返回包含token的JSON字符串
     */
    @PostMapping(value = "/user/getToken.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String getToken(
            @RequestParam("userId") String userId,
            @RequestParam("name") String name,
            @RequestParam("portraitUri") String portraitUri
    );

    /**
     * 刷新用户信息
     * 用于同步更新IM系统中的用户资料
     * @param userId 用户ID
     * @param name 用户昵称
     * @param portraitUri 用户头像URL
     * @return String 返回操作结果的JSON字符串
     */
    @PostMapping(value = "/user/refresh.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String refresh(
            @RequestParam("userId") String userId,
            @RequestParam("name") String name,
            @RequestParam("portraitUri") String portraitUri
    );

}
