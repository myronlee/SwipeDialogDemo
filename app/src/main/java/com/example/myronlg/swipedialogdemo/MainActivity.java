package com.example.myronlg.swipedialogdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.open_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BeautyDialog dialog = new BeautyDialog(MainActivity.this);
//                dialog.setChangeDimEnabled(true);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
//                        Toast.makeText(MainActivity.this, "dialog show", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "dialog dismiss", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "dialog cancel", Toast.LENGTH_LONG).show();
                    }
                });
//                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        findViewById(R.id.open_list_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new ListDialog(MainActivity.this);
                dialog.show();
            }
        });
        findViewById(R.id.open_scroll_view_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new ScrollViewDialog(MainActivity.this);
                dialog.show();
            }
        });
        findViewById(R.id.open_long_text_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new LongTextDialog(MainActivity.this);
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ListDialog dialog = new ListDialog(MainActivity.this);
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
