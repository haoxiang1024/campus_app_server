package com.school.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class Json {

    public static Object getJsonValueByKey(String jsonStr, String key) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        return jsonObject.get(key);
    }



    public static String updateByKey(String json, String key, String newValue) {
        Map<String, String> oldValue = JSON.parseObject(json, Map.class);
        oldValue.put(key, newValue);
        return JSONObject.toJSONString(oldValue);
    }




}
