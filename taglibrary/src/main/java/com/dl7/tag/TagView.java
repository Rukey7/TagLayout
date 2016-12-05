package com.dl7.tag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.dl7.tag.drawable.RotateDrawable;
import com.dl7.tag.utils.MeasureUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by long on 2016/7/20.
 * TagView
 */
public class TagView extends TextView {

    // 无效数值
    public final static int INVALID_VALUE = -1;

    private Paint mPaint;
    // 背景色
    private int mBgColor;
    // 边框颜色
    private int mBorderColor;
    // 边框大小
    private float mBorderWidth;
    // 边框角半径
    private float mRadius;
    // Tag内容
    private CharSequence mTagText;
    // 字体水平空隙
    private int mHorizontalPadding;
    // 字体垂直空隙
    private int mVerticalPadding;
    // 边框矩形
    private RectF mRect;
    // 调整标志位，只做一次
    private boolean mIsAdjusted = false;
    // 点击监听器
    private OnTagClickListener mTagClickListener;
    // 标签是否被按住
    private boolean mIsTagPress = false;
    // 宽度固定
    private int mFitWidth = INVALID_VALUE;
    // 是否使能按压反馈
    private boolean mIsPressFeedback = false;
    // 原始标签颜色
    private int mOriTextColor;


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
     *
     * @param context
     */
    private void _init(Context context) {
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTagText = getText();
        // 设置字体占中
        setGravity(Gravity.CENTER);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTagClickListener != null) {
                    mTagClickListener.onTagClick(String.valueOf(mTagText), mTagMode);
                }
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mTagClickListener != null) {
                    mTagClickListener.onTagLongClick(String.valueOf(mTagText), mTagMode);
                }
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fitTagNum = ((TagLayout) getParent()).getFitTagNum();
        if (fitTagNum == INVALID_VALUE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int availableWidth = ((TagLayout) getParent()).getAvailableWidth();
            int horizontalInterval = ((TagLayout) getParent()).getHorizontalInterval();
            int width = (availableWidth - (fitTagNum - 1) * horizontalInterval) / fitTagNum;
            int fitWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            super.onMeasure(fitWidthSpec, heightMeasureSpec);
            mFitWidth = width;
        }
        _adjustText();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置矩形边框
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = mRadius;
        if (mTagShape == SHAPE_ARC) {
            radius = mRect.height() / 2;
        } else if (mTagShape == SHAPE_RECT) {
            radius = 0;
        }
        // 是否进行按压反馈处理
        if (mIsPressFeedback) {
            // 按压反馈只会使用 mBgColor，且字体颜色在白色和 mBgColor 色直接变换
            mPaint.setColor(mOriTextColor);
            if (mIsTagPress) {
                mPaint.setStyle(Paint.Style.FILL);
                setTextColor(Color.WHITE);
                _setIconColor(Color.WHITE);
            } else {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mBorderWidth);
                setTextColor(mOriTextColor);
                _setIconColor(mOriTextColor);
            }
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
        } else {
            // 绘制背景
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mBgColor);
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
            // 绘制边框
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mBorderColor);
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
            _setIconColor(mOriTextColor);
        }

        super.onDraw(canvas);
    }

    /**
     * 调整内容，如果超出可显示的范围则做裁剪
     */
    private void _adjustText() {
        if (mIsAdjusted) {
            return;
        }
        mIsAdjusted = true;
        // 获取最大可用宽度
        int availableWidth;
        if (mFitWidth == INVALID_VALUE) {
            availableWidth = ((TagLayout) getParent()).getAvailableWidth();
        } else {
            availableWidth = mFitWidth;
        }
        mPaint.setTextSize(getTextSize());

        // 计算字符串长度
        float textWidth = mPaint.measureText(String.valueOf(mTagText));
        if (mTagMode == MODE_CHANGE) {
            availableWidth -= MeasureUtils.getFontHeight(getTextSize()) + getCompoundDrawablePadding();
            // 如果“换一换”三个字宽度超出，则改用一个"换"字
            if (textWidth + mHorizontalPadding * 2 > availableWidth) {
                textWidth = mPaint.measureText("换");
                if (textWidth + mHorizontalPadding * 2 > availableWidth) {
                    // 一个"换"字也超出就不显示字符,并把 DrawablePadding 清零让 Icon 居中
                    setText("");
                    mTagText = "";
                    textWidth = 0;
                    setCompoundDrawablePadding(0);
                } else {
                    setText("换");
                    mTagText = "换";
                }
            }
        }
        // 如果可用宽度不够用，则做裁剪处理，末尾不3个.
        else if (textWidth + mHorizontalPadding * 2 > availableWidth) {
            float pointWidth = mPaint.measureText(".");
            // 计算能显示的字体长度
            float maxTextWidth = availableWidth - mHorizontalPadding * 2 - pointWidth * 3;
            float tmpWidth = 0;
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < mTagText.length(); i++) {
                char c = mTagText.charAt(i);
                float cWidth = mPaint.measureText(String.valueOf(c));
                // 计算每个字符的宽度之和，如果超过能显示的长度则退出
                if (tmpWidth + cWidth > maxTextWidth) {
                    break;
                }
                strBuilder.append(c);
                tmpWidth += cWidth;
            }
            // 末尾添加3个.并设置为显示字符
            strBuilder.append("...");
            setText(strBuilder.toString());
            textWidth = mPaint.measureText(strBuilder.toString());
        }

        _initIcon(textWidth);
    }

    /**
     * ==================================== 开放接口 ====================================
     */

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
        mBorderWidth = borderWidth;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    public CharSequence getTagText() {
        return mTagText;
    }

    public void setTagText(CharSequence tagText) {
        mTagText = tagText;
        setText(tagText);
        mIsAdjusted = false;
    }

    public boolean isPressFeedback() {
        return mIsPressFeedback;
    }

    public void setPressFeedback(boolean pressFeedback) {
        mIsPressFeedback = pressFeedback;
        if (mIsPressFeedback) {
            mOriTextColor = getCurrentTextColor();
        }
    }

    public void updateTagColor(int color) {
        mOriTextColor = color;
    }

    /**
     * ==================================== 点击监听 ====================================
     */

    public OnTagClickListener getTagClickListener() {
        return mTagClickListener;
    }

    public void setTagClickListener(OnTagClickListener tagClickListener) {
        mTagClickListener = tagClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsPressFeedback) {
            return super.onTouchEvent(event);
        }
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mIsTagPress = true;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsTagPress && !_isViewUnder(event.getX(), event.getY())) {
                    mIsTagPress = false;
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsTagPress) {
                    mIsTagPress = false;
                    postInvalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断是否在 Tag 控件内
     *
     * @param x
     * @param y
     * @return
     */
    private boolean _isViewUnder(float x, float y) {
        return x >= 0 && x < getWidth() &&
                y >= 0 && y < getHeight();
    }

    /**
     * 点击监听器
     */
    public interface OnTagClickListener {
        void onTagClick(String text, @TagMode int tagMode);

        void onTagLongClick(String text, @TagMode int tagMode);
    }

    /**
     * ==================================== 显示模式 ====================================
     */

    // 3种外形模式：圆角矩形、圆弧、直角矩形
    public final static int SHAPE_ROUND_RECT = 101;
    public final static int SHAPE_ARC = 102;
    public final static int SHAPE_RECT = 103;
    // 3种类型模式：正常、编辑、换一换
    public final static int MODE_NORMAL = 201;
    public final static int MODE_EDIT = 202;
    public final static int MODE_CHANGE = 203;

    // 显示外形
    private int mTagShape = SHAPE_ROUND_RECT;
    // 显示类型
    private int mTagMode = MODE_NORMAL;
    // 装饰的icon
    private RotateDrawable mDecorateIcon;


    public int getTagShape() {
        return mTagShape;
    }

    public void setTagShape(@TagShape int tagShape) {
        mTagShape = tagShape;
    }

    public int getTagMode() {
        return mTagMode;
    }

    public void setTagMode(@TagMode int tagMode) {
        mTagMode = tagMode;
    }

    /**
     * 初始化 ICON
     *
     * @param textWidth
     */
    private void _initIcon(float textWidth) {
        if (mTagMode == MODE_CHANGE) {
            int size = MeasureUtils.getFontHeight(getTextSize());
            int left = 0;
            if (mFitWidth != INVALID_VALUE) {
                int drawablePadding = getCompoundDrawablePadding();
                left = (int) ((getMeasuredWidth() - textWidth - size) / 2) - mHorizontalPadding - drawablePadding / 2;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_change);
            mDecorateIcon = new RotateDrawable(bitmap, left);
            mDecorateIcon.setBounds(left, 0, size + left, size);
            mDecorateIcon.setColorFilter(getCurrentTextColor(), PorterDuff.Mode.SRC_IN);
            setCompoundDrawables(mDecorateIcon, null, null, null);
        }
    }

    /**
     * 设置 icon 颜色
     *
     * @param color
     */
    private void _setIconColor(int color) {
        if (mTagMode == MODE_CHANGE && mDecorateIcon != null) {
            mDecorateIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @IntDef({SHAPE_ROUND_RECT, SHAPE_ARC, SHAPE_RECT})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface TagShape {
    }

    @IntDef({MODE_NORMAL, MODE_EDIT, MODE_CHANGE})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface TagMode {
    }
}
