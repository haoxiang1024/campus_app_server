package com.school.controller;

import com.school.services.api.SearchInfoService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchInfoController {
    @Autowired
    private SearchInfoService searchInfoService;
    @ResponseBody
    @RequestMapping("/searchInfo")
    public ServerResponse searchInfo(String value) {
        return searchInfoService.searchInfo(value);
    }
}
