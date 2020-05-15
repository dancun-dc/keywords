package com.hngc.activity;

import com.hngc.entity.KeyWord;

import org.json.JSONArray;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        KeyWord keyWord = new KeyWord();
        JSONArray jsonArray=null;
        try{
            jsonArray=new JSONArray("[{'keyword':'public','type':'true'},{'keyword':'word','type':'false'}]");
            System.out.println(jsonArray.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        keyWord.setJsonArray(jsonArray);
        for (Map<String,String> map: keyWord.getList()){
            System.out.println(map.toString());
        }
    }
}