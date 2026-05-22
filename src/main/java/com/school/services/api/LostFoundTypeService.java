package com.school.services.api;

import com.school.utils.ServerResponse;



public interface LostFoundTypeService {

    ServerResponse getAllType();


    ServerResponse getIdByName(String name);


}

