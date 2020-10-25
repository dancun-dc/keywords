package com.dancun.Utils.http;

import android.os.Handler;
import android.os.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiPost {
    private List<Map<String,Object>> list = new ArrayList<>();
    private Message message = Message.obtain();
    public List<Map<String,Object>> getword(Map<String,Object> arg, Handler handler) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.115.4.140/")//设置网络请求url，后面一段写在网络请求接口里面
                .addConverterFactory(GsonConverterFactory.create())//添加Gson支持，然后Retrofit就会使用Gson将响应体（api接口的Take）转换我们想要的类型。
                .build();
        Api api = retrofit.create(Api.class);


//      Take为响应实体类，用来接受机器人返回的回复数据，以下为接口调用
//        用法和OkHttp的call如出一辙,
//                不同的是：如果是Android系统，回调方法执行在主线程
        Call<List<Map<String,Object>>> call = api.request(arg);//ask在前面赋予了各个字段的数据，在接口api中转成了json格式的数据，发送请求


        call.enqueue(new Callback<List<Map<String,Object>>>() {
            //         请求成功
            @Override
            public void onResponse(Call<List<Map<String,Object>>> call, Response<List<Map<String,Object>>> response) {
                list=response.body();
                message.obj=list;
                handler.sendMessage(message);

            }

            //            请求失败
            @Override
            public void onFailure(Call<List<Map<String,Object>>> call, Throwable t) {
                message.obj=list;
                handler.sendMessage(message);

            }
        });
        return list;
    }

}

