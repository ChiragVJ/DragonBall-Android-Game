package com.example.dbgame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @author chirag vijay 27009630
 * Class for ACCELEROMETER
 */
public class Accel implements SensorEventListener {
    public Sensor mySensor; //create sensor

    private SensorManager mySensorManager; //create sensor manager
    float tilt; //global varible to store the tilt status

    public Accel(Context context){
        mySensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE); // initalise sensor manager
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //intialise sensor as accelerometer
        mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL); //register the listener with normal delay
    }

    /**
     * method that records when sensor detects change
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        tilt = event.values[1]; //get the tilt value on the y axis ( as phone horizontal x axis in game  = y of phone)

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
