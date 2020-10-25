package com.dancun.activity;

import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.dancun.thread.HttpThread;
import com.dancun.view.LoadingDialog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hph
 */
public class StartGameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button javaModule;
    private Button cModule;
    private Button pythonModule;
    private Dialog loadDialog;
    private Intent intent;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<Map<String,String>> list = (List<Map<String, String>>) msg.obj;
            intent.putExtra("list", (Serializable) list);
            LoadingDialog.closeDialog(loadDialog);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        finView();
        addLinstenner();





    }



    @Override
    public void onClick(View v) {
        loadDialog = LoadingDialog.createLoadingDialog(StartGameActivity.this, "加载中...");
        intent = new Intent();
        intent.setClass(StartGameActivity.this,GameViewActivity.class);
        String lan="";
        if(v.getId()==R.id.btn_java_module){
//            Toast.makeText(getApplicationContext(),"java",Toast.LENGTH_LONG).show();
            lan="java";

        }
        if(v.getId()==R.id.btn_c_module){
//            Toast.makeText(getApplicationContext(),"c",Toast.LENGTH_LONG).show();
            lan="c";
        }
        if(v.getId()==R.id.btn_python_module){
//            Toast.makeText(getApplicationContext(),"python",Toast.LENGTH_LONG).show();
           lan="python";
        }
        intent.putExtra("module",lan);
        Map<String,Object> map = new HashMap<>();
        map.put("lan",lan);
        new Thread(new HttpThread(map,handler)).start();

    }
    private void finView(){
        javaModule=findViewById(R.id.btn_java_module);
        cModule=findViewById(R.id.btn_c_module);
        pythonModule=findViewById(R.id.btn_python_module);
    }
    private void addLinstenner() {
        javaModule.setOnClickListener(this);
        cModule.setOnClickListener(this);
        pythonModule.setOnClickListener(this);
    }
}
