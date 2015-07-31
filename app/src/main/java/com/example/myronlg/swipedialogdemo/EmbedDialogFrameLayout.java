package com.example.myronlg.swipedialogdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by myron.lg on 2015/7/30.
 */
public class EmbedDialogFrameLayout extends FrameLayout {

    private static final long FALL_DURATION = 800;
    private static final float DIM_RATIO = 0.8F;
    private float currentDim;
    private View dialogView;
    private View dimView;
    private int dialogTop;
    private int dialogHeight;
    private float downY;
    private int swipTopBoundary;
    private int swipBottomBoundary;
    private boolean animating;
    private SwipeDialogManager.RemoveDialogListener removeDialogListener;

    public EmbedDialogFrameLayout(Context context) {
        super(context);
    }

    public EmbedDialogFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmbedDialogFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addDialogView(int layout) {
//        dimView = new View(getContext());
//        dimView.setBackgroundColor(Color.BLACK);
//        dimView.setVisibility(INVISIBLE);
//        addView(dimView);

        dialogView = LayoutInflater.from(getContext()).inflate(layout, this, false);
        dialogView.setVisibility(INVISIBLE);
//        dialogView.setAlpha(0);
        FrameLayout.LayoutParams params = (LayoutParams) dialogView.getLayoutParams();
//        params.width = LayoutParams.WRAP_CONTENT;
//        params.height = LayoutParams.WRAP_CONTENT;
//        params.gravity = Gravity.CENTER;
        addView(dialogView, params);


    }

    public void show(){
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                dialogTop = dialogView.getTop();
                dialogHeight = dialogView.getHeight();
                swipTopBoundary = -(dialogView.getTop() + dialogView.getBottom()) / 2;
//                swipBottomBoundary = getHeight()-dimView.getBottom();
                swipBottomBoundary = -swipTopBoundary;
                fallIn();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        invalidate();
    }

    private void fallIn() {
        dialogView.setTranslationY(-dialogTop - dialogHeight);
        dialogView.setVisibility(VISIBLE);
//        dialogView.setAlpha(1);
//        dimView.setVisibility(VISIBLE);
//        dimView.setAlpha(0.8F);
//        setBackgroundColor(Color.parseColor("#88000000"));
        ValueAnimator fall = ValueAnimator.ofInt(-dialogTop - dialogHeight, 0);
        fall.setDuration(FALL_DURATION);
        fall.setInterpolator(new DecelerateInterpolator(1.6F));
        fall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                dialogView.setAlpha(valueAnimator.getAnimatedFraction() * 2);
                dialogView.setTranslationY((Integer) valueAnimator.getAnimatedValue());
//                dimView.setAlpha(valueAnimator.getAnimatedFraction() * DIM_RATIO);
                setBackgroundColor(Color.argb((int) (255 * valueAnimator.getAnimatedFraction() * DIM_RATIO), 0, 0, 0));
            }
        });
        fall.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                return onDown(ev);
            case MotionEvent.ACTION_MOVE:
                return onMove(ev);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return onEnd(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean onDown(MotionEvent ev) {
        downY = ev.getY();
        return super.dispatchTouchEvent(ev);
    }

    private boolean onMove(MotionEvent ev) {
        float y = ev.getY();
        float deltaY = y - downY;
        dialogView.setTranslationY(deltaY);
        float newDim = DIM_RATIO * (1 - Math.min(1, Math.abs(deltaY / swipTopBoundary)));
        setDim(newDim);
        return super.dispatchTouchEvent(ev);
    }

    private boolean onEnd(MotionEvent ev) {
        if (dialogView.getTranslationY() < swipTopBoundary){//[-infinite, -dialogTop)
            riseOut();
        } else if (dialogView.getTranslationY() < swipBottomBoundary){//[-dialog, bottom)
            recover();
        } else {
            fallOut();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void recover() {
        animate(0, DIM_RATIO, false);
    }

    private void riseOut() {
        animate(-dialogTop - dialogHeight, 0, true);
    }

    private void fallOut() {
        animate(getHeight() - dialogTop, 0, true);
    }

    private void animate(int endTranslationY, float endDim, final boolean dismiss) {
        final float startDim = currentDim;
        final float dimDelta = endDim - startDim;
        ValueAnimator animator = ValueAnimator.ofFloat(dialogView.getTranslationY(), endTranslationY);
        animator.setDuration(FALL_DURATION);
        animator.setInterpolator(new DecelerateInterpolator(1.6F));
//        if (dismiss){
//            animator.setInterpolator(new AccelerateInterpolator(1.6F));
//        } else {
//            animator.setInterpolator(new DecelerateInterpolator(1.6F));
//        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                dialogView.setAlpha(valueAnimator.getAnimatedFraction() * 2);
                dialogView.setTranslationY((Float) valueAnimator.getAnimatedValue());
//                dimView.setAlpha(valueAnimator.getAnimatedFraction() * DIM_RATIO);
//                setBackgroundColor(Color.argb((int) (255 * valueAnimator.getAnimatedFraction() * DIM_RATIO), 0, 0, 0));
                setDim(startDim + dimDelta * valueAnimator.getAnimatedFraction());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animating = false;
                if (dismiss) {
                    if (removeDialogListener != null){
                        removeDialogListener.removeDialog();
                    }
                    if (onDismissListener != null) {
                        onDismissListener.onDismiss(null);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();

    }

    private void setDim(float dim){
        setBackgroundColor(Color.argb((int) (255 * dim), 0, 0, 0));
        currentDim = dim;
    }

    private Dialog.OnDismissListener onDismissListener;

    public void setOnDismissListener(Dialog.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setRemoveDialogListener(SwipeDialogManager.RemoveDialogListener removeDialogListener) {
        this.removeDialogListener = removeDialogListener;
    }
}
