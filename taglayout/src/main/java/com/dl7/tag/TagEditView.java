package com.dl7.tag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dl7.tag.utils.MeasureUtils;

import static com.dl7.tag.TagView.INVALID_VALUE;
import static com.dl7.tag.TagView.SHAPE_ARC;
import static com.dl7.tag.TagView.SHAPE_RECT;
import static com.dl7.tag.TagView.SHAPE_ROUND_RECT;

/**
 * Created by long on 2017/4/21.
 */

public class TagEditView extends TextView {

    private Paint mBorderPaint;
    // 虚线路径
    private PathEffect mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);
    // 边框矩形
    private RectF mRect;
    // 边框大小
    private float mBorderWidth;
    // 显示外形
    private int mTagShape = SHAPE_ROUND_RECT;
    // 边框角半径
    private float mRadius;
    // 边框颜色
    private int mBorderColor;
    // 字体水平空隙
    private int mHorizontalPadding;
    // 字体垂直空隙
    private int mVerticalPadding;

    public TagEditView(Context context) {
        super(context);
        _init(context);
    }

    public TagEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    private void _init(Context context) {
        mHorizontalPadding = (int) MeasureUtils.dp2px(context, 5f);
        mVerticalPadding = (int) MeasureUtils.dp2px(context, 5f);
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        mRect = new RectF();
        mBorderColor = Color.parseColor("#ff333333");
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderWidth = MeasureUtils.dp2px(context, 0.5f);
        mRadius = MeasureUtils.dp2px(context, 5f);
        // 设置字体占中
        setGravity(Gravity.CENTER);
        _initEditMode();
    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置矩形边框
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewParent parent = getParent();
        if (parent instanceof TagLayout) {
            int fitTagNum = ((TagLayout) getParent()).getFitTagNum();
            if (fitTagNum != INVALID_VALUE) {
                int availableWidth = ((TagLayout) getParent()).getAvailableWidth();
                int horizontalInterval = ((TagLayout) getParent()).getHorizontalInterval();
                int fitWidth = (availableWidth - (fitTagNum - 1) * horizontalInterval) / fitTagNum;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(fitWidth, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = mRadius;
        if (mTagShape == SHAPE_ARC) {
            radius = mRect.height() / 2;
        } else if (mTagShape == SHAPE_RECT) {
            radius = 0;
        }
        canvas.drawRoundRect(mRect, radius, radius, mBorderPaint);
        super.onDraw(canvas);
    }

    /**
     * 初始化编辑模式
     */
    private void _initEditMode() {
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setHint("添加标签");
        mBorderPaint.setPathEffect(mPathEffect);
        setHintTextColor(Color.parseColor("#ffaaaaaa"));
        setMovementMethod(ArrowKeyMovementMethod.getInstance());
        requestFocus();
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (!TextUtils.isEmpty(getText())) {
                        ((TagLayout) getParent()).addTag(getText().toString());
                        setText("");
                        _closeSoftInput();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 离开编辑模式
     */
    public void exitEditMode() {
        clearFocus();
        setFocusable(false);
        setFocusableInTouchMode(false);
        setHint(null);
        setMovementMethod(null);
        _closeSoftInput();
    }

    /**
     * 关闭软键盘
     */
    private void _closeSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果软键盘已经开启
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ==================================== 开放接口 ====================================
     */

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        setHintTextColor(color);
    }

    /**
     * 调用这些接口进行属性设置如果最后可能会改变按钮的大小的话最后调用一下这个接口，以刷新界面，建议属性直接在布局里设置
     * 只需要回调onDraw()的话调用invalidate()就可以了
     */
    public void updateView() {
        requestLayout();
        invalidate();
    }
}
