package com.throwapp;

import android.util.Log;

/**
 * Created by cy on 2016/2/20.
 */
public class Calculate {
    static final int SBSIZE = 50;
//    static final  int GRAVITY_EARTH = 9;
    static final  int GRAVITY_EARTH = 10;
    static final  int WEIGHTLESS = 5;
    static final  int RATE = 10;
    /*
 * algorithm for calculation of hangtime-score
 */
    public static float hangtimeScoreCalc(int hangtime) {
        float i = hangtime;
        i /= 100;
        return i;
    }

    /*抛出多少厘米
 * algorithm for calculation of total-score
 */
    public static int totalScoreCalc(int power, int hangtime) {
        Log.d("LOGGING in CALC = ", " " + hangtime);
//        return power * power + power * hangtime;
        return power * hangtime*10;
    }
        /*
     * algorithm for calculation of power-score
     */

    public static int powerScoreCalc(int[] power) {

        int totalScore = 0;
        int k = 0;
        for (int i = 0; i < SBSIZE; i++) {
            if (power[i] > GRAVITY_EARTH) {
                totalScore += power[i];
                k++;
            }
        }

        Log.d("RecordThrow", "number of power readings in mStartBuffer " + k);
        /*TODO: number of readings not above 0, should
         * calculate reading for each vector above normal gravity
         *
         */

        //cant take credit for gravity (-GRAVITY_EARTH), thats just wrong
        if (k != 0) {
            return totalScore / k - GRAVITY_EARTH;
        } else {
            return totalScore;
        }
    }
        /*
     * sums up a bufferarray
     */

    public static int bufferCalc(int[] buffer) {

        int bufferSize = buffer.length;
        int totalValue = 0;
        for (int i = 0; i < bufferSize; i++) {
            totalValue += buffer[i];
        }


        Log.d("RecordThrow", "returning bufferCalc() " + totalValue);
        return totalValue;
    }
    public static int summarize() {
        int power = Calculate.powerScoreCalc(WorkThread.mStartBuffer);
        float hangtime =Calculate.hangtimeScoreCalc(WorkThread.mWeightlessDuration);

        float tmphangtime = hangtime * 100;

        int inthangtime = (int) tmphangtime;
        Log.d("LOGGING = * = 100", " " + inthangtime);
        int total = Calculate.totalScoreCalc(power, inthangtime);
        return total;
        /*
         * returning result to activity
         */
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("Power", power);
//        returnIntent.putExtra("Hangtime", hangtime);
//        returnIntent.putExtra("Total", total);
//        setResult(RESULT_OK, returnIntent);
//        finish();
    }
}
