package com.driver3.driverapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    public static JSONObject jsonResult(String tableName, String fieldName[], String types[], String values[], String functionNumber) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("TableName", tableName);
        JSONObject json2 = new JSONObject();
        json2.put("functionNumber", functionNumber);
        for(int i = 0; i < types.length; i++){
            JSONObject json3 = new JSONObject();
            json3.put(types[i], values[i]);
            json2.put(fieldName[i], json3);
        }
        json.put("Item", json2);
        return json;
    }
}
