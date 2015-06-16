package com.cmu.joel.testfactor8;

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

    long getResultCount() {
        return nCount;
    }

    @Override
    public void run() {
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
        }
    }
}
