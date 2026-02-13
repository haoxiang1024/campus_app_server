package com.school.controller;

import com.school.services.api.RongCloudApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/im")
public class IMController {
    @Autowired
    private RongCloudApi imService;


}
