/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.throwapp;

//import android.hardware.SensorListener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

/**
 *@hide
 * @author hallvardwestman
 */
public class AccelerationListener implements SensorEventListener {
    
    private SensorManager sensorManager;
    private static float initiatedEvent;
    private Handler handler;
    boolean running = true;
    /*
     * sets sensormanager from activity
     */
    public AccelerationListener(Context context){
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
    }
    
    /*
     * Initializes the sensor listening.
     */
    public void startListening() {
        
        
    	sensorManager.registerListener(this, 
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);//FAST AS POSSIBLE. IF YOU GO ANY FASTER, U R SUPRMAN! Droid on steroids
  
         
        
    }
    /*
     * Unregisters the listener
     */
    public void stopListening() {
         sensorManager.unregisterListener(this);
     }
    
    /*
     * sets current accelerometer-data
     */
    public void onSensorChanged(SensorEvent event) {
        
        int sensorType = event.sensor.getType();
        
        float x,y,z;
        
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            
            setCalculatedVector(event.values[0],event.values[1],event.values[2]);
            
            
        }
    }
    
    /*
     * Never in use
     */
    public void onAccuracyChanged(Sensor arg0, int arg1) {
       //   throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /*
     * returns accelerometer-data when polled
     */
    public synchronized float returnAcceleration(){
        
       
        return initiatedEvent;
        
    }
    public void setCalculatedVector(float x, float y, float z){
        Log.d("AccelerationHandler","X-axis"+x);
        Log.d("AccelerationHandler","Y-axis"+y);
        Log.d("AccelerationHandler","Z-axis"+z);
        try{
            //Log.d("calc", x+y+z+"");
        initiatedEvent = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    
}

