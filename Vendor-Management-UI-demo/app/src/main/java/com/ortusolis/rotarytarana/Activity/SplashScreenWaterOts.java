package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ortusolis.rotarytarana.R;

public class SplashScreenWaterOts extends AppCompatActivity {
    String Activity;
    Intent  intentNav=null;
    //
    String Login;
    Intent intent2=null;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_water_ots);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent1 = getIntent();

                        Activity = intent1.getExtras().getString("activity");
                        if(Activity.equals("DistributorActivity")){
                            intentNav = new Intent(SplashScreenWaterOts.this, DistributorActivity.class);
                        }/*else if(Activity.equals("LoginActivity")) {
                            intentNav = new Intent(SplashScreenWaterOts.this, LoginActivity.class);
                        }*/else
                         {
                            intentNav = new Intent(SplashScreenWaterOts.this, MainActivity.class);
                        }
                        startActivity(intentNav);
                        finish();
                    }
                },
                3000);
    }
}
