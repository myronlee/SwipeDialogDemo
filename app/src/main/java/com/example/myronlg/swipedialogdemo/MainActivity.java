package com.example.myronlg.swipedialogdemo;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private DialogContainerFrameLayout dialogContaner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        dialogContaner = (EmbedDialogFrameLayout) findViewById(R.id.dialog_container);
//        dialogContaner.addDialogView(R.layout.dialog);
        findViewById(R.id.open_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SwipeDialogManager swipeDialogManager = new SwipeDialogManager(MainActivity.this);
//                swipeDialogManager.addDialogView(R.layout.dialog);
//                swipeDialogManager.show();
                BeautyDialog dialog = new BeautyDialog(MainActivity.this);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Toast.makeText(MainActivity.this, "dialog show", Toast.LENGTH_LONG).show();
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
//            CustomDialog dialog = new CustomDialog(this);
//            dialog.setLayoutResID(R.layout.dialog);
//            dialog.show();
//            dialogContaner.show();
            SwipeDialogManager swipeDialogManager = new SwipeDialogManager(this);
            swipeDialogManager.addDialogView(R.layout.dialog);
            swipeDialogManager.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
