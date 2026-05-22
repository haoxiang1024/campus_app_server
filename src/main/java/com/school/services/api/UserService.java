package com.school.services.api;

import com.school.entity.User;
import com.school.utils.ServerResponse;



public interface UserService {

    ServerResponse register(String phone,String email,String password,int role);
    

    ServerResponse login(String phone,String pwd);
    

    ServerResponse resetPwd(String phone,String newPwd);
    

    ServerResponse updatePhoto(String photo,int id);
    

    ServerResponse updateAc(String nickname,String sex,int id);


    ServerResponse updateUserInfo(User user);
    

    User getUserById(Integer id);
    

    ServerResponse updateUserStatus(String ids, Integer status);
    


    ServerResponse getIMUserToken(int uid, String nickname);


    ServerResponse updatePhone(int id, String newPhone, String code);


    ServerResponse updateEmail(int id, String newEmail, String code);


    ServerResponse deleteAccount(int id);
}
