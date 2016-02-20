package com.throwapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author hallvardwestman
 */
public class RecordthrowActivity extends Activity {

    UtilThrowHeight utilThrowHeight;

    ImageView image;
    @Override
    public synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recordthrow);
        //显示正在校准中图片
        image = (ImageView) findViewById(R.id.test_image);

        utilThrowHeight=new UtilThrowHeight(this,new UtilThrowHeight.ThrowHeightCallBack() {
            @Override
            public void calibrateDevice() {
                image.post(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.ready);
                    }
                });

            }

            @Override
            public void initiateBuffer() {
                image.post(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageResource(R.drawable.weightless);
                    }
                });
            }

            @Override
            public void messureHangtime(int height) {
                /** Handler for recieving result from threads*/
            }
        });

        //float tmpNext = 0.0F;
        Log.d("StartListening", "starting listening");
        /*
         * cancelbutton while throwing, TODO: refactor code
         */
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                utilThrowHeight.stopListen();
            }
        });
    }
    /*
     * keeps orientation standar-view
     */

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }



    @Override
    public void onResume() {
        super.onResume();
        utilThrowHeight.startListen();
    }

    @Override
    public void onStop() {
        super.onStop();
        utilThrowHeight.stopListen();
        Log.d("RecordThrow", "stoppedListening");
        /*
         boolean cal = calibrateDeviceThread.isAlive();
         Log.d("RecordThrow","calibrateDeviceThread "+ cal);
         boolean mesPow = messurePowerThread.isAlive();
         Log.d("RecordThrow","messurePowerThread "+ mesPow);
         boolean mesHang = messureHangtimeThread.isAlive();
         Log.d("RecordThrow","messureHangtimeThread "+ mesHang);
        */
    }

    void cencelAction(){
        setResult(RESULT_CANCELED);
        finish();
    }
}