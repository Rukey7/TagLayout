package com.dl7.taglibrary.old;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.dl7.taglibrary.utils.MeasureUtils;


/**
 * Created by long on 2016/7/20.
 * TagView
 */
@Deprecated
public class TagView extends TextView {

    private Paint mPaint;
    // 背景色
    private int mBgColor;
    // 边框颜色
    private int mBorderColor;
    // 字体颜色
    private int mTextColor;
    // 边框大小
    private float mBorderWidth;
    // 字体大小，单位sp
    private float mTextSize;
    // 边框角半径
    private float mRadius;
    // 字体水平空隙
    private int mHorizontalPadding;
    // 字体垂直空隙
    private int mVerticalPadding;
    // 边框矩形
    private RectF mRect;


    public TagView(Context context, String text) {
        super(context);
        setText(text);
        _init(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void _init(Context context) {
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgColor = Color.parseColor("#33F44336");
        mBorderColor = Color.parseColor("#88F44336");
        mTextColor = Color.parseColor("#FF666666");
        mBorderWidth = MeasureUtils.dp2px(context, 0.5f);
        mTextSize = 13.0f;
        mRadius = MeasureUtils.dp2px(context, 5f);
        mHorizontalPadding = (int) MeasureUtils.dp2px(context, 5);
        mVerticalPadding = (int) MeasureUtils.dp2px(context, 5);
        // 设置字体占中
        setGravity(Gravity.CENTER);
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        setTextColor(mTextColor);
        // 设置字体大小，如果转化为像素单位则要使用setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        setTextSize(mTextSize);
//        setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置矩形边框
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
        // 绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
        super.onDraw(canvas);
    }
}
