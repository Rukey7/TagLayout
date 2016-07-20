package com.dl7.taglibrary.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Rukey7 on 2016/7/18.
 */
public class MeasureUtils {

    private MeasureUtils() {
    }


    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

}
