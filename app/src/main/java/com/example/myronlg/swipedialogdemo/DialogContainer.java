package com.example.myronlg.swipedialogdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by myron.lg on 2015/7/30.
 */
public class DialogContainer extends FrameLayout {

    private boolean handleByChild;
    private View targetView;


    private enum AnimateType {FALL_IN, RECOVER, RISE_OUT, FALL_OUT}

    private static final float MAX_DIM = 0.5F;
    private float currDim;
    private boolean changeDimEnabled = true;

    private boolean canceledOnTouchOutside;
    private boolean touchOutside;

    private View dialogView;

    private float downY;
    private float lastY;
    private int translationYTopBoundary;
    private int translationYBottomBoundary;
    private int translationYMax;

    private boolean animating;
    private boolean handleBySelf;

    private int touchSlop;
    private int flingVelocityThreshold;
    private VelocityTracker velocityTracker;

    private SwipeListener swipeListener;

    public DialogContainer(Context context) {
        super(context);
        init();
    }

    public DialogContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialogContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        int minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        int maxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        flingVelocityThreshold = (minFlingVelocity + maxFlingVelocity) / 5;
        touchSlop = viewConfiguration.getScaledTouchSlop();

        setVisibility(INVISIBLE);

    }

    public void addDialogView(View dialogView) {
        this.dialogView = dialogView;
        FrameLayout.LayoutParams params = (LayoutParams) dialogView.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
        }
        if (params.width == LayoutParams.MATCH_PARENT) {
            params.width = LayoutParams.WRAP_CONTENT;
        }
        if (params.height == LayoutParams.MATCH_PARENT) {
            params.height = LayoutParams.WRAP_CONTENT;
        }
        params.gravity = Gravity.CENTER;
        addView(dialogView, params);
    }


    public void show() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                translationYTopBoundary = -(DialogContainer.this.dialogView.getTop() + DialogContainer.this.dialogView.getBottom()) / 2;
                translationYBottomBoundary = -translationYTopBoundary;
                translationYMax = DialogContainer.this.dialogView.getBottom();

                dialogView.setTranslationY(-dialogView.getBottom());
                if (!changeDimEnabled) {
                    setDim(MAX_DIM);
                }
                setVisibility(VISIBLE);

                fallIn();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        invalidate();
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

        Rect dialogHitRect = new Rect();
        dialogView.getHitRect(dialogHitRect);
        touchOutside = !dialogHitRect.contains(((int) ev.getX()), ((int) ev.getY()));

        targetView = getTargetView(this, MotionEvent.obtain(ev));

        downY = ev.getY();
        lastY = ev.getY();
        super.dispatchTouchEvent(ev);
        return true;
    }

    private boolean onMove(MotionEvent ev) {
        velocityTracker.addMovement(ev);

        if (downY == -1) {
            downY = ev.getY();
        }

        if (lastY == -1) {
            lastY = ev.getY();
        }

        final float dy = ev.getY() - downY;

        if (handleBySelf) {
            dialogView.setTranslationY(dy);
            if (changeDimEnabled) {
                float newDim = MAX_DIM * (1 - Math.min(1, Math.abs(dy / translationYMax)));
                setDim(newDim);
            }
            return true;
        }

        if (handleByChild) {
            super.dispatchTouchEvent(ev);
            return true;
        }

        if (Math.abs(dy) >= touchSlop) {
            if (touchOutside || !targetViewCanScroll(ev.getY() - lastY)) {
                handleBySelf = true;
                handleByChild = false;
                downY = ev.getY();
                dispatchCancelEventToChild(ev);
                return true;
            } else {
                handleByChild = true;
                handleBySelf = false;
                super.dispatchTouchEvent(ev);
                return true;
            }
        }

        lastY = ev.getY();
        super.dispatchTouchEvent(ev);
        return true;
    }

    private boolean onEnd(MotionEvent ev) {
        velocityTracker.addMovement(ev);
        velocityTracker.computeCurrentVelocity(1000);
        float velocityY = velocityTracker.getYVelocity();
        if (handleBySelf) {
            if (Math.abs(velocityY) < flingVelocityThreshold) {
                if (dialogView.getTranslationY() < translationYTopBoundary) {//[-infinite, -dialogTop)
                    riseOut();
                } else if (dialogView.getTranslationY() < translationYBottomBoundary) {//[-dialog, bottom)
                    recover();
                } else {
                    fallOut();
                }
            } else {

                if (velocityY < 0) {
                    riseOut();
                } else {
                    fallOut();
                }
            }
        }
        reset();
        return super.dispatchTouchEvent(ev);
    }


    private void reset() {
        downY = -1;
        lastY = -1;
        handleBySelf = false;
        handleByChild = false;
    }


    private boolean targetViewCanScroll(float dy) {
        if (targetView instanceof AdapterView<?>) {
            AdapterView<?> adapterView = (AdapterView<?>) targetView;
            if (adapterView.getCount() == 0
                    || (adapterView.getLastVisiblePosition() == adapterView.getCount() - 1
                    && adapterView.getChildAt(adapterView.getChildCount() - 1).getBottom() <= adapterView.getHeight()
                    && dy < 0)
                    || (adapterView.getFirstVisiblePosition() == 0
                    && adapterView.getChildAt(0).getTop() >= 0)
                    && dy > 0) {
                return false;
            } else {
                return true;
            }
        }

        if ((targetView instanceof ScrollView)
                && ((ScrollView) targetView).getChildCount() > 0
                && ((ScrollView) targetView).getChildAt(0).getMeasuredHeight() > targetView.getHeight()
                && !((targetView.getScrollY() <= 0 && dy > 0) || (targetView.getScrollY() >= ((ScrollView) targetView).getChildAt(0).getMeasuredHeight() - targetView.getHeight() && dy < 0))) {
            return true;
        }

        if (targetView instanceof TextView
                && ((TextView) targetView).getMovementMethod() != null
                && ((TextView) targetView).getLayout().getHeight() > targetView.getHeight()
                && !((targetView.getScrollY() <= 0 && dy > 0) || (targetView.getScrollY() >= ((TextView) targetView).getLayout().getHeight() - targetView.getHeight() && dy < 0))) {
            return true;
        }

        return false;
    }

    private View getTargetView(View view, MotionEvent event) {
        if (view instanceof AdapterView<?> || view instanceof ScrollView) {
            event.recycle();
            return view;
        } else if (view instanceof ViewGroup) {
            event.offsetLocation(-view.getLeft(), -view.getTop());//do not swap them, tears
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                Rect childHitRect = new Rect();
                child.getHitRect(childHitRect);
                if (childHitRect.contains(((int) event.getX()), ((int) event.getY()))) {
//                    event.offsetLocation(-child.getTop(), -child.getLeft());
//                    event.transform(child.getInverseMatrix());
                    return getTargetView(child, event);
                }
            }
            event.recycle();
            return view;
        } else {
            event.recycle();
            return view;
        }
    }

    private void dispatchDownEventToChild(MotionEvent ev) {
        MotionEvent cancelEvent = MotionEvent.obtain(ev);
        cancelEvent.setAction(MotionEvent.ACTION_DOWN);
        super.dispatchTouchEvent(cancelEvent);
        cancelEvent.recycle();
    }

    private void dispatchCancelEventToChild(MotionEvent ev) {
        MotionEvent cancelEvent = MotionEvent.obtain(ev);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(cancelEvent);
        cancelEvent.recycle();
    }

    public void fallIn() {
        animate(0, MAX_DIM, AnimateType.FALL_IN);
    }

    private void recover() {
        animate(0, MAX_DIM, AnimateType.RECOVER);
    }

    public void riseOut() {
        animate(-dialogView.getBottom(), 0, AnimateType.RISE_OUT);
    }

    private void fallOut() {
        animate(getHeight() - dialogView.getTop(), 0, AnimateType.FALL_OUT);
    }

    private void animate(float endTranslationY, float endDim, final AnimateType animateType) {
        float translationYDelta = endTranslationY - dialogView.getTranslationY();
        long duration = Math.min(Math.max((long) (Math.abs(translationYDelta) * 0.7F), 400), 600);
        final float startDim = currDim;
        final float dimDelta = endDim - startDim;
        ValueAnimator animator = ValueAnimator.ofFloat(dialogView.getTranslationY(), endTranslationY);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator(1.6F));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                dialogView.setTranslationY((Float) valueAnimator.getAnimatedValue());
                if (changeDimEnabled) {
                    setDim(startDim + dimDelta * valueAnimator.getAnimatedFraction());
                }
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
                if (swipeListener != null) {
                    switch (animateType) {
                        case FALL_IN:
                            swipeListener.onFallIn();
                            break;
                        case RECOVER:
                            swipeListener.onRecover();
                            break;
                        case RISE_OUT:
                            swipeListener.onRiseOut();
                            break;
                        case FALL_OUT:
                            swipeListener.onFallOut();
                            break;
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

    private void setDim(float dim) {
        setBackgroundColor(Color.argb((int) (255 * dim), 0, 0, 0));
        currDim = dim;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    public void setSwipeListener(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    public interface SwipeListener {
        void onFallIn();

        void onRiseOut();

        void onFallOut();

        void onRecover();

        void onTouchOutside();
    }

    public void setChangeDimEnabled(boolean changeDimEnabled) {
        this.changeDimEnabled = changeDimEnabled;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        canceledOnTouchOutside = cancel;
        if (canceledOnTouchOutside) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchOutside) {
                        if (swipeListener != null) {
                            swipeListener.onTouchOutside();
                        }
                        riseOut();
                    }
                }
            });
        }
    }


}
