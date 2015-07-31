package com.example.myronlg.swipedialogdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private EmbedDialogFrameLayout dialogContaner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        dialogContaner = (EmbedDialogFrameLayout) findViewById(R.id.dialog_container);
//        dialogContaner.addDialogView(R.layout.dialog);
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
