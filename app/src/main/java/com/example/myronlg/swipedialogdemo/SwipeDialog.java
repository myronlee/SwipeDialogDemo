package com.example.myronlg.swipedialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by myron.lg on 2015/8/12.
 */
public class SwipeDialog extends Dialog {

    private DialogContainer container;
    private boolean cancel;

    public SwipeDialog(Context context) {
        super(context);
        init();
    }

    public SwipeDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected SwipeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        final Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = 0;

        container = new DialogContainer(getContext());
        container.setSwipeListener(new DialogContainer.SwipeListener() {
            @Override
            public void onFallIn() {
            }

            @Override
            public void onRiseOut() {
                if (cancel) {
                    SwipeDialog.super.cancel();
                    cancel = false;
                } else {
                    SwipeDialog.super.dismiss();
                }
            }

            @Override
            public void onFallOut() {
                SwipeDialog.super.dismiss();
            }

            @Override
            public void onRecover() {
            }
        });
    }


    @Override
    public void setContentView(int layoutResID) {
        View dialogView = LayoutInflater.from(getContext()).inflate(layoutResID, container, false);
//        container.setDialogView(dialogView);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getWidth(), getHeight());
//        super.setContentView(container, layoutParams);
        setContentView(dialogView, null);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        container.removeAllViews();
        container.addDialogView(view);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getWidth(), getHeight());
        super.setContentView(container, layoutParams);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        container.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        super.show();
        container.show();
    }

    @Override
    public void dismiss() {
        container.riseOut();
    }

    @Override
    public void cancel() {
        cancel = true;
        container.riseOut();
    }

    public void setChangeDimEnabled(boolean changeDimEnabled) {
        container.setChangeDimEnabled(changeDimEnabled);
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
}
