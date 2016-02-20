package com.throwapp;

import android.content.Context;

/**
 * Created by cy on 2016/2/20.
 */
public class UtilThrowHeight {
    AccelerationListener accelerationListener;
    public UtilThrowHeight(Context context,ThrowHeightCallBack callBack) {
        accelerationListener = new AccelerationListener(context);
        WorkThread.setCallBack(callBack);
        WorkThread.RunThreads = true;
        WorkThread.setSensorListener(accelerationListener);
        WorkThread.calibrateDevice();
    }
    public void startListen(){
        WorkThread.RunThreads = true;
        accelerationListener.startListening();
    }
    public void stopListen(){
        WorkThread.RunThreads = false;
        accelerationListener.stopListening();
    }

    /**UtilThrowHeight 抛高流程回调*/
    public interface ThrowHeightCallBack{
       void calibrateDevice();
       void initiateBuffer();
        /**抛出多少厘米*/
       void messureHangtime(int height);
    }
}
