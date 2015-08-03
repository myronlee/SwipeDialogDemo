package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by myron.lg on 2015/7/31.
 */
public class SwipeDialogManager{
    private Context context;
    private WindowManager windowManager;
    private EmbedDialogFrameLayout dialogViewContainer;

    public SwipeDialogManager(Context context) {
        this.context = context;
        this.windowManager = (WindowManager) context.getSystemService("window");
        dialogViewContainer = new EmbedDialogFrameLayout(context);
    }

    /**
     * deprecated
     * @param layout
     */
    public void addDialogView(int layout) {
        dialogViewContainer = new EmbedDialogFrameLayout(context);
        dialogViewContainer.addDialogView(layout);
        dialogViewContainer.setRemoveDialogListener(new RemoveDialogListener() {

            @Override
            public void removeDialog() {
                windowManager.removeView(dialogViewContainer);
            }
        });

        addContainerToWindowManager();
    }

    public void addDialogView(View view) {
        addViewToContainer(view);
        addContainerToWindowManager();
    }

    private void addViewToContainer(View view) {
        dialogViewContainer.addDialogView(view);
        dialogViewContainer.setRemoveDialogListener(new RemoveDialogListener() {

            @Override
            public void removeDialog() {
                windowManager.removeView(dialogViewContainer);
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
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowManager.addView(dialogViewContainer, windowParams);
    }


    public void show(){
        dialogViewContainer.show();
    }

    public interface RemoveDialogListener {
        void removeDialog();
    }
}
