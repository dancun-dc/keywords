package com.dancun.Utils.http;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface Api {

        //发送json数据形式的post请求，把网络请求接口的后半部分openapi/api/v2写在里面
        //Get是请求数据实体类，Take接受数据实体类
        @POST("keyword/words/list.json") //Post请求发送数据
        Call<List<Map<String,Object>>> request(@QueryMap Map<String, Object> map);//@body即非表单请求体，@QueryMap为表单请求
        // 被@Body注解的ask将会被Gson转换成json发送到服务器，返回到Take。 // 其中返回类型为Call<*>，*是接收数据的类

}
