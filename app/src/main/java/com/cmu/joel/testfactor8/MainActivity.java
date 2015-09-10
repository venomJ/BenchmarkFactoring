package com.cmu.joel.testfactor8;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.LogRecord;

class Global {
    public static int ivar1,ivar2;
    public static String svar1 = "",svar2 = "Go on";
    public static int[] myarray1=new int[10];
}

public class MainActivity extends ActionBarActivity {

    AlertDialog.Builder dlgAlert  = null;
    private Timer myTimer = null;
    private static String sharedString = new String();
    private long startTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dlgAlert  = new AlertDialog.Builder(this);
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

    public Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Log.d(TAG, String.format("Handler.handleMessage(): msg=%s", msg));
            // This is where main activity thread receives messages
            // Put here your handling of incoming messages posted by other threads
            if(msg.what == 999) {
                TextView tv = (TextView) findViewById(R.id.textResult);
                if (Global.svar2.length() > 0) {
                    tv.setText(Global.svar1);
                    //Global.svar1 = "Outdated";
                }
            }
            super.handleMessage(msg);
        }
    };

    public void onButtonCalcClicked(View view) {
        // clear result
        TextView tv = (TextView)findViewById(R.id.textResult);
        tv.setText("Calculating...");

        EditText mEdit = (EditText)findViewById(R.id.editNumber);
        final long num = Long.parseLong(mEdit.getText().toString());

        mEdit = (EditText)findViewById(R.id.editThread);
        final int threadCount = Integer.parseInt(mEdit.getText().toString());

        mEdit = (EditText)findViewById(R.id.editInteraction);
        final int InteractionLevel = Integer.parseInt(mEdit.getText().toString());

        startTS = System.currentTimeMillis();
        //
        CheckBox twoThreads = (CheckBox) findViewById (R.id.checkBox_2Threads);
        if(twoThreads.isChecked())
        {// we are doing 2 threads only
            // UI thread and computation thread communicate through shared variables
            // create a timer
            myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    TimerMethod();
                }

            }, 0, 1000);//every 1 second

            Global.svar1 = "";
            Global.svar2 = "Go on";
            
            try {
                RunFactor rf = new RunFactor(num, 0, 1, num, tv, Global.svar1, _handler);
                rf.act = this;
                Thread t = new Thread(rf);
                t.join();
                t.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {//original
            // Calculating in other threads
            ArrayList<Thread> tList = new ArrayList<Thread>();
            long step = num / threadCount;
            long curVal = 1;
            for(int i = 0 ; i < threadCount ; i++) {
                long s = (i == 0)? 1 : step * i;
                long e = (i == (threadCount - 1))? num : (s + step);
                RunFactor rf = new RunFactor(num, InteractionLevel, s, e, tv, null, _handler);
                rf.act = this;
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
        }

        long endTS = System.currentTimeMillis();
        long delta = endTS - startTS;
        double td = (double)delta / 1000;

        tv.setText("Time used: " + td );
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here
            TextView tv = (TextView) findViewById(R.id.textResult);
            if(Global.svar2.length() > 0) {
                tv.setText(Global.svar1);
                //Global.svar1 = "Outdated";
            }
            else
            {
                myTimer.cancel();
                myTimer.purge();
                long endTS = System.currentTimeMillis();
                long delta = endTS - startTS;
                double td = (double)delta / 1000;

                tv.setText(Global.svar1 + " Time used: " + td );
            }
        }
    };
}
