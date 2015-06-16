package com.cmu.joel.testfactor8;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RunFactor implements Runnable {
    private long nNumber = 500000;
    private long nStart = 1;
    private long nEnd = 500000;
    private long nCount = 0;

    RunFactor(long n, long s, long e) {
        nNumber = n;
        nStart = s;
        nEnd = e;
    }

    public long getResultCount() {
        return nCount;
    }

    @Override
    public void run() {
        if(nNumber == nEnd) {
            System.out.println("Reading configure file...");
            readConfigFile();
        }
        /*
        try {
            Thread.currentThread().sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //calculateFactors(num, 1, num);
        long nCount = 0;
        for (long l = nStart; l <= nEnd; l++) {
            if (nNumber % l == 0) {
                nCount++;
            }
            if(l % 1000000 == 0) {
                readConfigFile();
            }
        }

        if(nNumber == nEnd) {
            System.out.println("Writing configure file...");
            String out = "Total factors: " + nCount;
            writeConfigFile(out);

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
        }
    }

    public String readConfigFile() {
        StringBuilder text = new StringBuilder();
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard,"factorTest.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close() ;
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

            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write(config);
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
