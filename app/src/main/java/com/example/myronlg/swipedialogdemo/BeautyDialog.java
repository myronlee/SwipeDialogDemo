package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sinaapp.myron.library.SwipeDialog;

/**
 * Created by myron.lg on 2015/8/12.
 */
public class BeautyDialog extends SwipeDialog {
    public BeautyDialog(Context context) {
        super(context);
    }

    public BeautyDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BeautyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        findViewById(R.id.girl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "beautiful girl", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_LONG).show();
            }
        });
//        View contentParent = findViewById(android.R.id.content);
//        if (contentParent == null) {
//            Toast.makeText(getContext(), "contentParent == null", Toast.LENGTH_LONG).show();
//        } else if (contentParent instanceof FrameLayout){
//            Toast.makeText(getContext(), "contentParent instanceof FrameLayout", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getContext(), contentParent.getClass().toString(), Toast.LENGTH_LONG).show();
//        }
//
//        Toast.makeText(getContext(), getWindow().getDecorView().getClass().toString(), Toast.LENGTH_LONG).show();

    }
}
