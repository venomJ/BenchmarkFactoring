package com.cmu.joel.testfactor8;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonCalcClicked(View view) {
        // clear result
        TextView tv = (TextView)findViewById(R.id.textResult);
        tv.setText("Calculating...");

        EditText mEdit = (EditText)findViewById(R.id.editNumber);
        final long num = Long.parseLong(mEdit.getText().toString());

        mEdit = (EditText)findViewById(R.id.editThread);
        final int threadCount = Integer.parseInt(mEdit.getText().toString());

        long startTS = System.currentTimeMillis();

        ArrayList<Thread> tList = new ArrayList<Thread>();
        long step = num / threadCount;
        long curVal = 1;
        for(int i = 0 ; i < threadCount ; i++) {
            long s = (i == 0)? 1 : step * i;
            long e = (i == (threadCount - 1))? num : (s + step);
            RunFactor rf = new RunFactor(num, s, e);
            Thread t = new Thread(rf);
            tList.add(t);
            t.start();
        }

        for(int i = 0 ; i < tList.size() ; i++) {
            try {
                Thread t = tList.get(i);
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //long count = rf.getResultCount();

        long endTS = System.currentTimeMillis();
        long delta = endTS - startTS;
        double td = (double)delta / 1000;

        tv.setText("Time used: " + td );
        /*
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        String msg = "You input value is:" + num + "\n";
        msg += ("Time used: " + td + "\n");
        //msg += ("Factors found: " + count);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("Result");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        AlertDialog.Builder ok = dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dlgAlert.create().show();
        */
    }
}
