package com.dancun.thread;

import android.os.Handler;

import com.dancun.Utils.http.ApiPost;

import java.util.HashMap;
import java.util.Map;

public class HttpThread implements Runnable{
    private Map<String,Object> map = new HashMap<>();
    private Handler handler;
    public HttpThread(){};
    public  HttpThread(Map<String,Object> map, Handler handler){
        this.map=map;
        this.handler=handler;
    }
    @Override
    public void run() {
        ApiPost post = new ApiPost();

        post.getword(map,handler);
    }
}
