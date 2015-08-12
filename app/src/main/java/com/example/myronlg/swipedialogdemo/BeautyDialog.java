package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by myron.lg on 2015/8/12.
 */
public class BeautyDialog extends SwipeDialogNew {
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
    }
}
