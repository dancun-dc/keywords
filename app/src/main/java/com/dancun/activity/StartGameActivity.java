package com.dancun.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author hph
 */
public class StartGameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button javaModule;
    private Button cModule;
    private Button pythonModule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        finView();
        addLinstenner();





    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(StartGameActivity.this,GameViewActivity.class);
        if(v.getId()==R.id.btn_java_module){
//            Toast.makeText(getApplicationContext(),"java",Toast.LENGTH_LONG).show();
            intent.putExtra("module","java");
        }
        if(v.getId()==R.id.btn_c_module){
//            Toast.makeText(getApplicationContext(),"c",Toast.LENGTH_LONG).show();
            intent.putExtra("module","c");
        }
        if(v.getId()==R.id.btn_python_module){
//            Toast.makeText(getApplicationContext(),"python",Toast.LENGTH_LONG).show();
            intent.putExtra("module","python");
        }
        startActivity(intent);
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
