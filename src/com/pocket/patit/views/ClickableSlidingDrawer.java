package com.pocket.patit.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SlidingDrawer;

/**
 * {@link ClickableSlidingDrawer}
 * 
 * Adaptador de la cremallera
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class ClickableSlidingDrawer extends SlidingDrawer {

    private ViewGroup mHandleLayout;
    private final Rect mHitRect = new Rect();

    public ClickableSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ClickableSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View handle = getHandle();

        if (handle instanceof ViewGroup) {
            mHandleLayout = (ViewGroup) handle;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mHandleLayout != null) {
            int childCount = mHandleLayout.getChildCount();
            int handleClickX = (int)(event.getX() - mHandleLayout.getX());
            int handleClickY = (int)(event.getY() - mHandleLayout.getY());

            Rect hitRect = mHitRect;

            for (int i=0;i<childCount;i++) {
                View childView = mHandleLayout.getChildAt(i);
                childView.getHitRect(hitRect);

                if (hitRect.contains(handleClickX, handleClickY)) {
                    return false;
                }
            }
        }

        return super.onInterceptTouchEvent(event);
    }
}
