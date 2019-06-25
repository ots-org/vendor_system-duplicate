package com.android.water_distribution.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.water_distribution.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PhoneNumberActivity extends AppCompatActivity {

    Button done;
    EditText enterPhoneNumber;
    SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);
        enterPhoneNumber = findViewById(R.id.enterPhoneNumber);
        done = findViewById(R.id.done);
        sharedPreferences = getSharedPreferences("water_management",0);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enterPhoneNumber.getText().toString().isEmpty()) {

                    sharedPreferences.edit().putString("phone_number",enterPhoneNumber.getText().toString()).commit();

                    startActivity(new Intent(PhoneNumberActivity.this, OTPActivity.class));
                    finish();
                }
            }
        });
    }
}
