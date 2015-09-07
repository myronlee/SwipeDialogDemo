package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;

import com.sinaapp.myron.library.SwipeDialog;

/**
 * Created by myron.lg on 2015/8/15.
 */
public class ScrollViewDialog extends SwipeDialog {
    public ScrollViewDialog(Context context) {
        super(context);
    }

    public ScrollViewDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ScrollViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_scroll_view);

    }
}
