package com.example.myronlg.swipedialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by myron.lg on 2015/8/12.
 */
public class SwipeDialogNew extends Dialog {

    enum SwipeType {DISMISS, CANCEL}

    private DialogContainerFrameLayout container;
    private ViewGroup.LayoutParams layoutParams;

    public SwipeDialogNew(Context context) {
        super(context);
        init();
    }

    public SwipeDialogNew(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected SwipeDialogNew(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        final Window window = getWindow();
//        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
//        windowParams.x = 0;
//        windowParams.y = 0;
//        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        windowParams.flags = windowParams.flags & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED & ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        windowParams.format = PixelFormat.TRANSLUCENT;
//        windowParams.horizontalMargin = 0;
//        windowParams.verticalMargin = 0;
//        windowParams.horizontalWeight = 1.0F;
//        windowParams.verticalWeight = 1.0F;

//        window.setAttributes(windowParams);

        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        container = new DialogContainerFrameLayout(getContext());
//        container.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        container.setSwipeListener(new SwipeListener() {
            @Override
            public void onSwipe(SwipeType swipeType) {
                switch (swipeType){
                    case DISMISS:
                        SwipeDialogNew.super.dismiss();
                        break;
                    case CANCEL:
                        SwipeDialogNew.super.cancel();
                        break;
                }

            }
        });

        layoutParams = new ViewGroup.LayoutParams(getWidth(), getHeight());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        View dialogView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        setContentView(dialogView, null);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        container.addDialogView(view);
        super.setContentView(container, layoutParams);
    }

    @Override
    public void show() {
        super.show();
        container.show();
    }

    @Override
    public void dismiss() {
        container.dismiss();
    }

    @Override
    public void cancel() {
        container.cancel();
    }

    public int getWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public int getHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels - getStatusBarHeight();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public interface SwipeListener {
        void onSwipe(SwipeType swipeType);
    }
}
