package com.dancun.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.dancun.Utils.KeyWordsUtils;
import com.dancun.adapter.GameOverAdapter;
import com.dancun.view.GameSurfaceView;

import java.util.List;
import java.util.Map;

/**
 * @author hph
 */
public class GameViewActivity extends AppCompatActivity implements View.OnClickListener {
    private String module;
    private TextView tv_title,tv_left,tv_center,tv_right;
    private ListView listView;
    private Button btn_reGame,btn_finishGame;
    private List<Map<String,String>> list;
    private Handler mHandle=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            setContentView(R.layout.activity_game_view);
            tv_title=findViewById(R.id.tv_title);
            listView=findViewById(R.id.listView);
            btn_finishGame=findViewById(R.id.btn_finishGame);
            btn_reGame=findViewById(R.id.btn_reGame);
            btn_finishGame.setOnClickListener(GameViewActivity.this);
            btn_reGame.setOnClickListener(GameViewActivity.this);
            tv_center=findViewById(R.id.tv_center);
            tv_left=findViewById(R.id.tv_left);
            tv_right=findViewById(R.id.tv_right);

            super.handleMessage(msg);
            int sc=msg.what;
            List<Map<String,String>> words = (List<Map<String, String>>) msg.obj;
            if(words.size()>=10){
                tv_title.setText("游戏结束\r\n\t\t得分 "+sc);
//                finish();
            }else{
                tv_title.setText("游戏通关\r\n\t\t得分 "+sc);
                if(words.size()==0){
                    tv_left.setVisibility(View.GONE);
                    tv_right.setVisibility(View.GONE);
                    tv_center.setText("恭喜你全对");
                }

            }


            listView.setAdapter(new GameOverAdapter(GameViewActivity.this,words));


        }
    };
    GameSurfaceView game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GameSurfaceView(this);
        dataIni();//数据初始化
        setContentView(game);

    }
    private  void dataIni(){
        module=(String)getIntent().getSerializableExtra("module");
//        list= KeyWordsUtils.getKeyWordList(this,module);
        list= (List<Map<String, String>>) getIntent().getSerializableExtra("list");
        setValues(game);


    }
    private void setValues(GameSurfaceView game){
        //设置初始速度
        game.setSpeedInit(8);
        //设置是否加速
        game.setSpeedUp(true);
        //设置加速度
        game.setAcceleration(15);
        //设置关键字
        game.setKeywords(list);
        //
        game.setmHandler(mHandle);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_finishGame){
            finish();
        }else if(v.getId()==R.id.btn_reGame){
            game = new GameSurfaceView(this);
            dataIni();//数据初始化
            setContentView(game);
        }
    }
}
