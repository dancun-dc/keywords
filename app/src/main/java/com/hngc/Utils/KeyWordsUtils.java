package com.hngc.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.hngc.entity.KeyWord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author hph
 */
public class KeyWordsUtils {

    public static List<Map<String,String>> getKeyWordList (Context context,String module){
        JSONArray jsonArray=getConf(context,module);
        List<Map<String,String>> list =null;
        KeyWord keyWord  =new KeyWord();
        keyWord.setJsonArray(jsonArray);
        list=keyWord.getList();
        return list;
    }

    public static JSONArray getConf(Context context, String module){
        String conf = "";
        JSONArray jsonArray=null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("keyword.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            conf=stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonArray=new JSONObject(conf).getJSONArray(module);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  jsonArray;
    }


}
