package com.example.myronlg.swipedialogdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sinaapp.myron.library.SwipeDialog;

/**
 * Created by myron.lg on 2015/8/15.
 */
public class ListDialog extends SwipeDialog {
    public ListDialog(Context context) {
        super(context);
    }

    public ListDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_listview);
        ListView listView = (ListView)findViewById(R.id.list);
        BaseAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
