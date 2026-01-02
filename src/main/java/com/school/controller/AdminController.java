package com.school.controller;

import com.school.admin.entity.User;
import com.school.admin.response.ResponseDTO;
import com.school.utils.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/admin")
public class AdminController {
    @ResponseBody
    @RequestMapping("/login")
    public ResponseDTO login(User  user) {
        return ResponseDTO.successObj();
    }

}
