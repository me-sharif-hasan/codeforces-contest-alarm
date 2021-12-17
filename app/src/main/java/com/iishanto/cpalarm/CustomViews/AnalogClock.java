package com.iishanto.cpalarm.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.iishanto.cpalarm.Codeforces.CfApi;

import java.util.Calendar;

public class AnalogClock extends View {

    Paint paint=null;

    float width=getWidth();
    float height=getHeight();
    float radius =0;
    Rect rect=new Rect();
    float padding=100;
    int fontSize=0;
    int gap=70;

    public AnalogClock(Context context) {
        super(context);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    boolean isInit=false;
    void init(){
        if(isInit) return;
        paint=new Paint();
        width=getWidth();
        height=getHeight();
        radius=(Math.min(width,height)-padding)/2;
        fontSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,13,getResources().getDisplayMetrics());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        init();
        drawClockFace(canvas);

        drawNumbers(canvas);

        drawLoader(canvas);

        drawHands(canvas);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/2,height/2,10,paint);

        postInvalidateDelayed(500);
        invalidate();

    }
    float loaded= 0.0F;
    boolean isSuccess=true;
    private void drawLoader(Canvas canvas){
        if(isSuccess) {
            paint.setColor(Color.WHITE);
        }else{
            paint.setColor(Color.RED);
        }
        paint.setStrokeWidth(5);
        canvas.drawCircle(width/2,height/2, (float) (radius*loaded/100.0),paint);
    }

    private void drawClockFace(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.rgb(204,24,255));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setShadowLayer(40.5f,0,0,Color.DKGRAY);
        canvas.drawCircle(width/2,height/2,radius,paint);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(width/2,height/2,14,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawCircle(width/2,height/2,16,paint);

    }

    private void drawHands(Canvas canvas) {
        paint.setColor(Color.WHITE);
        Calendar c = Calendar.getInstance();
        float hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHand(canvas, (hour * 5f) + (c.get(Calendar.MINUTE) * 5f / 60.0), 1);
        drawHand(canvas, c.get(Calendar.MINUTE), 2);
        drawHand(canvas, c.get(Calendar.SECOND), 3);
    }


    private void drawHand(Canvas canvas,double loc,int stat){
        double angle=Math.PI*loc/30-Math.PI/2;
        int handRadius= (int) (stat==1?radius-70-40:(stat==2?radius-70-20:radius-70));

        paint.setStrokeWidth((float) 15.0/stat);
        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawLine(width/2,
                height/2,
                (float) (width /2.0+Math.cos(angle)*handRadius),
                (float) (height/2.0+Math.sin(angle)*handRadius),
                paint);

    }

    private void drawNumbers(Canvas canvas) {

        paint.setTextSize(fontSize);
        paint.setColor(Color.WHITE);
        for(int i=1;i<=12;i++){
            String ns=String.valueOf(i);
            paint.getTextBounds(ns,0,ns.length(),rect);

            double angle= Math.PI/6*(i-3);
            float x= (float) (width/2.0+Math.cos(angle)*(radius-gap)-rect.width()/2.0);
            float y= (float) (height/2.0+Math.sin(angle)*(radius-gap)+rect.height()/2.0);

            canvas.drawText(ns,x,y,paint);

        }
    }

    public void drawArc(float percent,boolean state){
        System.out.println("ii: drawing "+percent);
        loaded=percent;
        isSuccess=state;
    }


    public Loader anim(){
        return new Loader(this);
    }
    public class Loader implements CfApi.LoadingAnimation{
        AnalogClock ref;
        public Loader(AnalogClock ang){
            ref=ang;
        }
        @Override
        public void setStatus(float percent,boolean status) {
            ref.drawArc(percent,status);
        }
    }
}
