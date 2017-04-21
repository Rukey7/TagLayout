package com.dl7.tag.drawable;

import android.graphics.drawable.Drawable;

/**
 * Created by long on 2017/4/21.
 */

public abstract class SimpleCallBack implements Drawable.Callback {

    @Override
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
    }

    @Override
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
    }
}
