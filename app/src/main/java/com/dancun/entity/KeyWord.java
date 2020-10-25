package com.dancun.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hph
 */
public class KeyWord {
    private JSONArray jsonArray = null;
    private List<Map<String,String>> list = new ArrayList<>();


    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray=jsonArray;

        try {
            for(int a=0;a<jsonArray.length();a++){
                JSONObject jsonObject=jsonArray.getJSONObject(a);
                Map<String ,String> map = new HashMap<>();
                map.put("keyword",jsonObject.getString("keyword"));
                map.put("type",jsonObject.getString("type"));
                if(!Boolean.parseBoolean(map.get("type"))){
                    map.put("rightkey",jsonObject.getString("rightkey"));
                }

                list.add(map);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }


}
