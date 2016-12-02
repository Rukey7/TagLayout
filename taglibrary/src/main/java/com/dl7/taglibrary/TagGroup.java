package com.dl7.taglibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dl7.taglibrary.utils.MeasureUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by long on 2016/7/20.
 * TagGroup
 */
public class TagGroup extends ViewGroup {

    private Paint mPaint;
    // 背景色
    private int mBgColor;
    // 边框颜色
    private int mBorderColor;
    // 边框大小
    private float mBorderWidth;
    // 边框角半径
    private float mRadius;
    // Tag之间的垂直间隙
    private int mVerticalInterval;
    // Tag之间的水平间隙
    private int mHorizontalInterval;
    // 边框矩形
    private RectF mRect;
    // 可用的最大宽度
    private int mAvailableWidth;

    private int mTagBgColor;
    private int mTagBorderColor;
    private int mTagTextColor;
    private float mTagBorderWidth;
    private float mTagTextSize;
    private float mTagRadius;
    private int mTagHorizontalPadding;
    private int mTagVerticalPadding;
    private TagView.OnTagClickListener mOnTagClickListener;
    // 这个用来保存设置监听器之前的TagView
    private List<TagView> mTagViews = new ArrayList<>();
    // 显示模式
    private int mTagMode = TagView.MODE_ROUND_RECT;
    private int mFitTagNum = TagView.INVALID_VALUE;
    private boolean mIsPressFeedback = false;


    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context);
    }

    private void _init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgColor = Color.parseColor("#11FF0000");
        mBorderColor = Color.parseColor("#22FF0000");
        mBorderWidth = MeasureUtils.dp2px(context, 0.5f);
        mRadius = MeasureUtils.dp2px(context, 5f);
        int defaultInterval = (int) MeasureUtils.dp2px(context, 5f);
        mHorizontalInterval = defaultInterval;
        mVerticalInterval = defaultInterval;
        mRect = new RectF();
        // 如果想要自己绘制内容，则必须设置这个标志位为false，否则onDraw()方法不会调用
        setWillNotDraw(false);
        setPadding(defaultInterval, defaultInterval, defaultInterval, defaultInterval);

        // 初始化TagView
        mTagBgColor = Color.parseColor("#33F44336");
        mTagBorderColor = Color.parseColor("#88F44336");
        mTagTextColor = Color.parseColor("#FF666666");
        mTagBorderWidth = MeasureUtils.dp2px(context, 0.5f);
        mTagTextSize = 13.0f;
        mTagRadius = MeasureUtils.dp2px(context, 5f);
        mTagHorizontalPadding = (int) MeasureUtils.dp2px(context, 5);
        mTagVerticalPadding = (int) MeasureUtils.dp2px(context, 5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        // 计算可用宽度，为测量宽度减去左右padding值，这个放在measureChildren前面，在子视图中会用到这个参数
        mAvailableWidth = widthSpecSize - getPaddingLeft() - getPaddingRight();
        Log.e("TagGroup", "onMeasure " + mAvailableWidth);
        // 测量子视图
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int tmpWidth = 0;
        int measureHeight = 0;
        int maxLineHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 记录该行的最大高度
            if (maxLineHeight == 0) {
                maxLineHeight = child.getMeasuredHeight();
            } else {
                maxLineHeight = Math.max(maxLineHeight, child.getMeasuredHeight());
            }
            // 统计该行TagView的总宽度
            tmpWidth += child.getMeasuredWidth() + mHorizontalInterval;
            // 如果超过可用宽度则换行
            if (tmpWidth - mHorizontalInterval > mAvailableWidth) {
                // 统计TagGroup的测量高度，要加上垂直间隙
                measureHeight += maxLineHeight + mVerticalInterval;
                // 重新赋值
                tmpWidth = child.getMeasuredWidth() + mHorizontalInterval;
                maxLineHeight = child.getMeasuredHeight();
            }
        }
        // 统计TagGroup的测量高度，加上最后一行
        measureHeight += maxLineHeight;

        // 设置测量宽高，记得算上padding
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, measureHeight + getPaddingTop() + getPaddingBottom());
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }

        mAvailableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        // 当前布局使用的top坐标
        int curTop = getPaddingTop();
        // 当前布局使用的left坐标
        int curLeft = getPaddingLeft();
        int maxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (maxHeight == 0) {
                maxHeight = child.getMeasuredHeight();
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }

            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            // 超过一行做换行操作
            if (width + curLeft > mAvailableWidth + getPaddingLeft()) {
                curLeft = getPaddingLeft();
                // 计算top坐标，要加上垂直间隙
                curTop += maxHeight + mVerticalInterval;
                maxHeight = child.getMeasuredHeight();
            }
            // 设置子视图布局
            child.layout(curLeft, curTop, curLeft + width, curTop + height);
            // 计算left坐标，要加上水平间隙
            curLeft += width + mHorizontalInterval;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
        // 绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
    }

    /*********************************设置TagGroup*********************************/

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = MeasureUtils.dp2px(getContext(), borderWidth);
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public int getVerticalInterval() {
        return mVerticalInterval;
    }

    public void setVerticalInterval(int verticalInterval) {
        mVerticalInterval = verticalInterval;
    }

    public int getHorizontalInterval() {
        return mHorizontalInterval;
    }

    public void setHorizontalInterval(int horizontalInterval) {
        mHorizontalInterval = horizontalInterval;
    }

    public int getAvailableWidth() {
        return mAvailableWidth;
    }

    public void setAvailableWidth(int availableWidth) {
        mAvailableWidth = availableWidth;
    }

    public int getFitTagNum() {
        return mFitTagNum;
    }

    public void setFitTagNum(int fitTagNum) {
        mFitTagNum = fitTagNum;
    }

    /*********************************设置TagView*********************************/

    private TagView _initTagView(String text) {
        TagView tagView = new TagView(getContext(), text);
        tagView.setBgColor(mTagBgColor);
        tagView.setBorderColor(mTagBorderColor);
        tagView.setTextColor(mTagTextColor);
        tagView.setBorderWidth(mTagBorderWidth);
        tagView.setRadius(mTagRadius);
        tagView.setTextSize(mTagTextSize);
        tagView.setHorizontalPadding(mTagHorizontalPadding);
        tagView.setVerticalPadding(mTagVerticalPadding);
        tagView.setPressFeedback(mIsPressFeedback);
        tagView.setTagClickListener(mOnTagClickListener);
        if (mOnTagClickListener == null) {
            mTagViews.add(tagView);
        }
        tagView.setTagMode(mTagMode);
        return tagView;
    }

    public int getTagBgColor() {
        return mTagBgColor;
    }

    public void setTagBgColor(int tagBgColor) {
        mTagBgColor = tagBgColor;
    }

    public int getTagBorderColor() {
        return mTagBorderColor;
    }

    public void setTagBorderColor(int tagBorderColor) {
        mTagBorderColor = tagBorderColor;
    }

    public int getTagTextColor() {
        return mTagTextColor;
    }

    public void setTagTextColor(int tagTextColor) {
        mTagTextColor = tagTextColor;
    }

    public float getTagBorderWidth() {
        return mTagBorderWidth;
    }

    public void setTagBorderWidth(float tagBorderWidth) {
        mTagBorderWidth = MeasureUtils.dp2px(getContext(), tagBorderWidth);
    }

    public float getTagTextSize() {
        return mTagTextSize;
    }

    public void setTagTextSize(float tagTextSize) {
        mTagTextSize = tagTextSize;
    }

    public float getTagRadius() {
        return mTagRadius;
    }

    public void setTagRadius(float tagRadius) {
        mTagRadius = tagRadius;
    }

    public int getTagHorizontalPadding() {
        return mTagHorizontalPadding;
    }

    public void setTagHorizontalPadding(int tagHorizontalPadding) {
        mTagHorizontalPadding = tagHorizontalPadding;
    }

    public int getTagVerticalPadding() {
        return mTagVerticalPadding;
    }

    public void setTagVerticalPadding(int tagVerticalPadding) {
        mTagVerticalPadding = tagVerticalPadding;
    }

    public boolean isPressFeedback() {
        return mIsPressFeedback;
    }

    public void setPressFeedback(boolean pressFeedback) {
        mIsPressFeedback = pressFeedback;
    }

    public TagView.OnTagClickListener getOnTagClickListener() {
        return mOnTagClickListener;
    }

    public void setOnTagClickListener(TagView.OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
        // 避免先调用设置TagView，后设置监听器导致前面设置的TagView不能响应点击
        for (TagView tagView : mTagViews) {
            tagView.setTagClickListener(mOnTagClickListener);
        }
    }

    public void setTagMode(@TagView.TagMode int tagMode) {
        mTagMode = tagMode;
    }

    /******************************************************************/

    /**
     * 添加Tag
     * @param text tag内容
     */
    public void addTag(String text) {
        addView(_initTagView(text));
    }

    public void addTags(String... textList) {
        for (String text : textList) {
            addTag(text);
        }
    }

    public void cleanTags() {
        removeAllViews();
        postInvalidate();
    }

    public void setTags(String... textList) {
        cleanTags();
        addTags(textList);
    }
}
