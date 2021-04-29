package com.example.packagebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public static MainActivity instance;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    public static Float xmax,ymax,zmax;
    BroadcastReceiver mBroadcastReceiver;
    IntentFilter mIntentFilter;
    public static Float temp;
    public static Float maxtemp;
    public static FusedLocationProviderClient fuse;

    public class PostTask extends TimerTask {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            System.out.println("Task Run");
            System.out.println(StartActivity.mode);
            if(StartActivity.mode.equals("GPS")){
                MainActivity.fuse.getLastLocation()
                        .addOnSuccessListener(MainActivity.instance, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                System.out.println("GPS SUCCESS");
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    Requester req = Requester.getInstance(MainActivity.instance);
                                    HashMap<String,Object> map = new HashMap<String,Object>();
                                    Date date = new Date();
                                    map.put("last_updated",date.toString());
                                    map.put("lat",((Double)location.getLatitude()).toString());
                                    map.put("lon",((Double)location.getLongitude()).toString());
                                    map.put("current_temp",temp.toString());
                                    map.put("max_temp",maxtemp.toString());
                                    map.put("trackingID",StartActivity.ID);
                                    map.put("max_gs",xmax.toString());
                                    req.makePOST(map);

                                }
                            }
                        });
        }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        fuse = LocationServices.getFusedLocationProviderClient(this);

        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);
        xmax = (float)0;
        ymax = (float)0;
        zmax = (float)0;
        temp = (float)0;
        maxtemp = (float)0;

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                temp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
                temp = (temp * (float)9/(float)5) + 32;
                if(temp > maxtemp){
                    maxtemp = temp;
                }
                t4.setText("Ambient Temp:" + temp.toString() + "F");
            }
        };



        mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(mBroadcastReceiver,mIntentFilter);





        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this,mAccelerometer,10000);

        Timer timer = new Timer("Update Timer");
        timer.schedule(new PostTask(),60000L,60000L);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Float x,y,z;
        Float temp;
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            xmax = (x > xmax) ? x : xmax;
            ymax = (y > ymax) ? y : ymax;
            zmax = (z > zmax) ? z : zmax;

            t1.setText("X ACCELERATION " + xmax.toString() + " m/s^2");
            t2.setText("Y ACCELERATION " + ymax.toString() + " m/s^2");
            t3.setText("Z ACCELERATION " + zmax.toString() + " m/s^2");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}