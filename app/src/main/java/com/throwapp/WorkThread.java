package com.throwapp;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by cy on 2016/2/20.
 *{@hide}
 */

public class WorkThread {
    static int[] mCalibrationBuffer = new int[Calculate.SBSIZE];
    static int[] mStartBuffer = new int[Calculate.SBSIZE];
    static int mWeightlessDuration = 0;
    static boolean RunThreads = false;
    static Thread calibrateDeviceThread, messurePowerThread, messureHangtimeThread;
    static AccelerationListener mAccelerationListener;
    static UtilThrowHeight.ThrowHeightCallBack mCallBack;
    /**依照现场环境校准设备*/
    public static synchronized void calibrateDevice() {
        calibrateDeviceThread = new Thread() {
            int calibrationVector = 0;
            public void run() {
            /*
              * Waiting for sensor-reading
              */
                int calibrationPosition = 0;
                int calibrationCheck = 0;

                while (calibrationCheck < 8 || calibrationCheck > 10 && RunThreads) {

                    Log.d("RecordThrow", "Waiting for sensor-reading");
                    try {
                        this.sleep(10);

                        calibrationVector = (int) mAccelerationListener.returnAcceleration();

                        if (calibrationPosition == Calculate.SBSIZE - 1) {
                            calibrationPosition = 0;
                        }

                        mCalibrationBuffer[calibrationPosition] = calibrationVector;
                        calibrationPosition++;


                        calibrationCheck = Calculate.bufferCalc(mCalibrationBuffer) / Calculate.SBSIZE;
                        Log.d("RecordThrow", "calibrationCheck = " + calibrationCheck);

                    } catch (InterruptedException ex) {
                        Log.d("RecordThrow", "calibrateDevice()");
                    }
                }
                WorkThread.messurePower();
                mCallBack.calibrateDevice();

                this.interrupt();
            }
        };
        Log.d("RecordThrow", "starting initiateBuffer");
        calibrateDeviceThread.start();
    }

    public static synchronized void messurePower() {
        messurePowerThread = new Thread() {

            public void run() {

                mStartBuffer = mCalibrationBuffer;


                int totalFilterVector = 9;
                int startBufferPosition = 0;
                int[] startFilterBuffer = new int[5];

                int startFilterPosition = 0;
                int curAccel;

                while (totalFilterVector > Calculate.WEIGHTLESS && RunThreads) {
                    Log.d("RecordThrow", "Waiting for weightless state");
                    try {
                        this.sleep(Calculate.RATE);
                        curAccel = (int) mAccelerationListener.returnAcceleration();

                        Log.d("RecordThrow", "Current vector is = " + curAccel);
                    /*
                     * wrapping mStartBuffer - array
                     */
                        if (startBufferPosition == Calculate.SBSIZE - 1) {
                            startBufferPosition = 0;
                            Log.d("RecordThrow", "Wrapping startbufferarray");
                        }

                        //filling startbuffer
                        mStartBuffer[startBufferPosition] = curAccel;
                        startBufferPosition++;

                        /*
                         * FILTERING THAT USER HAS THROWN
                         */
                        startFilterBuffer[startFilterPosition] = curAccel;
                        startFilterPosition++;


                        /*wrapping filterarray
                         *
                         */
                        if (startFilterPosition == 4) {
                            startFilterPosition = 0;
                            Log.d("RecordThrow", "Resetting filterarray");
                        }

                        //setting up filter to check for weightless

                        totalFilterVector = Calculate.bufferCalc(startFilterBuffer);
                        Log.d("RecordThrow", "buffervector = " + totalFilterVector);


                    } catch (InterruptedException ex) {
                        Log.d("RecordThrow", "couldnt sleep and set buffervalue");
                    }
                }

             /*
              * device is weightless, calling function to record
              */
                WorkThread.messureHangtime();
                mCallBack.initiateBuffer();
                this.interrupt();
            }
        };
        Log.d("RecordThrow", "starting initiateBuffer");
        messurePowerThread.start();

    }
    /*
    * Messuring hangtime 悬空时间
    *
    */
    public static synchronized void messureHangtime() {
        messureHangtimeThread = new Thread() {

            public void run() {


                int curAccel = 0;
                int i = 0;
                long bootTime = SystemClock.elapsedRealtime();
                int[] endFilterBuffer = new int[5];
                int endBufferPosition = 0;
                int totalFilterVector = 0;
                mWeightlessDuration = 0;
             /*
              * reading duration of which device is weightless
              */
                while (totalFilterVector < Calculate.GRAVITY_EARTH && RunThreads) {
                    Log.d("RecordThrow", "messuring time device is weightless");
                    try {
                        this.sleep(Calculate.RATE);

                        curAccel = (int) mAccelerationListener.returnAcceleration();
                        endFilterBuffer[endBufferPosition] = curAccel;
                        endBufferPosition++;
                        Log.d("RecordThrow", "vector in weightless 'state' =" + curAccel);
                        mWeightlessDuration++;
                    } catch (InterruptedException ex) {
                    }
                    /*
                     * filter that assures current state is weightless
                     */

                    if (endBufferPosition == 4) {
                        endBufferPosition = 0;

                        Log.d("RecordThrow", "Resetting filterarray");
                    }

                    totalFilterVector = Calculate.bufferCalc(endFilterBuffer);
                    Log.d("RecordThrow", "totalbuffervector = " + totalFilterVector);
                }
                mCallBack.messureHangtime(Calculate.summarize());
                long bootTime2 = SystemClock.elapsedRealtime();
                int weightlessDuration2 = ((int) bootTime2 - (int) bootTime);
                Log.d("RecordThrow", "systemclock weightless= " + weightlessDuration2);
                this.interrupt();
            }
        };

        Log.d("RecordThrow", "starting messureHangtime");
        messureHangtimeThread.start();

    }
    public static void setSensorListener(AccelerationListener accelerationListener){
        mAccelerationListener=accelerationListener;
    }
    public static void setCallBack(UtilThrowHeight.ThrowHeightCallBack callBack){
        mCallBack=callBack;
    }
}
