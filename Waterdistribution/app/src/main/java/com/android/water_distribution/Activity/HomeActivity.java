package com.android.water_distribution.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.WebControllerInterface;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class HomeActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    TextView currentTemp, humidity, currentTemp1, humidity1, plantName, autoText, dateText, timeText;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        mToolbar = findViewById(R.id.toolbar);
        currentTemp = findViewById(R.id.currentTemp);
        humidity = findViewById(R.id.humidity);
        currentTemp1 = findViewById(R.id.currentTemp1);
        humidity1 = findViewById(R.id.humidity1);
        plantName = findViewById(R.id.plantName);
        autoText = findViewById(R.id.autoText);
        dateText = findViewById(R.id.dateEdit);
        timeText = findViewById(R.id.timeEdit);

        setSupportActionBar(mToolbar);

        Bundle bundle = getIntent().getExtras();
        String plantNameStr = bundle.getString("plant");
        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);
            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(plantNameStr);
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        plantName.setText(plantNameStr);

        if (bundle.getBoolean("automatic")){
            autoText.setText("Automatic");
            Constants.SendSms(HomeActivity.this,"/AA/");
        }
        else {
            autoText.setText("Manual");
            Constants.SendSms(HomeActivity.this,"/MM/");
        }

        if (isNetworkConnected()) {
            doneRegister();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh:
                if (isNetworkConnected()) {
                    doneRegister();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void doneRegister() {
        dateText.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        timeText.setText(new SimpleDateFormat("hh:mm aa").format(new Date()));

        WebserviceController ws = new WebserviceController(HomeActivity.this,
                new WebControllerInterface() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject mainJsonObject = jsonObject.getJSONObject("main");
                            String tempKelvin = mainJsonObject.getString("temp");
                            float celsius = Float.valueOf(tempKelvin) - 273.15F;
                            NumberFormat numberFormat = NumberFormat.getNumberInstance();
                            numberFormat.setMaximumFractionDigits(1);
                            currentTemp.setText(numberFormat.format(celsius) + " C");
                            humidity.setText(mainJsonObject.getString("humidity") + " C");

                            currentTemp1.setText(numberFormat.format(celsius) + " C");
                            humidity1.setText(mainJsonObject.getString("humidity") + " C");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        ws.sendRequest(Constants.WEATHER_API);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_home, menu);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
    }

}
