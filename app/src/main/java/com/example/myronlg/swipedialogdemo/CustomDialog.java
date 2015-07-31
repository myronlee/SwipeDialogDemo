package com.example.myronlg.swipedialogdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.util.zip.Inflater;

import javax.security.auth.login.LoginException;

/**
 * Created by myron.lg on 2015/7/15.
 */
public class CustomDialog extends Dialog implements View.OnTouchListener {

    private static final long DURATION = 400;
    private int layoutResID;
    private View rootView;
    private Window window;

    private ValueAnimator currentAnimator;

    private float downY = -1;
    private int windowOriginalY = -1;

    public CustomDialog(Context context) {
        super(context);
        init();
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window dialogWindow = getWindow();
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = 720;
        params.height = 1200;
        dialogWindow.setAttributes(params);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.dialog, (ViewGroup) getWindow().getDecorView(), true);

        rootView = findViewById(R.id.girl);
        rootView.setTranslationY(500);
    }
/*

    private void init() {
        window = getWindow();
        setWindowStyle();
//        setWindowY(2000);
        LayoutInflater.from(getContext()).inflate(R.layout.dialog, (ViewGroup) getWindow().getDecorView(), true);
//        setContentView(R.layout.dialog);
//        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = findViewById(R.id.girl);
        rootView.setTranslationY(500);
//        rootView.setOnTouchListener(this);
//        rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                rootView.getViewTreeObserver().removeOnPreDrawListener(this);
//                windowOriginalY = getWindow().getAttributes().y;
//                return true;
//            }
//        });
    }

*/
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            windowOriginalY = getWindow().getAttributes().y;
//        }
//    }
//
//    @Override
//    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
//        super.onWindowAttributesChanged(params);
//        if (params.y != 0 && windowOriginalY != -1){
//            windowOriginalY = params.y;
//        }
//    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

//        View decorView = window.getDecorView();
//        View contentView = decorView.findViewById(android.R.id.content);
//        View rootView = contentView.findViewById(R.id.root);
//
//        int x = 100;

        if (windowOriginalY == -1){
            windowOriginalY = window.getAttributes().y;
            Log.e("", ""+windowOriginalY);
        }
        if (isAnimating()) {
            return true;
        }
        if (downY == -1) {
            downY = motionEvent.getY();
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downY = motionEvent.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float deltaY = motionEvent.getY() - downY;
                moveWindow(deltaY);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (shouldDismiss()) {
                    slideDismiss();
                } else {
                    slideBack();
                }
                break;
            }
        }

        return true;
    }

    private void reset() {
        downY = -1;
    }

    private void slideBack() {
        int begY = window.getAttributes().y;
        int endY = windowOriginalY;
        currentAnimator = ValueAnimator.ofInt(begY, endY);
        currentAnimator.setDuration(DURATION);
        currentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setWindowY((Integer) valueAnimator.getAnimatedValue());
            }
        });
        currentAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                currentAnimator = null;
                reset();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        currentAnimator.start();
    }

    private void slideDismiss() {
        int begY = window.getAttributes().y;
        int endY = - window.getAttributes().height;
        currentAnimator = ValueAnimator.ofInt(begY, endY);
        currentAnimator.setDuration(DURATION);
        currentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setWindowY((Integer) valueAnimator.getAnimatedValue());
            }
        });
        currentAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                currentAnimator = null;
                reset();
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        currentAnimator.start();
    }


    private boolean shouldDismiss() {
        return window.getAttributes().y < 0;
    }

    private void moveWindow(float deltaY) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = windowOriginalY + Math.round(deltaY);
        window.setAttributes(params);
    }

    private void setWindowY(int y){
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = y;
        window.setAttributes(params);
    }

    private void setWindowStyle() {
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 720;
        params.height = 1200;
        window.setAttributes(params);
    }

    public void setLayoutResID(int layoutResID) {
        this.layoutResID = layoutResID;
    }

    private boolean isAnimating() {
        return currentAnimator != null;
    }
}
