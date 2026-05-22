package com.school.services.api;

import com.school.utils.ServerResponse;
import org.apache.ibatis.annotations.Param;



public interface LostFoundService {

    ServerResponse getLostFoundDetail(int lostfoundtypeId,String type);


    ServerResponse getUserName(int id);


    ServerResponse addLostFound(String lostfoundtJson);


    ServerResponse getLostFoundByUserId(int user_id);


    ServerResponse updateState(int id,String state,int user_id);


    ServerResponse showTopList(@Param("stick")Integer stick);
    

    ServerResponse getInfoByKey(String keyword);
    

    ServerResponse getLostFoundById(Integer id);
    

    ServerResponse deleteLostFoundById(Integer id);
    

    ServerResponse updateLostFoundStatus(Integer id, String state);

    

    ServerResponse getDetailByTitle(String title,String type);
}
