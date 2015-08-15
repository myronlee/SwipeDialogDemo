package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
