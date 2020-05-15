package com.hngc.view;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.*;
import androidx.appcompat.app.AlertDialog;
import com.hngc.Utils.DensityUtils;
import com.hngc.activity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author hph
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable, View.OnTouchListener{

    private final int ACCELERATION_DEFAULT = 3;

    private SurfaceHolder mHolder;

    private Canvas mCanvas;//创建画布
    private Thread t;//创建线程
    private boolean isRunning;//判断是否在运行和GameStatus作用一样
    private int mWidth;//像素宽度
    private int mHeight;//像素高度

    private Bitmap wordsBitmap;//words颜色
    private Bitmap wordsPressBitmap;//点击后的颜色
    private int star=10;//10次失误机会
    private boolean speedUp = true;//加速
    private int mSpeed;//速度
    private final int SPEED_DEFAULT = 10;
    private int speedInit ;//初始化速度
    private int acceleration;//每隔acceleration个word加速一次
    private List<Words> wordsList=new ArrayList<>();
    private List<Map<String,String>> errorWords=new ArrayList<>();//失误的单词
    private List<Map<String,String>> keywords=new ArrayList<>();



    private Rect gameRect = new Rect();

    private Rect scorePaintBound = new Rect();

    private int score;//分数
    private int scoreTextSize;//分数文本大小
    private final int SCORE_TEXT_SIZE_DEFAULT = 40;//分数文本大小初始值
    private int scoreColor;//分数字体颜色
    private final int SCORE_COLOR_DEFAULT = Color.parseColor("#ff4848");//分数字体颜色初始值
    private Paint scorePaint;//分数的画笔

    private Context context;//上下文对象，不多解释了
    private Handler mHandler;


    public enum GameStatus
    {
        WAITTING, RUNNING, STOP;
    }
    private GameStatus mGameStatus = GameStatus.WAITTING;

    private int vibratorDuration = 300;
    /**
     * hanler 没有使用message
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    showResult("游戏结束");
                    break;

                case 2:
                    showResult("游戏通关");
                    break;
            }
        }
    };

    /**
     *
     * @param context 上下文对象
     */
    public GameSurfaceView(Context context) {
        this(context,null);
    }
    public GameSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true);// 设置画布 背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);
        this.setOnTouchListener(this);

        mSpeed = speedInit = DensityUtils.dp2px(getContext(),SPEED_DEFAULT);

        acceleration = DensityUtils.dp2px(getContext(),ACCELERATION_DEFAULT);


        scoreTextSize = SCORE_TEXT_SIZE_DEFAULT;
        scorePaint = new Paint();
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setAntiAlias(true);
        scoreColor = SCORE_COLOR_DEFAULT;
        scorePaint.setColor(scoreColor);
        scorePaint.setTextSize(DensityUtils.dp2px(getContext(),scoreTextSize));
        scorePaint.setStrokeWidth(DensityUtils.dp2px(getContext(),3));

        initBitmap();

    }

    /**
     * 加载当前view时时调用的方法
     * 不是创建对象时调用的
     * @param holder
     */
    @Override//创建时调用的方法
    public void surfaceCreated(SurfaceHolder holder) {
//        showResult("test");
        isRunning = true;
        t = new Thread(this);//另起一个线程调用自己run方法
        t.start();//启动线程
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {//
        gameRect.set(0,0,width,height);

        mWidth = width;
        mHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {

        while (isRunning)
        {
            long start = System.currentTimeMillis();
            logic();
            draw();

            long end = System.currentTimeMillis();

            try
            {
                if (end - start < 30)
                {
                    Thread.sleep(30 - (end - start));
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

    }

    private void logic(){
        Random random = new Random();
        if (wordsList.size()<=0) {//初始化
                //创建单词组
                Words words = new Words(getContext(), random.nextInt(mWidth),-300
                        , mWidth ,mHeight,wordsBitmap,wordsPressBitmap,keywords.get(random.nextInt(keywords.size())));
                wordsList.add(words);
            return;
        }
        if (mGameStatus == GameStatus.RUNNING){//游戏运行状态
            if (wordsList.get(wordsList.size()-1).getY()>mHeight/3){//最后一个单词到了屏幕的1/3位置加载第新的单词
                Words words = new Words(getContext(), random.nextInt(mWidth),-300
                        , mWidth ,mHeight,wordsBitmap,wordsPressBitmap,keywords.get(random.nextInt(keywords.size())));
                wordsList.add(words);
            }
            if (wordsList.get(0).getY()>=mHeight){
                if (!wordsList.get(0).isHasTrueClick() && wordsList.get(0).isKeyWords()){//关键字已经在屏幕下面，但是还没有点击
                    errorWords.add(wordsList.get(0).getMap());
                    if(--star<=0){
                        mGameStatus = GameStatus.STOP;
                        handler.sendEmptyMessage(1);
                    }

                }
                wordsList.remove(0);
            }

            for (int i =0;i<wordsList.size();i++){//向下移动
                wordsList.get(i).setY(wordsList.get(i).getY()+mSpeed);
            }

        }
    }

    /**
     * 封装画图方法
     */
    private void draw(){

        try{
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null){
                drawBg();//画背景
                drawBlockGroup();//画方块
                drawScore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     * 加速
     */
    private void speedUp(){
        if (isSpeedUp()) {
            if (score % acceleration == 0&&score!=0) {
                mSpeed++;
            }
        }
    }

    /**
     * 画背景，很重要
     */
    private void drawBg() {
        Paint bgPaint = new Paint();
//        mCanvas.drawBitmap(loadBitmapByResId(R.mipmap.bg), null, gameRect, null);
        bgPaint.setColor(Color.parseColor("#FF607D8B"));
        mCanvas.drawRect(new Rect(0,0,mWidth,mHeight),bgPaint);
        if(mGameStatus==GameStatus.WAITTING){
//            mCanvas.drawBitmap(loadBitmapByResId(R.mipmap.bg), null, gameRect, null);
            bgPaint.setColor(Color.parseColor("#cccccc"));
            bgPaint.setTextSize(100);
            mCanvas.drawText("点击屏幕开始游戏",(mWidth-800)/2,mHeight/2-100,bgPaint);
        }

    }
    private void drawBlockGroup(){
        for (Words words : wordsList){
            words.draw(mCanvas);
        }
    }

    /**
     * 加载Bitmap图形
     */
    private void initBitmap(){
        wordsBitmap = loadBitmapByResId(R.mipmap.wordspic);
        wordsPressBitmap = loadBitmapByResId(R.mipmap.wordspresspic);
    }
    private Bitmap loadBitmapByResId(int resId){
        return BitmapFactory.decodeResource(getResources(),resId);
    }

    /**
     * 画分数 和 心
     */
    private void drawScore(){
        String scoreStr ="❤ "+star+"\t\t\t\t\t\t分数  "+ score+"";
        scorePaint.getTextBounds(scoreStr,0,scoreStr.length(),scorePaintBound);
        mCanvas.drawText(scoreStr,mWidth/2-scorePaintBound.width()/2,DensityUtils.dp2px(getContext(),30)+scorePaintBound.height(),scorePaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGameStatus ==GameStatus.STOP){
            return true;
        }
        if (mGameStatus == GameStatus.WAITTING){//进入游戏初始化
            mGameStatus = GameStatus.RUNNING;//点击一次才会开始游戏
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点击坐标
                float downX = event.getX();
                float downY = event.getY();
                int size = wordsList.size();
                for (int i =0;i<size;i++) {

                    Words words = wordsList.get(i);
                    //单词所在矩形的左上角坐标
                    int x = words.getX();
                    int y = words.getY();
                    if (!words.isHasTrueClick()) {
                        //判断点击的位置是不是在单词所在的矩形当中
                        if (downX > x && downX < x + words.getWidth() && downY > y && downY < y + words.getHeight()) {
                            //只判断一次就跳出循环
                            if(words.isKeyWords()){
                                speedUp();//加速默认关闭
                                if(++score>=50){
                                    mGameStatus = GameStatus.STOP;
                                    handler.sendEmptyMessage(2);
                                }

                            }else {
                                errorWords.add(words.getMap());
                                if(--star<=0){
                                    mGameStatus = GameStatus.STOP;
                                    handler.sendEmptyMessage(1);
                                }

                            }
                            words.setHasTrueClick(true);
                            break;
                        }

                    }
                }
        }
        return true;
    }

    /**
     * 游戏结束弹窗，封装成函数，方便调用
     * @param reason
     */
    private void showResult(String reason){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(reason)
//                .setMessage(reason)
//                .setPositiveButton("重新玩", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        reset();
//                        mGameStatus = GameSurfaceView.GameStatus.WAITTING;
//                    }
//                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isRunning=false;
                        Message message=Message.obtain();
                        message.obj=errorWords;
                        message.what=score;
                        mHandler.sendMessage(message);
//                        System.exit(0);//弃用改用handler+meaasge
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = alertDialog.getWindow();

        alertDialog.show();
        dialogWindow.setBackgroundDrawableResource(R.mipmap.dialogpic);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP|Gravity.LEFT);

        lp.y = (int) getResources().getDisplayMetrics().heightPixels/2-150; // 新位置Y坐标
        lp.x=100;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels-200; // 宽度
        lp.height=300;
        lp.dimAmount = 0.0f;
        dialogWindow.setAttributes(lp);

    }

    private void reset(){
        mSpeed = speedInit;
        score = 0;
        wordsList = new ArrayList<>();
    };

    //为了提高安全性，全局参数使用private封装，提供getting和setting方法
    public boolean isSpeedUp() {
        return speedUp;
    }

    public void setSpeedUp(boolean speedUp) {
        this.speedUp = speedUp;
    }

    public int getVibratorDuration() {
        return vibratorDuration;
    }

    public void setVibratorDuration(int vibratorDuration) {
        this.vibratorDuration = vibratorDuration;
    }

    public int getScoreTextSize() {
        return scoreTextSize;
    }

    public void setScoreTextSize(int scoreTextSize) {
        this.scoreTextSize = scoreTextSize;
        scorePaint.setTextSize(DensityUtils.dp2px(getContext(),scoreTextSize));
    }

    public int getScoreColor() {
        return scoreColor;
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
        scorePaint.setColor(scoreColor);
    }

    public Bitmap getBlackPressBitmap() {
        return wordsPressBitmap;
    }

    public void setBlackPressBitmap(Bitmap wordsPressBitmap) {
        this.wordsPressBitmap = wordsPressBitmap;
    }

    public int getSpeedInit() {
        return speedInit;
    }

    public void setSpeedInit(int speedInit) {
        this.speedInit = speedInit;
        mSpeed = speedInit;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setStar(int star){
        this.star=star;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public Bitmap getWordsBitmap() {
        return wordsBitmap;
    }

    public void setWordsPressBitmap(Bitmap wordsBitmap) {
    this.wordsBitmap = wordsBitmap;
    }

    public  void setKeywords(List<Map<String,String>> keywords){
        this.keywords=keywords;
    }
    public void setmHandler(Handler mHandler){
        this.mHandler=mHandler;
    }
    public  boolean isRunning(){
        return isRunning;
    }


}
