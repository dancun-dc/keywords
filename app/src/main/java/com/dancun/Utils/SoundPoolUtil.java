package com.dancun.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.SoundPool;

/**
 * @author hph18
 */
public class SoundPoolUtil {
    private static SoundPoolUtil soundPoolUtil;
    private SoundPool soundPool;
    private int resoures;

    @SuppressLint("NewApi")//这里初始化SoundPool的方法是安卓5.0以后提供的新方式
    public SoundPoolUtil(Context context,int resoures) {
        soundPool = new SoundPool.Builder().build();
        //加载音频文件
        this.resoures=resoures;
        soundPool.load(context, resoures, 1);
    }

    public void play() {
//        Log.d(, "number " + number);
        /**
         * 播放音频
         * params说明：
         * //左耳道音量【0~1】
         * //右耳道音量【0~1】
         * //播放优先级【0表示最低优先级】
         * //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
         * //播放速度【1是正常，范围从0~2】
         */
        soundPool.play(1, 1, 1, 1, 0, 1);
    }
}