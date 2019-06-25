package com.android.water_distribution.Activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.water_distribution.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PasswordActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    EditText newPassword,reEnterNewPassword;
    Button submit;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mToolbar = findViewById(R.id.toolbar);
        newPassword = findViewById(R.id.enterPassword);
        reEnterNewPassword = findViewById(R.id.enterNewPassword);
        submit = findViewById(R.id.submit);

        setSupportActionBar(mToolbar);

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
            toolbarTitle.setText("Password");
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneRegister();
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

    boolean validate(){
        boolean valid = true;
        if (newPassword.getText().toString().isEmpty()){
            valid = false;
        }
        else if (reEnterNewPassword.getText().toString().isEmpty()){
            valid = false;
        }
        else if (!newPassword.getText().toString().equals(reEnterNewPassword.getText().toString())){
            valid = false;
        }

        return valid;
    }

    void doneRegister(){
        if (validate()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
            builder.setMessage("Password changed successfully.");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
            builder.setMessage("Enter Passwords");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            },3000);
        }
    }

}
