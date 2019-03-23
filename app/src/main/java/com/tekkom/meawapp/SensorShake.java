package com.tekkom.meawapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SensorShake extends Service implements SensorEventListener {

    Float[][] deltaAccelerometer;


    /// firstUpdate & shakeInitiated
    Boolean[] initAndUpdate;

    Float shakeTresshold;


    Sensor accelerometer;
    SensorManager sensorManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value1 = event.values[0];
        float value2 = event.values[1];
        float value3 = event.values[2];

        updateDelta(value1, value2, value3);

        if ((!initAndUpdate[1]) && isAccelerationChanged()) {

            initAndUpdate[1] = true;

        } else if ((initAndUpdate[1]) && isAccelerationChanged()) {

            excecuteShakeAction();

        } else if ((initAndUpdate[1]) && !isAccelerationChanged()) {

            initAndUpdate[1] = false;

        }

    }

    private void excecuteShakeAction() {
    }

    private boolean isAccelerationChanged() {

        //Ada delta, min 2 axis

        float dx = Math.abs(deltaAccelerometer[0][1] - deltaAccelerometer[0][0]);
        float dy = Math.abs(deltaAccelerometer[1][1] - deltaAccelerometer[1][0]);
        float dz = Math.abs(deltaAccelerometer[2][1] - deltaAccelerometer[2][0]);

        return ((dx > shakeTresshold && dy > shakeTresshold) ||
                (dx > shakeTresshold && dz > shakeTresshold) ||
                (dy > shakeTresshold && dz > shakeTresshold));
    }

    private void updateDelta(float ax, float ay, float az) {

        if (initAndUpdate[0]) {

            deltaAccelerometer[0][0] = ax;
            deltaAccelerometer[1][0] = ay;
            deltaAccelerometer[2][0] = az;
            initAndUpdate[0] = false;

        } else {
            deltaAccelerometer[0][1] = ax;
            deltaAccelerometer[1][1] = ay;
            deltaAccelerometer[2][1] = az;

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        deltaAccelerometer = new Float[3][2];
        initAndUpdate = new Boolean[2];

        initAndUpdate[0] = true;
        initAndUpdate[1] = false;


        shakeTresshold = 12.5f;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


    }
}
