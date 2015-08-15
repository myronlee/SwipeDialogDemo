package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by myron.lg on 2015/8/15.
 */
public class LongTextDialog extends SwipeDialog {
    public LongTextDialog(Context context) {
        super(context);
    }

    public LongTextDialog(Context context, int theme) {
        super(context, theme);
    }

    protected LongTextDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_long_text);
        TextView textView = (TextView) findViewById(R.id.long_text);
        textView.setMovementMethod(new ScrollingMovementMethod());

    }
}
