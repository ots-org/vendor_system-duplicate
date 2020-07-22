package com.ortusolis.rotarytarana.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ortusolis.rotarytarana.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OTPActivity extends AppCompatActivity {

    EditText otp_input1;
    /*EditText otp_input2;
    EditText otp_input3;
    EditText otp_input4;*/
    Button verify;
    String otp = "";
    String userId = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp_input1 = findViewById(R.id.otp_input1);
        /*otp_input2 = findViewById(R.id.otp_input2);
        otp_input3 = findViewById(R.id.otp_input3);
        otp_input4 = findViewById(R.id.otp_input4);*/
        verify = findViewById(R.id.verify);

        if (getIntent().hasExtra("otp") && getIntent().hasExtra("userId")){
            otp = getIntent().getStringExtra("otp");
            userId = getIntent().getStringExtra("userId");
        }

        /*otp_input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    otp_input2.requestFocus();
                }
            }
        });

        otp_input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    otp_input1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    otp_input3.requestFocus();
                }
            }
        });

        otp_input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    otp_input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    otp_input4.requestFocus();
                }
            }
        });

        otp_input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    otp_input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp_input1.getText().toString().isEmpty() || !otp.equalsIgnoreCase(otp_input1.getText().toString().trim()) /*&& otp_input2.getText().toString().isEmpty()
                        && otp_input3.getText().toString().isEmpty() && otp_input4.getText().toString().isEmpty()*/) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivity.this);
                    builder.setTitle("Incorrect OTP");
                    builder.setMessage("Please enter the correct OTP to reset new password.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    },3000);
                }
                else {
                    Intent intent = new Intent(OTPActivity.this,PasswordActivity.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
