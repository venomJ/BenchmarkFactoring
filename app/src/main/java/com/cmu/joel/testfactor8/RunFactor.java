package com.cmu.joel.testfactor8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RunFactor implements Runnable {
    public Activity act = null;
    private long nNumber = 500000;
    private long nStart = 1;
    private long nEnd = 500000;
    private long nCount = 0;
    private int interactionlvl = 0;

    //private BufferedWriter wbr = null;
    //private BufferedReader rbr = null;


    RunFactor(long n, int il, long s, long e) {
        nNumber = n;
        interactionlvl = il;
        nStart = s;
        nEnd = e;

        /*
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"factorTest.txt");
        try {
            wbr = new BufferedWriter(new FileWriter(file));
            rbr = new BufferedReader(new FileReader(file));
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
    }

    public long getResultCount() {
        return nCount;
    }

    @Override
    public void run() {
        if(interactionlvl < 0)
            interactionlvl = 0;//make sure in safe range

        /*
        if(nNumber == nEnd) {
            System.out.println("Reading configure file...");
            //readConfigFile();
        }*/
        /*
        try {
            Thread.currentThread().sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //calculateFactors(num, 1, num);
        long step = (interactionlvl == 0) ? nEnd + 1 : (nEnd - nStart) / interactionlvl;

        long nCount = 0;
        for (long l = nStart; l <= nEnd; l++) {
            if (nNumber % l == 0) {
                nCount++;
            }
            //if(interactionlvl > 0) {
                long range = l - nStart;
                if (range > 0 && range % step == 0) {
                    // do interaction here
                    //readConfigFile();
                    writeConfigFile("foo bar");
                }
           // }
            /*
            if(l % 100000 == 0) {
                //readConfigFile();
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(act);
                //String msg = "You input value is:" + num + "\n";
                //msg += ("Time used: " + td + "\n");
                //msg += ("Factors found: " + count);
                dlgAlert.setMessage("Just test");
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
            }*/
        }
        /*
        if(nNumber == nEnd) {
            System.out.println("Writing configure file...");
            String out = "Total factors: " + nCount;
            //writeConfigFile(out);

            /*
            StringBuilder text = new StringBuilder();
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard,"factorTest.txt");

                BufferedWriter br = new BufferedWriter(new FileWriter(file));
                br.write(out);
                br.close() ;
            }catch (IOException e) {
                e.printStackTrace();
            }*/
        //}
    }

    public String readConfigFile() {
        StringBuilder text = new StringBuilder();
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard,"factorTest.txt");
            BufferedReader rbr = new BufferedReader(new FileReader(file));

            String line;
            while ((line = rbr.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            rbr.close() ;
            System.out.println("Reading result: " + text);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    public boolean writeConfigFile(String config) {
        StringBuilder text = new StringBuilder();
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard,"factorTest.txt");

            BufferedWriter wbr = new BufferedWriter(new FileWriter(file));
            wbr.write(config);
            wbr.close() ;
            System.out.println("File written in thread:" + this.toString());
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
