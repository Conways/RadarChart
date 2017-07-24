package com.conways.radarchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Conways on 2017/7/21.
 */

public class RadarChart extends View {

    private float roundLineWith;
    private int roundLineColor;
    private int roundColor;

    private float itemTextSize;
    private int itemTextColor;
    private int itemLineColor;
    private float itemLineWith;

    private int abilityRoundLineColor;
    private float abilityRoundLineWith;
    private int abliityRoundColor;

    private int deflectionAngle;
    private int max;

    private float itemRadius;


    private Paint roundLinePaint;
    private Paint roundBgPaint;

    private Paint itemTextPaint;
    private Paint itemLinePaint;

    private Paint ablityRoundLinePaint;
    private Paint ablityRoundBgPaint;

    private Path roundPath;
    private Path abilityPath;


    private int mWith;
    private int mHeight;

    private List<RadarData> list;
    private Point roundPoint;
    private Point ablityPoint;
    private Point leftBottomPoint;
    private Rect rect;

    public RadarChart(Context context) {
        super(context);
        initPaint();
        initData();
    }

    public RadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        initPaint();
        initData();
    }

    public RadarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initPaint();
        initData();
    }


    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RadarChart);
        roundLineWith = ta.getDimension(R.styleable.RadarChart_roundLineWith, 1);
        roundLineColor = ta.getColor(R.styleable.RadarChart_roundLineColor, 0xff000000);
        roundColor = ta.getColor(R.styleable.RadarChart_roundColor, 0xff000000);

        itemTextSize = ta.getDimension(R.styleable.RadarChart_itemTextSize, 1);
        itemTextColor = ta.getColor(R.styleable.RadarChart_itemTextColor, 0xff000000);
        itemLineColor = ta.getColor(R.styleable.RadarChart_itemLineColor, 0xff000000);
        itemLineWith = ta.getDimension(R.styleable.RadarChart_itemLineWith, 1);

        abilityRoundLineColor = ta.getColor(R.styleable.RadarChart_abilityRoundLineColor, 0xff000000);
        abilityRoundLineWith = ta.getDimension(R.styleable.RadarChart_abilityRoundLineWith, 1);
        abliityRoundColor = ta.getColor(R.styleable.RadarChart_abliityRoundColor, 0x0f000000);

        max = ta.getInteger(R.styleable.RadarChart_max, 100);
        deflectionAngle = ta.getInteger(R.styleable.RadarChart_deflectionAngle, 0);
        itemRadius = ta.getDimension(R.styleable.RadarChart_itemRadius, dip2px(32f));
        ta.recycle();
    }

    private void initPaint() {
        roundLinePaint = new Paint();
        roundLinePaint.setAntiAlias(true);
        roundLinePaint.setStyle(Paint.Style.STROKE);
        roundLinePaint.setColor(roundLineColor);
        roundLinePaint.setStrokeWidth(roundLineWith);

        roundBgPaint = new Paint();
        roundBgPaint.setAntiAlias(true);
        roundBgPaint.setStyle(Paint.Style.FILL);
        roundBgPaint.setColor(roundColor);

        itemTextPaint = new Paint();
        itemTextPaint.setAntiAlias(true);
        itemTextPaint.setTextSize(itemTextSize);
        itemTextPaint.setColor(itemTextColor);

        itemLinePaint = new Paint();
        itemLinePaint.setAntiAlias(true);
        itemLinePaint.setColor(itemLineColor);
        itemLinePaint.setStrokeWidth(itemLineWith);

        ablityRoundLinePaint = new Paint();
        ablityRoundLinePaint.setAntiAlias(true);
        ablityRoundLinePaint.setColor(abilityRoundLineColor);
        ablityRoundLinePaint.setStyle(Paint.Style.STROKE);
        ablityRoundLinePaint.setStrokeWidth(abilityRoundLineWith);

        ablityRoundBgPaint = new Paint();
        ablityRoundBgPaint.setAntiAlias(true);
        ablityRoundBgPaint.setStyle(Paint.Style.FILL);
        ablityRoundBgPaint.setColor(abliityRoundColor);

        roundPath = new Path();
        abilityPath = new Path();
        rect=new Rect();
    }

    private void initData() {
        roundPoint = new Point();
        ablityPoint = new Point();
        leftBottomPoint=new Point();
        list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RadarData radarData = new RadarData();
            radarData.setAbliity(50 + (int) (Math.random() * 50));
            radarData.setItemName(i+"能力");
            list.add(radarData);
        }
    }

    public void setDeflectionAngle(int deflectionAngle) {
        this.deflectionAngle = deflectionAngle;
        invalidate();
    }

    public void setList(List<RadarData> list) {
        if (null==list||list.size()<=2){
            throw new IllegalArgumentException("list's size must biger than 2");
        }
        this.list.clear();
        this.list.addAll(list);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMeasureMode == MeasureSpec.EXACTLY) {
            mWith = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            mWith = 500;
        }
        if (heightMeasureMode == MeasureSpec.EXACTLY) {
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            mHeight = 500;
        }
        setMeasuredDimension(mWith, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAll(canvas);
    }

    private void drawAll(Canvas canvas) {
        if (null == list || list.size() < 3) {
            return;
        }
        drawRound(canvas);
        drawAblity(canvas);
    }


    private void drawRound(Canvas canvas) {

        roundPath.reset();
        for (int i = 0; i < list.size(); i++) {
            String str=list.get(i).getItemName();
            itemTextPaint.getTextBounds(str,0,str.length(),rect);
            findRoundPointByPositon(i);
            canvas.drawLine(mWith / 2, mHeight / 2, roundPoint.getX(), roundPoint.getY(), itemLinePaint);
            if (i == 0) {
                roundPath.moveTo(roundPoint.getX(), roundPoint.getY());
            } else {
                roundPath.lineTo(roundPoint.getX(), roundPoint.getY());
            }
            canvas.drawText(str, leftBottomPoint.getX()-rect.width()/2, leftBottomPoint.getY()
                    +rect.height()/2,
                    itemTextPaint);

        }
        roundPath.close();
        canvas.drawPath(roundPath, roundBgPaint);
        canvas.drawPath(roundPath, roundLinePaint);
    }

    private void drawAblity(Canvas canvas) {
        abilityPath.reset();
        for (int i = 0; i < list.size(); i++) {
            findAblityPointByPosition(i);

            if (i == 0) {
                abilityPath.moveTo(ablityPoint.getX(), ablityPoint.getY());
            } else {
                abilityPath.lineTo(ablityPoint.getX(), ablityPoint.getY());
            }
        }
        abilityPath.close();
        canvas.drawPath(abilityPath, ablityRoundBgPaint);
        canvas.drawPath(abilityPath, ablityRoundLinePaint);
    }


    private void findRoundPointByPositon(int position) {
        int angle = deflectionAngle + position * 360 / list.size();
        int x = (int) (mWith / 2 + itemRadius * Math.sin(Math.PI * angle / 180));
        int y = (int) (mHeight / 2 - itemRadius * Math.cos(Math.PI * angle / 180));
        roundPoint.setX(x);
        roundPoint.setY(y);
        double radius=Math.sqrt(rect.width()*rect.width()/4+rect.height()*rect.height()/4);
        int x1 = (int) (mWith / 2 + (itemRadius+radius) * Math.sin(Math.PI * angle / 180));
        int y1 = (int) (mHeight / 2 - (itemRadius+radius) * Math.cos(Math.PI * angle / 180));
        leftBottomPoint.setX(x1);
        leftBottomPoint.setY(y1);
    }



    private void findAblityPointByPosition(int position) {
        int angle = deflectionAngle + position * 360 / list.size();
        int ablity = list.get(position).getAbliity();
        if (ablity > max) {
            ablity = max;
        }
        float ablityRadius = itemRadius * ablity / max;
        int x = (int) (mWith / 2 + ablityRadius * Math.sin(Math.PI * angle / 180));
        int y = (int) (mHeight / 2 - ablityRadius * Math.cos(Math.PI * angle / 180));
        ablityPoint.setX(x);
        ablityPoint.setY(y);
    }


    private int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    class Point {
        int x;
        int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
