package com.school.services.api;


import com.school.utils.FeignAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "RongCloudApi", url = "https://api.rong-api.com",configuration = FeignAuthInterceptor.class) // 绑定拦截器
public interface RongCloudApi {

    @PostMapping(value = "/user/getToken.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String getToken(
            @RequestParam("userId") String userId,
            @RequestParam("name") String name,
            @RequestParam("portraitUri") String portraitUri
    );


    @PostMapping(value = "/user/refresh.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String refresh(
            @RequestParam("userId") String userId,
            @RequestParam("name") String name,
            @RequestParam("portraitUri") String portraitUri
    );


    @PostMapping(value = "/user/block.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String block(
            @RequestParam("userId") String userId,
            @RequestParam("minute") Number minute
    );
    @PostMapping(value = "/user/unblock.json",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String unblock(
            @RequestParam("userId") String userId);

}
