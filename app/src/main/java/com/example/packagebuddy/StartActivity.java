package com.example.packagebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StartActivity extends AppCompatActivity {
    TextView tvID;
    public static String ID;
    public static String mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mode = "GPS";

        getSupportActionBar().hide();
        tvID = findViewById(R.id.idView);
        ID = generateID();
        tvID.setText(ID);
        /*
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("trackingID",ID);
        m.put("lat","20.0");
        m.put("lon","-80.0");
        m.put("max_gs","100");
        m.put("max_temp","100");
        m.put("current_temp","77");
        m.put("last_updated","Oct 11 1983");

         */

        Requester req = Requester.getInstance(this);
        this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION},
                2);





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 2:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mode = "GPS";
                    requestPermissions(new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            3);


                }  else {
                   // mode = "NOGPS";
                }
                return;
            case 3:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mode = "GPS";


                }  else {
                   // mode = "NOGPS";
                }
                return;

        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    public String generateID(){
        int upperBound = 9;
        Random rnd = new Random();
        String s = "";
        for(int i = 0;i < 6;i++){
            String num = ((Integer)rnd.nextInt(upperBound)).toString();
            s = s + num;
        }
        System.out.printf("Random ID = %s\n",s);
        return s;
    }

    public void onClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}