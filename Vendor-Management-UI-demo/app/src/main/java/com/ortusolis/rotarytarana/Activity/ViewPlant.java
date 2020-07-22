package com.ortusolis.rotarytarana.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.PlantModel;
import com.google.gson.Gson;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewPlant extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView squareImageView;
    TextView plantName,plantDesc,descriptionTitle;
    String imagePath = "";
    Toolbar mToolbar;
    ActionBar action;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_view);
        fab = findViewById(R.id.fab);
        squareImageView = findViewById(R.id.picture);
        plantName = findViewById(R.id.text);
        plantDesc = findViewById(R.id.description);
        descriptionTitle = findViewById(R.id.descriptionTitle);
        mToolbar = findViewById(R.id.toolbar);

        PlantModel plantModel = new Gson().fromJson(getIntent().getStringExtra("plant"),PlantModel.class);

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
            toolbarTitle.setText(plantModel.getName());
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        plantName.setText(plantModel.getName());
        plantName.setEnabled(false);
        if (plantModel.getDescription() != null) {
            descriptionTitle.setVisibility(View.VISIBLE);
            plantDesc.setText(plantModel.getDescription());
        }
        else {
            descriptionTitle.setVisibility(View.GONE);
            plantDesc.setVisibility(View.GONE);
        }

        final String regex = "\\d+";

        if (plantModel.getPath().matches(regex)) {
            squareImageView.setImageResource(Integer.valueOf(plantModel.getPath()));
        }
        else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(plantModel.getPath(), options);
            squareImageView.setImageBitmap(bitmap);
        }

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

}
