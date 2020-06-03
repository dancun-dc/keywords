package com.hngc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/**
 * @author hph
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button playMusic;
    private Button closeMusic;
    private Button startGame;
    private Button closeGame;
    private boolean pauseFlag = false;
    MediaPlayer mp ;
//    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp=MediaPlayer.create(this,R.raw.bgm);

        mp.setLooping(true);//设置循环播放
//        am = (AudioManager) this.getSystemService
//                (this.AUDIO_SERVICE);
        findView();
        addLinstener();

        //添加监听


    }
    private void findView(){
        startGame = findViewById(R.id.startGame);
        closeMusic = findViewById(R.id.closeMusic);
        playMusic = findViewById(R.id.playMusic);
        closeGame = findViewById(R.id.closeGame);
    }
    private void addLinstener(){
        startGame.setOnClickListener(this);
        closeGame.setOnClickListener(this);
        playMusic.setOnClickListener(this);
        closeMusic.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.startGame){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, StartGameActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.closeGame){
            try{
                mp.stop();
                mp.reset();
            }catch (Exception e){
                e.printStackTrace();
            }
            finish();
        }else if(v.getId()==R.id.playMusic){
            try{
                mp.start();
            }catch (Exception e){
                e.printStackTrace();
            }
//            Toast.makeText(this,"playMusic",Toast.LENGTH_LONG).show();
        }else if(v.getId()==R.id.closeMusic){
            try {
                mp.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Toast.makeText(MainActivity.this, "停止播放", Toast.LENGTH_SHORT).show();

        }
    }

}
