package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by myron.lg on 2015/8/3.
 */
public abstract class SwipeDismissDialog {
    public SwipeDismissDialog(Context context){

    }

    abstract public View onCreateView(LayoutInflater inflater, ViewGroup container);

    protected void onViewCreated(View view) {
    }

    public void show(){

    }

    public void dismisss(){

    }
}
