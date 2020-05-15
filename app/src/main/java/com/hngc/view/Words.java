package com.hngc.view;

import android.content.Context;
import android.graphics.*;
import com.hngc.Utils.DensityUtils;

import java.util.Map;
import java.util.Random;

/**
 * @author hph
 */
public class Words {
    private Context mContext;
    private int mWidth;//屏幕宽度
    private int mHeight;//屏幕高度
    private int width=100;//矩形宽度
    private int height=100;//矩形高度
    private int x;//矩形左上角位置
    private int y;
    private String words;//关键字
    private boolean isKeyWords;//是否是关键字
    private Bitmap wordsBitmap;//图案画布
    private RectF rect = new RectF();//定义一个矩形
    private Paint mPaint;//定义一个画笔
    private boolean hasTrueClick;
    private Bitmap keyWordPressBitmap;
    private Map<String,String> map;

    /**
     * 构造方法初始化，弃用了X,Y
     * @param context
     * @param x
     * @param y
     * @param mWidth
     * @param mHeight
     * @param wordsBitmap
     * @param keyWordPressBitmap
     * @param map
     */
    public Words(Context context, int x, int y, int mWidth, int mHeight, Bitmap wordsBitmap,
                 Bitmap keyWordPressBitmap, Map<String,String> map){//构造方法初始化变量
        this.mContext = context;
        this.width=map.get("keyword").length()*50+20+60;//宽度根据电池长度来决定
        this.height=width;
        this.map=map;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.wordsBitmap = wordsBitmap;
        this.words = map.get("keyword");
        this.isKeyWords = Boolean.parseBoolean(map.get("type"));
        this.keyWordPressBitmap=keyWordPressBitmap;
        this.x = new Random().nextInt(mWidth-width);//随机位置掉落
        this.y = -width;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#f1f1f1"));
        mPaint.setStrokeWidth(DensityUtils.dp2px(context,1));
        mPaint.setTextSize(80);
        mPaint.setTypeface(Typeface.MONOSPACE);//等宽字体
        mPaint.setFakeBoldText(true);
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public void setRectSize(int width,int height){this.height=height;this.width=width;}//设置矩形大小
    public void setWordsBitmap(Bitmap blackBitmap) {
        this.wordsBitmap = wordsBitmap;
    }
    public boolean isKeyWords(){return  isKeyWords;}//用于判断点击是不是关键字
    public boolean isHasTrueClick() { return hasTrueClick; }//用于判断关键字是否漏点
    public void setHasTrueClick(boolean hasTrueClick) {
        this.hasTrueClick = hasTrueClick;
        wordsBitmap=keyWordPressBitmap;

    }
        
    public void draw(Canvas canvas){
        rect.set(x,y,x+width,y+height);
        canvas.drawBitmap(wordsBitmap,null,rect,null);
        canvas.drawText(words,x+45,y+width/2+30,mPaint);

    }
    public Map<String,String> getMap(){
        return map;
    }



}
