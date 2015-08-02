package com.example.myronlg.swipedialogdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by myron.lg on 2015/7/31.
 */
public class SwipeDialogManager{
    private Context context;
    private WindowManager windowManager;
    private EmbedDialogFrameLayout dialogContainer;

    public SwipeDialogManager(Context context) {
        this.context = context;
        this.windowManager = (WindowManager) context.getSystemService("window");
        dialogContainer = new EmbedDialogFrameLayout(context);
    }

    /**
     * deprecated
     * @param layout
     */
    public void addDialogView(int layout) {
        dialogContainer = new EmbedDialogFrameLayout(context);
        dialogContainer.addDialogView(layout);
        dialogContainer.setRemoveDialogListener(new RemoveDialogListener(){

            @Override
            public void removeDialog() {
                windowManager.removeView(dialogContainer);
            }
        });

        addContainerToWindowManager();
    }

    public void addDialogView(View view) {
        addViewToContainer(view);

        addContainerToWindowManager();
    }

    private void addViewToContainer(View view) {
        dialogContainer.addDialogView(view);
        dialogContainer.setRemoveDialogListener(new RemoveDialogListener() {

            @Override
            public void removeDialog() {
                windowManager.removeView(dialogContainer);
            }
        });
    }

    private void addContainerToWindowManager() {
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.x = 0;
        windowParams.y = 0;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
//        windowParams.dimAmount = 0.0F;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowManager.addView(dialogContainer, windowParams);
    }


    public void show(){
        dialogContainer.show();
    }

    public interface RemoveDialogListener {
        void removeDialog();
    }
}
