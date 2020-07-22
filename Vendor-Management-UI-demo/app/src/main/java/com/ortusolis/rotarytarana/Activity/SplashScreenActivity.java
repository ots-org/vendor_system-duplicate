package com.ortusolis.rotarytarana.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ortusolis.rotarytarana.R;
import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AhoyOnboarderActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("water_management",0);

        if (sharedPreferences.getBoolean("login",false)){
            Intent intent = null;
            if (sharedPreferences.getBoolean("distributor",false)) {
                intent = new Intent(SplashScreenActivity.this, DistributorActivity.class);
                intent.putExtra("activity","DistributorActivity");
                sharedPreferences.edit().putBoolean("distributor",true).commit();
            }
            else {
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.putExtra("activity","MainActivity");
            }
            startActivity(intent);
            finish();
        }

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Search Products", "Search from the range of Products to match your needs.", R.drawable.search);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Order Online", "Order for the Product from your place of comfort.", R.drawable.order);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Track Order", "Track your order turn by turn using our tracking system.", R.drawable.map);
        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.white);
        }

        setFinishButtonTitle("Get Started");
        showNavigationControls(true);
        setGradientBackground();
        setInactiveIndicatorColor(R.color.grey_600);
        setActiveIndicatorColor(R.color.white);
        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {

        startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
