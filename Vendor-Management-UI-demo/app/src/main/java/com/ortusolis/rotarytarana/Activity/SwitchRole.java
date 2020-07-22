package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ortusolis.rotarytarana.R;

import java.util.ArrayList;
import java.util.List;

public class SwitchRole extends AppCompatActivity {
    Toolbar mToolbar;
    Spinner spinnerUserType;
    ActionBar action;
    List<String> userRole;
    Button updateUserSwitchRole;
    String updatedUserRole="";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_role);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        sharedPreferences = getSharedPreferences("water_management",0);
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
            toolbarTitle.setText("Switch Role");
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserSwitch);
        updateUserSwitchRole = (Button) findViewById(R.id.updateSwitchRole);
        userRole = new ArrayList();

        userRole.add("Select role");
        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){
            if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                userRole.add("Distributor");
//            userRole.add("Employee");
                userRole.add("Customer");
            }
            else if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
                userRole.add("Admin");
//            userRole.add("Employee");
                userRole.add("Customer");
            }
            else if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
            userRole.add("Admin");
                userRole.add("Distributor");
                userRole.add("Customer");
            }
            else {
            userRole.add("Admin");
                userRole.add("Distributor");
//            userRole.add("Employee");
            }
        }else {
            if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                userRole.add("Distributor");
//            userRole.add("Employee");
                userRole.add("Customer");
            }
            else if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
//                userRole.add("Admin");
//            userRole.add("Employee");
                userRole.add("Customer");
            }
            else if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
//            userRole.add("Admin");
                userRole.add("Distributor");
                userRole.add("Customer");
            }
            else {
//            userRole.add("Admin");
                userRole.add("Distributor");
//            userRole.add("Employee");
            }
        }


//        userRole.add("");
//        userRole.add("");
        spinnerUserType.setAdapter(new ArrayAdapter(SwitchRole.this, android.R.layout.simple_spinner_dropdown_item, userRole));
        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 0:

                        updatedUserRole=parent.getItemAtPosition(i).toString();
                        Toast.makeText(SwitchRole.this, updatedUserRole, Toast.LENGTH_LONG).show();
                        // Whatever you want to happen when the first item gets selected
                        break;
                    case 1:
//                        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){
//                            updatedUserRole="1";
//                        }else {
//                            updatedUserRole = "2";
//                        }
                        updatedUserRole=parent.getItemAtPosition(i).toString();
                        Toast.makeText(SwitchRole.this, updatedUserRole, Toast.LENGTH_LONG).show();
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
//                        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){
//                            updatedUserRole="2";
//                        }else {
//                            updatedUserRole="3";
//                        }
                        updatedUserRole=parent.getItemAtPosition(i).toString();
                        Toast.makeText(SwitchRole.this, updatedUserRole, Toast.LENGTH_LONG).show();
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 3:
//                        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){
//                            updatedUserRole="3";
//                        }else {
//                            updatedUserRole="4";
//                        }
                        updatedUserRole=parent.getItemAtPosition(i).toString();
                        Toast.makeText(SwitchRole.this, updatedUserRole, Toast.LENGTH_LONG).show();
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 4:
//                        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){
//                            updatedUserRole="4";
//                        }
                        updatedUserRole=parent.getItemAtPosition(i).toString();
                        Toast.makeText(SwitchRole.this, updatedUserRole, Toast.LENGTH_LONG).show();
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateUserSwitchRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserSwitchRoleForAdmin();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void updateUserSwitchRoleForAdmin(){
        if (spinnerUserType.getSelectedItemPosition()==0){
            Toast.makeText(this, "Select role to switch", Toast.LENGTH_LONG).show();
            return;
        }
        switch (updatedUserRole) {
            case "Admin":
                sharedPreferences.edit().putString("userRoleId","1").commit();
                ReloadFragment();
                Toast.makeText(this, "Selected role to Admin", Toast.LENGTH_LONG).show();
                // Whatever you want to happen when the thrid item gets selected
                break;
            case "Distributor":
                sharedPreferences.edit().putString("userRoleId","2").commit();
//                onBackPressed();
                ReloadFragment();
                Toast.makeText(this, "Selected role to Distributor", Toast.LENGTH_LONG).show();
                // Whatever you want to happen when the first item gets selected
                break;
            case "Employee":
                sharedPreferences.edit().putString("userRoleId","3").commit();
//                onBackPressed();
                ReloadFragment();
                Toast.makeText(this, "Selected role to Partner", Toast.LENGTH_LONG).show();
                // Whatever you want to happen when the second item gets selected
                break;
            case "Customer":
                sharedPreferences.edit().putString("userRoleId","4").commit();
//                onBackPressed();
                ReloadFragment();
                Toast.makeText(this, "Selected role to Customer", Toast.LENGTH_LONG).show();
                // Whatever you want to happen when the thrid item gets selected
                break;
        }
    }
    public void ReloadFragment(){
        userRole.clear();
        Intent mStartActivity = new Intent(SwitchRole.this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(SwitchRole.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)SwitchRole.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
       finishAffinity();
        finish();

    }
}
