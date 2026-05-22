package com.school.services.api;

import com.school.utils.ServerResponse;


public interface AdminService {

    ServerResponse getAllUser();
    

    ServerResponse getAllLostFoundCount(String type);
    

    ServerResponse getAllUserInfo(int page,int size);
    

    ServerResponse searchUsers(String keyword);
    

    ServerResponse resetPassword(String ids);
    

    ServerResponse getLostFoundByPage(int page, int pageSize, String keyword, String type, String state);
    

    ServerResponse updateStickStatus(int id,int stick);


    ServerResponse getCommentsByPage(int page, int pageSize, String keyword, String state);


    ServerResponse updateCommentStatus(Integer commentId, int state);


    ServerResponse deleteCommentById(Integer commentId);


    ServerResponse getTypeByPage(int page, int pageSize, String keyword);
    

    ServerResponse addType(String name);
    

    ServerResponse updateType(Integer id, String name);
    

    ServerResponse deleteTypeById(Integer typeId);
    

    ServerResponse deleteTypeBatch(String ids);


    ServerResponse getUserListByPage(int page, int pageSize, String keyword, Integer state, Integer role);
}

