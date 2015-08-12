package com.example.myronlg.swipedialogdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by myron.lg on 2015/7/30.
 */
public class DialogContainerFrameLayout extends FrameLayout {

    private static final float DIM_RATIO = 0.8F;
    private float currentDim;

    private View dialogView;

    private float downY;
    private int translationYTopBoundary;
    private int translationYBottomBoundary;
    private int translationYMax;

    private boolean animating;
    private boolean intercept;

    private int touchSlop;
    private int flingVelocityThreshold;
    private VelocityTracker velocityTracker;

    private SwipeDialogNew.SwipeListener swipeListener;

    public DialogContainerFrameLayout(Context context) {
        super(context);
        init();
    }

    public DialogContainerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialogContainerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        int minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        int maxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        flingVelocityThreshold = (minFlingVelocity + maxFlingVelocity) / 5;
        touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public void addDialogView(View dialogView) {
        this.dialogView = dialogView;
        dialogView.setVisibility(INVISIBLE);
        FrameLayout.LayoutParams params = (LayoutParams) dialogView.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
        }
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        addView(dialogView, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        velocityTracker.clear();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (animating) {
            return false;
        }
        switch (ev.getAction()) {
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
        velocityTracker.clear();
        velocityTracker.addMovement(ev);

        downY = ev.getY();
        super.dispatchTouchEvent(ev);
        return true;
    }

    private boolean onMove(MotionEvent ev) {
        velocityTracker.addMovement(ev);

        if (downY == -1) {
            downY = ev.getY();
        }

        float dy = ev.getY() - downY;

        dialogView.setTranslationY(dy);
        float newDim = DIM_RATIO * (1 - Math.min(1, Math.abs(dy / translationYMax)));
        setDim(newDim);

        if (intercept) {
            return true;
        }
        if (Math.abs(dy) > touchSlop) {
            dispatchCancelEvent(ev);
            intercept = true;
            return true;
        }

        return super.dispatchTouchEvent(ev);

    }

    private boolean onEnd(MotionEvent ev) {
        velocityTracker.addMovement(ev);
        velocityTracker.computeCurrentVelocity(1000);
        float velocityY = velocityTracker.getYVelocity();
        if (Math.abs(velocityY) < flingVelocityThreshold) {
            if (dialogView.getTranslationY() < translationYTopBoundary) {//[-infinite, -dialogTop)
                riseOut(SwipeDialogNew.SwipeType.DISMISS);
            } else if (dialogView.getTranslationY() < translationYBottomBoundary) {//[-dialog, bottom)
                recover();
            } else {
                fallOut();
            }
        } else {
            if (intercept) {
                if (velocityY < 0) {
                    riseOut(SwipeDialogNew.SwipeType.DISMISS);
                } else {
                    fallOut();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dispatchCancelEvent(MotionEvent ev) {
        MotionEvent cancelEvent = MotionEvent.obtain(ev);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(cancelEvent);
        cancelEvent.recycle();
    }

    private void fallIn() {
        dialogView.setTranslationY(-dialogView.getBottom());
        dialogView.setVisibility(VISIBLE);
        animate(0, DIM_RATIO, false, SwipeDialogNew.SwipeType.DISMISS);
    }

    private void recover() {
        animate(0, DIM_RATIO, false, null);
    }

    private void riseOut(SwipeDialogNew.SwipeType swipeType) {
        animate(-dialogView.getBottom(), 0, true, swipeType);
    }

    private void fallOut() {
        animate(getHeight() - dialogView.getTop(), 0, true, SwipeDialogNew.SwipeType.DISMISS);
    }

    private void animate(float endTranslationY, float endDim, final boolean dismiss, final SwipeDialogNew.SwipeType swipeType) {
        float translationYDelta = endTranslationY - dialogView.getTranslationY();
        long duration = Math.min(Math.max((long) (Math.abs(translationYDelta) * 0.7F), 400), 600);
        final float startDim = currentDim;
        final float dimDelta = endDim - startDim;
        ValueAnimator animator = ValueAnimator.ofFloat(dialogView.getTranslationY(), endTranslationY);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator(1.6F));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                dialogView.setTranslationY((Float) valueAnimator.getAnimatedValue());
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
                downY = -1;
                intercept = false;
                if (dismiss && swipeListener != null && swipeType != null) {
                    swipeListener.onSwipe(swipeType);
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

    private void setDim(float dim) {
        setBackgroundColor(Color.argb((int) (255 * dim), 0, 0, 0));
        currentDim = dim;
    }

    public void show() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                translationYTopBoundary = -(DialogContainerFrameLayout.this.dialogView.getTop() + DialogContainerFrameLayout.this.dialogView.getBottom()) / 2;
                translationYBottomBoundary = -translationYTopBoundary;
                translationYMax = DialogContainerFrameLayout.this.dialogView.getBottom();
                fallIn();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        invalidate();
    }

    public void dismiss() {
        riseOut(SwipeDialogNew.SwipeType.DISMISS);
    }

    public void cancel() {
        riseOut(SwipeDialogNew.SwipeType.CANCEL);
    }

    public void setSwipeListener(SwipeDialogNew.SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

}
