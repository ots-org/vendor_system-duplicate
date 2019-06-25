package com.android.water_distribution.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.water_distribution.R;
import com.android.water_distribution.Utility.SquareImageView;
import com.android.water_distribution.adapter.PlantAdapter;
import com.android.water_distribution.pojo.PlantModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PlantGridActivity extends AppCompatActivity{

    GridView gridview;
    FloatingActionButton fab;
    PlantAdapter plantAdapter;
    ArrayList<PlantModel> plantsString;
    Toolbar mToolbar;
    ActionBar action;
    SquareImageView squareImageView;
    String imagePath = "";
    Gson gson;
    SharedPreferences sharedPreferences;
    ImageView imageSelector;
    MenuItem searchMenuItem;
    SearchView searchView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_grid);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        gridview = (GridView) findViewById(R.id.gridview);
        mToolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        sharedPreferences = getSharedPreferences("plants",0);

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
            toolbarTitle.setText("Plant");
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        plantsString = new ArrayList<>();

        String allPlantsObj = sharedPreferences.getString("plantsobj","");
        gson = new Gson();
        ArrayList<PlantModel> plan = gson.fromJson(allPlantsObj,new TypeToken<ArrayList<PlantModel>>(){}.getType());
        if (plan!=null) {
            plantsString.addAll(plan);
        }
        else {
            addPlants("Jasmine",R.drawable.jasmine+"");
            addPlants("Rose",R.drawable.rose+"");
            addPlants("Geranium",R.drawable.geranium+"");
        }

        plantAdapter = new PlantAdapter(PlantGridActivity.this,plantsString);
        gridview.setAdapter(plantAdapter);

        plantAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(PlantGridActivity.this, view, "transition");
                int revealX = (int) (view.getX() + view.getWidth() / 2);
                int revealY = (int) (view.getY() + view.getHeight() / 2);

                Intent intent = new Intent(PlantGridActivity.this, CreatePlant.class);
                /*intent.putExtra(CreatePlant.EXTRA_CIRCULAR_REVEAL_X, revealX);
                intent.putExtra(CreatePlant.EXTRA_CIRCULAR_REVEAL_Y, revealY);
                ActivityCompat.startActivityForResult(PlantGridActivity.this, intent,21212, options.toBundle());*/
                startActivityForResult(intent,21212);
            }
        });

    }

    void addPlants(String name, String resourse){
        PlantModel plantModel = new PlantModel();
        plantModel.setPath(resourse);
        plantModel.setName(name);
        plantsString.add(plantModel);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content_plant, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                plantAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item)
            {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInputFromWindow(searchView.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN, 0);
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
                searchView.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.RESULT_SHOWN, 0);

                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, expandListener);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21212 && resultCode == RESULT_OK) {
            sharedPreferences.edit().remove("plantsobj").commit();
            String plantStr = data.getStringExtra("plant");
            plantsString.add(gson.fromJson(plantStr,PlantModel.class));
            plantAdapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sharedPreferences.edit().putString("plantsobj",gson.toJson(plantsString)).commit();
                }
            },2000);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
    }

}
